# Trip Manager · 1.7.6

Aplicación Android para organizar viajes, itinerarios, documentos, vuelos y equipaje.

Esta actualización incorpora la versión local 1.7.6 (versionCode 76), procedente de [TripManagerE5B1, commit ada200e](https://github.com/babylongirl14/TripManagerE5B1/commit/ada200ee3e021e284394b1c4a9171eb3ea566907), en el repositorio del equipo **xVIMATIx/TripManager**. Se conserva el historial previo del repositorio.

## Probar sin Android Studio

1. Abrir [Actions → Android APK](https://github.com/xVIMATIx/TripManager/actions/workflows/android.yml).
2. Entrar a la ejecución más reciente de `main` que aparezca en verde.
3. En **Artifacts**, descargar `TripManager-1.7.6-debug` e iniciar sesión en GitHub si se solicita.
4. Descomprimir el ZIP e instalar `app-debug.apk` en un teléfono con **Android 7.0 o posterior**. Autorizar la instalación desde el navegador o gestor de archivos cuando Android lo pida.

La APK está firmada para depuración y destinada a pruebas. Si Android rechaza una actualización porque la instalación anterior tenía otra firma, respaldar lo necesario y desinstalar la versión anterior; desinstalar borra sus datos locales. Los artefactos de Actions se conservan 30 días; se pueden regenerar con **Run workflow**.

## Abrir y compilar el proyecto

- Abrir la raíz del repositorio en Android Studio.
- Usar **JDK 17**, Android SDK **36** y Build Tools **36.0.0**. El wrapper descarga Gradle **8.13**.
- Configurar el SDK desde Android Studio o mediante `ANDROID_HOME`. No subir `local.properties`.
- En Windows: `gradlew.bat assembleDebug testDebugUnitTest lintDebug`.
- En Linux/macOS: `bash ./gradlew assembleDebug testDebugUnitTest lintDebug`.
- APK generada: `app/build/outputs/apk/debug/app-debug.apk`.

## Pruebas del equipo

1. Registrar una cuenta local (contraseña de al menos seis caracteres), iniciar sesión y comprobar el cierre de sesión.
2. Crear, editar y eliminar un viaje; cerrar y reabrir la app para comprobar persistencia.
3. Agregar actividades al itinerario y probar recordatorios, concediendo permisos de notificación y de alarmas cuando corresponda.
4. Adjuntar y abrir documentos; establecer, cambiar y restablecer el PIN.
5. Agregar vuelos manualmente y comprobar las listas de equipaje.
6. Revisar recomendaciones de preparación y conversión de zonas horarias.

Reportar fallos con modelo del teléfono, versión de Android, pasos para reproducir y captura o mensaje del error. Las comprobaciones de compilación no sustituyen estas pruebas en dispositivo.

## Alcance de esta versión

- Datos y cuentas almacenados localmente con Room/SQLite; no hay sincronización entre teléfonos.
- Vuelos de captura manual; no hay seguimiento de vuelos en tiempo real.
- Recomendaciones climáticas basadas en datos y reglas locales; no son un pronóstico en línea.
- Para las pruebas, utilizar contraseñas de prueba: la autenticación actual almacena la contraseña en la base de datos local sin hash.

## Documentación

- [Esquema de base de datos](DATABASE_SCHEMA.md)
- [SQL de referencia](database_schema.sql)
- [Diagrama PlantUML](database_diagram.puml)

La fuente de verdad de la base de datos de la app son las entidades y migraciones de Room en `app/src/main/java/com/example/tripmanager/data/`; los documentos son material de referencia.
