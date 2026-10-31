package com.cesde.proyecto_academix.service;

import com.cesde.proyecto_academix.dto.CalificacionRequestDTO;
import com.cesde.proyecto_academix.dto.TareaRequestDTO;
import com.cesde.proyecto_academix.dto.TareaResponseDTO;
import com.cesde.proyecto_academix.dto.portal.EstudianteNotasDTO;
import com.cesde.proyecto_academix.dto.portal.FestivoDTO;
import com.cesde.proyecto_academix.dto.portal.FilaCalificacionDTO;
import com.cesde.proyecto_academix.dto.portal.GuardarCalificacionesDTO;
import com.cesde.proyecto_academix.dto.portal.HijoDTO;
import com.cesde.proyecto_academix.dto.portal.MateriaDocenteDTO;
import com.cesde.proyecto_academix.dto.portal.MateriaNotasDTO;
import com.cesde.proyecto_academix.dto.portal.ResultadoGuardadoDTO;
import com.cesde.proyecto_academix.dto.portal.TareaCalendarioDTO;
import com.cesde.proyecto_academix.exception.ReglaDeNegocioException;
import com.cesde.proyecto_academix.model.entity.Calificacion;
import com.cesde.proyecto_academix.model.entity.Materia;
import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import com.cesde.proyecto_academix.model.enums.TipoTarea;
import com.cesde.proyecto_academix.repository.CalificacionRepository;
import com.cesde.proyecto_academix.repository.MateriaRepository;
import com.cesde.proyecto_academix.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Prepara los datos en el formato EXACTO que usan las pantallas de React
 * (notas, panel docente, acudientes y calendario). Reutiliza los servicios
 * existentes para que se sigan aplicando sus reglas de negocio.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortalService {

    private final UsuarioService usuarioService;
    private final MateriaService materiaService;
    private final CalificacionService calificacionService;
    private final TareaService tareaService;
    private final UsuarioRepository usuarioRepository;
    private final MateriaRepository materiaRepository;
    private final CalificacionRepository calificacionRepository;

    // ------------------------- Estudiante y acudiente -------------------------

    /** Vista "Mis notas" del estudiante. */
    public HijoDTO notasDeEstudiante(Long estudianteId) {
        Usuario estudiante = obtenerConRol(estudianteId, RolUsuario.ESTUDIANTE);
        return convertirAHijo(estudiante, primerAcudienteId(estudiante));
    }

    /** Vista "Mis hijos": solo los estudiantes a cargo de ESE acudiente. */
    public List<HijoDTO> hijosDeAcudiente(Long acudienteId) {
        Usuario acudiente = obtenerConRol(acudienteId, RolUsuario.ACUDIENTE);
        return seguro(acudiente.getEstudiantesACargo()).stream()
                .sorted(Comparator.comparing(Usuario::getNombre))
                .map(hijo -> convertirAHijo(hijo, acudiente.getId()))
                .toList();
    }

    /** Vista "Detalle del hijo". */
    public HijoDTO hijo(Long estudianteId) {
        Usuario estudiante = obtenerConRol(estudianteId, RolUsuario.ESTUDIANTE);
        return convertirAHijo(estudiante, primerAcudienteId(estudiante));
    }

    // ------------------------------ Panel docente ------------------------------

    public List<MateriaDocenteDTO> materiasDeDocente(Long docenteId) {
        obtenerConRol(docenteId, RolUsuario.DOCENTE);
        return materiaRepository.findByDocenteIdOrderByNombreAsc(docenteId).stream()
                .map(m -> new MateriaDocenteDTO(m.getId(), m.getNombre()))
                .toList();
    }

    /**
     * Estudiantes de la materia con sus notas (null = todavía sin nota).
     * Como no existe una tabla de matrículas, se consideran inscritos los estudiantes ACTIVOS que ya
     * tienen calificación en la materia, más los estudiantes activos que aún no tienen ninguna nota
     * (recién registrados), para que el docente pueda calificarlos.
     */
    public List<EstudianteNotasDTO> estudiantesDeMateria(Long materiaId) {
        materiaService.obtenerEntidadPorId(materiaId); // 404 si la materia no existe
        Set<Long> inscritos = calificacionRepository.findByMateriaId(materiaId).stream()
                .map(c -> c.getEstudiante().getId())
                .collect(Collectors.toSet());
        return usuarioRepository.findByRolOrderByNombreAsc(RolUsuario.ESTUDIANTE).stream()
                .filter(e -> Boolean.TRUE.equals(e.getEstadoActivo()))
                .filter(e -> inscritos.contains(e.getId())
                        || calificacionRepository.findByEstudianteId(e.getId()).isEmpty())
                .sorted(Comparator.comparing((Usuario e) -> ordenGrado(e.getGrado()))
                        .thenComparing(Usuario::getNombre))
                .map(estudiante -> {
                    Optional<Calificacion> c = calificacionRepository
                            .findFirstByEstudianteIdAndMateriaId(estudiante.getId(), materiaId);
                    List<Double> notas = c.map(x -> Arrays.asList(x.getAct1(), x.getAct2(), x.getAct3()))
                            .orElse(Arrays.<Double>asList(null, null, null));
                    String observacion = c.map(Calificacion::getObservacion).orElse("");
                    String nombre = estudiante.getGrado() == null ? estudiante.getNombre()
                            : estudiante.getNombre() + " (" + estudiante.getGrado() + ")";
                    return new EstudianteNotasDTO(estudiante.getId(), nombre, notas,
                            observacion == null ? "" : observacion);
                })
                .toList();
    }

    /**
     * Guarda las notas del panel docente. Solo se guardan las filas con las 3 notas
     * completas, porque la tabla calificaciones exige act1, act2 y act3.
     */
    @Transactional
    public ResultadoGuardadoDTO guardarCalificaciones(GuardarCalificacionesDTO dto) {
        Long materiaId = convertirId(dto.materiaId());
        int guardados = 0;
        int omitidos = 0;

        for (FilaCalificacionDTO fila : seguro(dto.calificaciones())) {
            List<Double> n = fila.notas();
            boolean completa = n != null && n.size() == 3 && n.stream().allMatch(nota -> nota != null);
            if (!completa) {
                omitidos++;
                continue;
            }
            Optional<Calificacion> existente =
                    calificacionRepository.findFirstByEstudianteIdAndMateriaId(fila.id(), materiaId);
            if (existente.isPresent()) {
                calificacionService.actualizar(existente.get().getId(), new CalificacionRequestDTO(
                        null, null, n.get(0), n.get(1), n.get(2), fila.observacion()));
            } else {
                calificacionService.crear(new CalificacionRequestDTO(
                        fila.id(), materiaId, n.get(0), n.get(1), n.get(2), fila.observacion()));
            }
            guardados++;
        }
        return new ResultadoGuardadoDTO(true, guardados, omitidos);
    }

    // -------------------------------- Calendario --------------------------------

    public List<TareaCalendarioDTO> tareasCalendario() {
        return tareaService.listarTodas().stream().map(this::convertirATareaCalendario).toList();
    }

    @Transactional
    public TareaCalendarioDTO crearTareaCalendario(TareaCalendarioDTO dto) {
        if (dto.materia() == null || dto.materia().isBlank()) {
            throw new ReglaDeNegocioException("Debe seleccionar una materia");
        }
        Materia materia = materiaRepository.findFirstByNombreIgnoreCase(dto.materia().trim())
                .orElseThrow(() -> new ReglaDeNegocioException(
                        "La materia " + dto.materia() + " no existe en Academix"));

        // Se reutiliza TareaService para aplicar sus reglas (fecha no anterior a hoy, materia activa)
        TareaResponseDTO creada = tareaService.crear(new TareaRequestDTO(dto.titulo(), dto.descripcion(),
                materia.getId(), convertirTipo(dto.tipo()), dto.grado(), dto.fecha(), dto.observaciones()));
        return convertirATareaCalendario(creada);
    }

    /** Festivos de Colombia 2026. */
    public List<FestivoDTO> festivos() {
        return List.of(
                new FestivoDTO("2026-01-01", "Año Nuevo"),
                new FestivoDTO("2026-01-12", "Reyes Magos"),
                new FestivoDTO("2026-03-23", "San José"),
                new FestivoDTO("2026-04-02", "Jueves Santo"),
                new FestivoDTO("2026-04-03", "Viernes Santo"),
                new FestivoDTO("2026-05-01", "Día del Trabajo"),
                new FestivoDTO("2026-05-18", "Ascensión del Señor"),
                new FestivoDTO("2026-06-08", "Corpus Christi"),
                new FestivoDTO("2026-06-15", "Sagrado Corazón"),
                new FestivoDTO("2026-06-29", "San Pedro y San Pablo"),
                new FestivoDTO("2026-07-20", "Día de la Independencia"),
                new FestivoDTO("2026-08-07", "Batalla de Boyacá"),
                new FestivoDTO("2026-08-17", "Asunción de la Virgen"),
                new FestivoDTO("2026-10-12", "Día de la Raza"),
                new FestivoDTO("2026-11-02", "Todos los Santos"),
                new FestivoDTO("2026-11-16", "Independencia de Cartagena"),
                new FestivoDTO("2026-12-08", "Inmaculada Concepción"),
                new FestivoDTO("2026-12-25", "Navidad"));
    }

    // -------------------------------- Ayudantes --------------------------------

    private Usuario obtenerConRol(Long id, RolUsuario rolEsperado) {
        Usuario usuario = usuarioService.obtenerEntidadPorId(id);
        if (usuario.getRol() != rolEsperado) {
            throw new ReglaDeNegocioException("El usuario " + id + " no tiene rol " + rolEsperado);
        }
        return usuario;
    }

    private HijoDTO convertirAHijo(Usuario estudiante, Long acudienteId) {
        List<MateriaNotasDTO> materias = new ArrayList<>(calificacionRepository.findByEstudianteId(estudiante.getId())
                .stream()
                .map(c -> new MateriaNotasDTO(c.getMateria().getNombre(),
                        List.of(c.getAct1(), c.getAct2(), c.getAct3())))
                .toList());
        materias.sort(Comparator.comparing(MateriaNotasDTO::nombre));
        String grado = estudiante.getGrado() == null ? "" : estudiante.getGrado();
        return new HijoDTO(estudiante.getId(), acudienteId, estudiante.getNombre(), grado, materias);
    }

    // "7B" -> "07B" para que el 10 y el 11 queden después del 9 al ordenar
    private static String ordenGrado(String grado) {
        if (grado == null) {
            return "";
        }
        return grado.length() == 2 ? "0" + grado : grado;
    }

    private Long primerAcudienteId(Usuario estudiante) {
        return seguro(estudiante.getAcudientes()).stream()
                .map(Usuario::getId)
                .sorted()
                .findFirst()
                .orElse(null);
    }

    private TareaCalendarioDTO convertirATareaCalendario(TareaResponseDTO t) {
        return new TareaCalendarioDTO(t.id(), t.titulo(), t.descripcion(), t.materiaNombre(),
                textoTipo(t.tipo()), t.grado(), t.fecha(), t.observaciones() == null ? "" : t.observaciones());
    }

    // "Investigación" -> INVESTIGACION (quita tildes y pasa a mayúsculas)
    private TipoTarea convertirTipo(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ReglaDeNegocioException("Debe seleccionar el tipo de actividad");
        }
        String limpio = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT);
        try {
            return TipoTarea.valueOf(limpio);
        } catch (IllegalArgumentException e) {
            throw new ReglaDeNegocioException("Tipo de actividad no válido: " + texto);
        }
    }

    // INVESTIGACION -> "Investigación" (como se muestra en React)
    private String textoTipo(TipoTarea tipo) {
        return switch (tipo) {
            case TAREA -> "Tarea";
            case INVESTIGACION -> "Investigación";
            case LABORATORIO -> "Laboratorio";
        };
    }

    private Long convertirId(String texto) {
        try {
            return Long.valueOf(texto.trim());
        } catch (RuntimeException e) {
            throw new ReglaDeNegocioException("Materia no válida: " + texto);
        }
    }

    private static <T> List<T> seguro(List<T> lista) {
        return lista == null ? List.of() : lista;
    }

    private static <T> Set<T> seguro(Set<T> conjunto) {
        return conjunto == null ? Set.of() : conjunto;
    }
}
