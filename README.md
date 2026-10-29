# Academix API

API REST de gestión académica (usuarios, materias, tareas, calificaciones y boletines) hecha con **Spring Boot**, **JPA** y **PostgreSQL**, aplicando el **patrón DTO** y manejo global de errores.

- Base URL local: `http://localhost:8080`
- Formato: JSON (en Postman: **Body → raw → JSON**)

---

## 1. Cómo correrlo

1. Java 21 o superior (el `pom.xml` pide 25; si tienes otra versión usa `-Djava.version=21`).
2. Crea un archivo `.env` en la raíz del proyecto (junto al `pom.xml`) con tus datos de PostgreSQL:

   ```
   DB_URL=jdbc:postgresql://TU_HOST:5432/TU_BD?sslmode=require
   DB_USERNAME=tu_usuario
   DB_PASSWORD=tu_clave
   ```

3. Ejecuta desde la carpeta del proyecto:

   ```powershell
   .\mvnw spring-boot:run
   # con otra versión de Java:
   .\mvnw spring-boot:run "-Djava.version=21"
   ```

Cuando veas `Started ProyectoAcademixApplication`, el servidor está listo en el puerto 8080. Las tablas se crean solas.

---

## 2. Roles de usuario

| Rol | Qué es | Reglas que hoy aplica la API |
| --- | --- | --- |
| `ESTUDIANTE` | Alumno | **Obligatorio** tener `grado`. Es el único rol que puede recibir calificaciones y tener boletín. |
| `DOCENTE` | Profesor | Es el único rol que se puede asignar como docente de una materia. No lleva `grado`. |
| `ACUDIENTE` | Padre, madre o responsable | No lleva `grado`. Por ahora no tiene reglas propias ni endpoint para vincularlo a un estudiante. |
| `ADMIN` | Administrador | No lleva `grado`. Por ahora no tiene reglas propias (la API aún no tiene autenticación). |

Reglas generales de usuario:

- El `correo` es **único** en todo el sistema.
- Solo los usuarios con rol `ESTUDIANTE` pueden tener `grado`; los demás roles **no** deben enviarlo.
- El `password` se recibe al crear/actualizar pero **nunca** aparece en las respuestas.

---

## 3. Endpoints

Todos los recursos tienen las mismas 5 operaciones:

| Método | Ruta | Qué hace | Código de éxito |
| --- | --- | --- | --- |
| `GET` | `/api/{recurso}` | Lista todos | `200` |
| `GET` | `/api/{recurso}/{id}` | Obtiene uno | `200` |
| `POST` | `/api/{recurso}` | Crea uno | `201` |
| `PUT` | `/api/{recurso}/{id}` | Actualiza | `200` |
| `DELETE` | `/api/{recurso}/{id}` | Elimina | `204` |

Recursos: `usuarios`, `materias`, `tareas`, `calificaciones`, `boletines`.

Errores (siempre con este formato):

```json
{ "mensaje": "Descripción del error" }
```

| Código | Cuándo |
| --- | --- |
| `404` | El registro no existe (por ejemplo `GET /api/usuarios/9999`). |
| `400` | Se viola una regla de negocio o el JSON es inválido (enum mal escrito, fecha con mal formato). |

---

## 4. Orden recomendado para probar en Postman

Los datos dependen unos de otros, así que créalos en este orden:

1. Usuario **DOCENTE**
2. Usuario **ESTUDIANTE**
3. Usuario **ACUDIENTE** y **ADMIN** (opcionales)
4. **Materia** (necesita el id del docente)
5. **Tarea** (necesita el id de la materia)
6. **Calificación** (necesita ids de estudiante y materia)
7. **Boletín** (necesita el id del estudiante)

> **Importante sobre los `id`:** cada `POST` devuelve el `id` del registro creado. Úsalo en los pasos siguientes. En estos ejemplos se usan `1`, `2`, etc. Si tu base de datos ya tiene datos, los ids serán otros: reemplázalos por los reales. Los correos y códigos son únicos, así que si te dice que ya existen, cámbialos.

---

## 5. Usuarios — `/api/usuarios`

Campos: `nombre`, `correo`, `password`, `rol`, `grado` (solo `ESTUDIANTE`), `contacto` (opcional: `telefono`, `departamento`, `ciudad`).

### 5.1 Crear DOCENTE — `POST /api/usuarios`

```json
{
  "nombre": "Carlos Ruiz",
  "correo": "carlos.ruiz@cesde.edu",
  "password": "1234",
  "rol": "DOCENTE",
  "contacto": {
    "telefono": "3001112233",
    "departamento": "Antioquia",
    "ciudad": "Medellín"
  }
}
```

