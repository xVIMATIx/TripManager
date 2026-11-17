-- Esquema de Base de Datos TripManager
-- Versión: 5
-- Generado desde el código Room Database

-- Tabla: users
CREATE TABLE IF NOT EXISTS users (
    username TEXT PRIMARY KEY NOT NULL,
    password TEXT NOT NULL
);

-- Tabla: trips
CREATE TABLE IF NOT EXISTS trips (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    destination TEXT NOT NULL,
    startDate INTEGER NOT NULL,
    endDate INTEGER NOT NULL,
    tripType TEXT NOT NULL,  -- 'VACATION' o 'WORK'
    username TEXT NOT NULL,
    FOREIGN KEY(username) REFERENCES users(username)
);

-- Tabla: documents
CREATE TABLE IF NOT EXISTS documents (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    tripId INTEGER NOT NULL,
    title TEXT NOT NULL,
    fileName TEXT NOT NULL,
    filePath TEXT NOT NULL,
    fileType TEXT NOT NULL,
    createdAt INTEGER NOT NULL,
    FOREIGN KEY(tripId) REFERENCES trips(id)
);

-- Tabla: itinerary_items
CREATE TABLE IF NOT EXISTS itinerary_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    tripId INTEGER NOT NULL,
    activityDateTime INTEGER NOT NULL,
    description TEXT NOT NULL,
    hasReminder INTEGER NOT NULL,  -- 0 = false, 1 = true
    reminderTimeBefore TEXT NOT NULL,
    alertType TEXT NOT NULL,  -- 'IMPORTANT' o 'NORMAL'
    FOREIGN KEY(tripId) REFERENCES trips(id)
);

-- Tabla: saved_flights
CREATE TABLE IF NOT EXISTS saved_flights (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    flightNumber TEXT NOT NULL,
    origin TEXT NOT NULL,
    originName TEXT,
    destination TEXT NOT NULL,
    destinationName TEXT,
    departureTime INTEGER NOT NULL,
    arrivalTime INTEGER NOT NULL,
    airline TEXT,
    gate TEXT,
    status TEXT NOT NULL,  -- 'EN_TIEMPO', 'RETRASADO', 'CANCELADO', 'EN_VUELO', 'PROGRAMADO', 'DESCONOCIDO'
    delayMinutes INTEGER NOT NULL DEFAULT 0,
    dateAdded INTEGER NOT NULL
);

-- Tabla: packing_items
CREATE TABLE IF NOT EXISTS packing_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    tripId INTEGER,  -- NULL = plantilla global
    category TEXT NOT NULL,  -- 'ROPA', 'CALZADO', 'ARTICULOS_HIGIENE', 'ELECTRONICOS', 'DOCUMENTOS', 'MEDICINA', 'COMIDA', 'OTROS'
    item TEXT NOT NULL,
    isChecked INTEGER NOT NULL DEFAULT 0,  -- 0 = false, 1 = true
    isCustom INTEGER NOT NULL DEFAULT 1,  -- 0 = false, 1 = true
    FOREIGN KEY(tripId) REFERENCES trips(id)
);

-- Índices sugeridos para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_trips_username ON trips(username);
CREATE INDEX IF NOT EXISTS idx_documents_tripId ON documents(tripId);
CREATE INDEX IF NOT EXISTS idx_itinerary_items_tripId ON itinerary_items(tripId);
CREATE INDEX IF NOT EXISTS idx_packing_items_tripId ON packing_items(tripId);
CREATE INDEX IF NOT EXISTS idx_saved_flights_dateAdded ON saved_flights(dateAdded);

-- Comentarios sobre los enums utilizados:

-- TripType: VACATION, WORK
-- AlertType: IMPORTANT, NORMAL
-- FlightStatus: EN_TIEMPO, RETRASADO, CANCELADO, EN_VUELO, PROGRAMADO, DESCONOCIDO
-- PackingCategory: ROPA, CALZADO, ARTICULOS_HIGIENE, ELECTRONICOS, DOCUMENTOS, MEDICINA, COMIDA, OTROS

