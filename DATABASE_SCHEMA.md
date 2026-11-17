# Esquema de Base de Datos - TripManager

## Resumen
Base de datos Room (SQLite) con 6 entidades principales para gestionar viajes, usuarios, documentos, itinerarios, vuelos y equipaje.

**Versión de la base de datos:** 5

---

## Entidades

### 1. users
Tabla de usuarios del sistema.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| **username** | String | PRIMARY KEY | Nombre de usuario único |
| password | String | NOT NULL | Contraseña del usuario |

**Relaciones:**
- Un usuario puede tener múltiples viajes (1:N)

---

### 2. trips
Tabla principal de viajes.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| **id** | Long | PRIMARY KEY, AUTO_INCREMENT | Identificador único del viaje |
| destination | String | NOT NULL | Destino del viaje |
| startDate | Long | NOT NULL | Fecha de inicio (timestamp) |
| endDate | Long | NOT NULL | Fecha de fin (timestamp) |
| tripType | TripType | NOT NULL | Tipo de viaje (enum) |
| username | String | FOREIGN KEY → users.username | Usuario propietario del viaje |

**Enums:**
- `TripType`: VACATION, WORK

**Relaciones:**
- Pertenece a un usuario (N:1) → users
- Puede tener múltiples documentos (1:N) → documents
- Puede tener múltiples items de itinerario (1:N) → itinerary_items
- Puede tener múltiples items de equipaje (1:N) → packing_items

---

### 3. documents
Documentos asociados a un viaje.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| **id** | Long | PRIMARY KEY, AUTO_INCREMENT | Identificador único del documento |
| tripId | Long | FOREIGN KEY → trips.id | Viaje al que pertenece |
| title | String | NOT NULL | Título del documento |
| fileName | String | NOT NULL | Nombre del archivo |
| filePath | String | NOT NULL | Ruta del archivo |
| fileType | String | NOT NULL | Tipo de archivo |
| createdAt | Long | NOT NULL | Fecha de creación (timestamp) |

**Relaciones:**
- Pertenece a un viaje (N:1) → trips

---

### 4. itinerary_items
Items del itinerario de un viaje.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| **id** | Long | PRIMARY KEY, AUTO_INCREMENT | Identificador único del item |
| tripId | Long | FOREIGN KEY → trips.id | Viaje al que pertenece |
| activityDateTime | Long | NOT NULL | Fecha y hora de la actividad (timestamp) |
| description | String | NOT NULL | Descripción (máx 200 caracteres) |
| hasReminder | Boolean | NOT NULL | Indica si tiene recordatorio |
| reminderTimeBefore | String | NOT NULL | Tiempo antes del recordatorio (ej: "1 hora", "30 min") |
| alertType | AlertType | NOT NULL | Tipo de alerta (enum) |

**Enums:**
- `AlertType`: IMPORTANT, NORMAL

**Relaciones:**
- Pertenece a un viaje (N:1) → trips

---

### 5. saved_flights
Vuelos guardados (independientes, no relacionados directamente con viajes).

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| **id** | Long | PRIMARY KEY, AUTO_INCREMENT | Identificador único del vuelo |
| flightNumber | String | NOT NULL | Número de vuelo (ej: "AA123") |
| origin | String | NOT NULL | Código del aeropuerto de origen |
| originName | String | NULLABLE | Nombre del aeropuerto de origen |
| destination | String | NOT NULL | Código del aeropuerto de destino |
| destinationName | String | NULLABLE | Nombre del aeropuerto de destino |
| departureTime | Long | NOT NULL | Hora de salida (timestamp) |
| arrivalTime | Long | NOT NULL | Hora de llegada (timestamp) |
| airline | String | NULLABLE | Aerolínea |
| gate | String | NULLABLE | Puerta de embarque |
| status | FlightStatus | NOT NULL | Estado del vuelo (enum) |
| delayMinutes | Int | NOT NULL, DEFAULT 0 | Minutos de retraso |
| dateAdded | Long | NOT NULL | Fecha de agregado (timestamp) |

**Enums:**
- `FlightStatus`: EN_TIEMPO, RETRASADO, CANCELADO, EN_VUELO, PROGRAMADO, DESCONOCIDO

**Relaciones:**
- Ninguna (tabla independiente)

---

### 6. packing_items
Items de equipaje para un viaje o plantillas globales.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| **id** | Long | PRIMARY KEY, AUTO_INCREMENT | Identificador único del item |
| tripId | Long? | FOREIGN KEY → trips.id, NULLABLE | Viaje al que pertenece (null = plantilla global) |
| category | PackingCategory | NOT NULL | Categoría del item (enum) |
| item | String | NOT NULL | Nombre del item |
| isChecked | Boolean | NOT NULL, DEFAULT false | Indica si está marcado |
| isCustom | Boolean | NOT NULL, DEFAULT true | true = agregado por usuario, false = de plantilla |

**Enums:**
- `PackingCategory`: ROPA, CALZADO, ARTICULOS_HIGIENE, ELECTRONICOS, DOCUMENTOS, MEDICINA, COMIDA, OTROS

**Relaciones:**
- Puede pertenecer a un viaje (N:1) → trips (opcional, nullable para plantillas globales)

---

## Diagrama de Relaciones

```
users (1) ────────< (N) trips (1) ────────< (N) documents
                              │
                              ├───────────────────< (N) itinerary_items
                              │
                              └───────────────────< (N) packing_items

saved_flights (independiente)
```

---

## Convertidores de Tipos (TypeConverters)

La base de datos utiliza convertidores de tipos para almacenar enums como strings:

1. **FlightStatusConverter**: Convierte `FlightStatus` ↔ String
2. **PackingCategoryConverter**: Convierte `PackingCategory` ↔ String

Los enums `TripType` y `AlertType` también se almacenan como strings en la base de datos.

---

## Migraciones

La base de datos ha pasado por las siguientes migraciones:

- **v1 → v2**: Creación de tabla `documents`
- **v2 → v3**: Creación de tabla `itinerary_items`
- **v3 → v4**: Creación de tabla `saved_flights`
- **v4 → v5**: Creación de tabla `packing_items`

---

## Notas de Diseño

1. **Claves Foráneas**: Las relaciones se manejan mediante campos de clave foránea, pero Room no fuerza restricciones de integridad referencial a nivel de base de datos por defecto.

2. **Timestamps**: Todas las fechas se almacenan como `Long` (timestamp en milisegundos desde epoch).

3. **Plantillas Globales**: Los `packing_items` con `tripId = null` representan plantillas globales reutilizables.

4. **Vuelos Independientes**: `saved_flights` no tiene relación directa con `trips`, permitiendo guardar vuelos de forma independiente.