Respuesta `201` (observa que **no** trae `password`):

```json
{
  "id": 1,
  "nombre": "Carlos Ruiz",
  "correo": "carlos.ruiz@cesde.edu",
  "rol": "DOCENTE",
  "grado": null,
  "contacto": { "telefono": "3001112233", "departamento": "Antioquia", "ciudad": "Medellín" },
  "estadoActivo": true,
  "fechaCreacion": "2026-10-28T19:30:00"
}
```

### 5.2 Crear ESTUDIANTE — `POST /api/usuarios`

```json
{
  "nombre": "Laura Gómez",
  "correo": "laura.gomez@cesde.edu",
  "password": "1234",
  "rol": "ESTUDIANTE",
  "grado": "10A",
  "contacto": {
    "telefono": "3004445566",
    "departamento": "Antioquia",
    "ciudad": "Medellín"
  }
}
```

### 5.3 Crear ACUDIENTE — `POST /api/usuarios`

```json
{
  "nombre": "María Gómez",
  "correo": "maria.gomez@cesde.edu",
  "password": "1234",
  "rol": "ACUDIENTE"
}
```

### 5.4 Crear ADMIN — `POST /api/usuarios`

```json
{
  "nombre": "Admin Academix",
  "correo": "admin@cesde.edu",
  "password": "1234",
  "rol": "ADMIN"
}
```

### 5.5 Otras operaciones

- Listar: `GET /api/usuarios`
- Uno: `GET /api/usuarios/2`
- Actualizar: `PUT /api/usuarios/2` (se envía el usuario completo, con los mismos campos del `POST`)

  ```json
  {
    "nombre": "Laura Gómez Pérez",
    "correo": "laura.gomez@cesde.edu",
    "password": "1234",
    "rol": "ESTUDIANTE",
    "grado": "11A"
  }
  ```

- Eliminar: `DELETE /api/usuarios/2`

---

## 6. Materias — `/api/materias`

Campos: `nombre`, `codigo` (**único**), `docenteId` (debe ser un usuario con rol `DOCENTE`).

### 6.1 Crear — `POST /api/materias`

```json
{
  "nombre": "Matemáticas",
  "codigo": "MAT101",
  "docenteId": 1
}
```

Respuesta `201`:

```json
{
  "id": 1,
  "nombre": "Matemáticas",
  "codigo": "MAT101",
  "docenteId": 1,
  "docenteNombre": "Carlos Ruiz",
  "estadoActivo": true
}
```

### 6.2 Actualizar — `PUT /api/materias/1`

```json
{
  "nombre": "Matemáticas Avanzadas",
  "codigo": "MAT101",
  "docenteId": 1
}
```

Reglas:

- Si el `codigo` ya existe en otra materia → `400`.
- Si `docenteId` es de un usuario que no es `DOCENTE` → `400`.
- Si `docenteId` no existe → `404`.

---

## 7. Tareas — `/api/tareas`

Campos: `titulo`, `descripcion`, `materiaId`, `tipo`, `grado`, `fecha`, `observaciones`.

Valores de `tipo`: `TAREA`, `INVESTIGACION`, `LABORATORIO`.
Formato de `fecha`: `AAAA-MM-DD` (ejemplo: `2026-12-01`).

### 7.1 Crear — `POST /api/tareas`

```json
{
  "titulo": "Taller de ecuaciones",
  "descripcion": "Resolver los ejercicios 1 al 10 del capítulo 3",
  "materiaId": 1,
  "tipo": "TAREA",
  "grado": "10A",
  "fecha": "2026-12-01",
  "observaciones": "Entregar en hoja cuadriculada"
}
```

### 7.2 Actualizar — `PUT /api/tareas/1`

```json
{
  "titulo": "Taller de ecuaciones (versión corregida)",
  "descripcion": "Resolver los ejercicios 1 al 12 del capítulo 3",
  "materiaId": 1,
  "tipo": "LABORATORIO",
  "grado": "10A",
  "fecha": "2026-12-05",
  "observaciones": "Se amplió el plazo"
}
```

Reglas:

- La `fecha` **no puede ser anterior a hoy** → si lo es, `400`. Usa siempre una fecha futura.
- La materia debe existir (`404`) y estar activa (`400`).

---

## 8. Calificaciones — `/api/calificaciones`

Campos: `estudianteId` (rol `ESTUDIANTE`), `materiaId`, `act1`, `act2`, `act3` (cada nota entre `0.0` y `5.0`), `observacion` (opcional).

### 8.1 Crear — `POST /api/calificaciones`

```json
{
  "estudianteId": 2,
  "materiaId": 1,
  "act1": 4.0,
  "act2": 3.5,
  "act3": 5.0,
  "observacion": "Buen desempeño"
}
```

Respuesta `201` (incluye el `promedio`, que se calcula solo):

```json
{
  "id": 1,
  "estudianteId": 2,
  "estudianteNombre": "Laura Gómez",
  "materiaId": 1,
  "materiaNombre": "Matemáticas",
  "act1": 4.0,
  "act2": 3.5,
  "act3": 5.0,
  "promedio": 4.166666666666667,
  "observacion": "Buen desempeño"
}
```

### 8.2 Actualizar — `PUT /api/calificaciones/1`

La actualización es **parcial**: solo envía lo que quieres cambiar.

```json
{
  "act2": 4.5,
  "observacion": "Mejoró en la segunda actividad"
}
```

Reglas:

- El usuario asignado como estudiante debe tener rol `ESTUDIANTE` → si no, `400`.
- Cada nota debe estar entre `0.0` y `5.0` → si no, `400`.

---

## 9. Boletines académicos — `/api/boletines`

Campos: `estudianteId`, `promedioGeneral` (entre `0` y `5`), `materiasAprobadas`, `materiasEnRiesgo`.

### 9.1 Crear — `POST /api/boletines`

```json
{
  "estudianteId": 2,
  "promedioGeneral": 4.2,
  "materiasAprobadas": 5,
  "materiasEnRiesgo": 1
}
```

Respuesta `201`:

```json
{
  "id": 1,
  "estudianteId": 2,
  "estudianteNombre": "Laura Gómez",
  "promedioGeneral": 4.2,
  "materiasAprobadas": 5,
  "materiasEnRiesgo": 1
}
```

### 9.2 Actualizar — `PUT /api/boletines/1`

Solo se actualizan las métricas; el estudiante de un boletín no se cambia.

```json
{
  "promedioGeneral": 4.4,
  "materiasAprobadas": 6,
  "materiasEnRiesgo": 0
}
```

Reglas:

- **Un solo boletín por estudiante** → si ya tiene uno, `400`.
- `promedioGeneral` entre `0` y `5` → si no, `400`.
- El estudiante debe existir → si no, `404`.

---

## 10. Pruebas de errores (para mostrar que las reglas funcionan)

| Prueba | Petición | Resultado esperado |
| --- | --- | --- |
| Usuario inexistente | `GET /api/usuarios/9999` | `404` `{ "mensaje": "No existe un usuario con el id 9999" }` |
| Correo repetido | `POST /api/usuarios` con un correo ya usado | `400` |
| Estudiante sin grado | `POST /api/usuarios` con `"rol": "ESTUDIANTE"` y sin `grado` | `400` |
| Docente con grado | `POST /api/usuarios` con `"rol": "DOCENTE"` y `"grado": "10A"` | `400` |
| Materia con un no docente | `POST /api/materias` con `docenteId` de un estudiante | `400` |
| Código de materia repetido | `POST /api/materias` con un `codigo` existente | `400` |
| Tarea con fecha pasada | `POST /api/tareas` con `"fecha": "2020-01-01"` | `400` |
| Nota fuera de rango | `POST /api/calificaciones` con `"act1": 6.0` | `400` |
| Calificación a un docente | `POST /api/calificaciones` con `estudianteId` de un docente | `400` |
| Segundo boletín | `POST /api/boletines` dos veces para el mismo estudiante | `400` |
| Rol inválido | `POST /api/usuarios` con `"rol": "VISITANTE"` | `400` |

---

## 11. Estructura del proyecto

```
src/main/java/com/cesde/proyecto_academix/
├── controller/   → reciben RequestDTO y devuelven ResponseDTO
├── service/      → reglas de negocio y conversión DTO ⇄ entidad
├── repository/   → acceso a la base de datos (JPA)
├── model/        → entidades, enums y base común
├── dto/          → objetos de entrada y salida (sin password)
└── exception/    → excepciones propias + manejador global de errores
```

### ¿Qué es un DTO?

Un **DTO** (*Data Transfer Object*) es una clase sencilla que solo transporta datos entre el cliente y el servidor.

- **RequestDTO:** lo que el cliente envía al crear o actualizar (sin `id`).
- **ResponseDTO:** lo que la API devuelve (oculta datos sensibles como el `password`).

Flujo de una petición:

```
Postman ─RequestDTO─▶ Controller ─▶ Service ─Entidad─▶ Base de datos
Postman ◀─ResponseDTO─ Controller ◀─ Service ◀─Entidad─ Base de datos
```
