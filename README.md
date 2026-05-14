# WeatherSnap

Search live weather for a city, capture a photo with a custom CameraX camera, compress it, attach notes, and save the report locally.

## Tech

Kotlin · Jetpack Compose · MVVM · ViewModel · StateFlow · Coroutines · Hilt · Navigation Compose · Retrofit + Gson + OkHttp logging · Room · CameraX · Material 3.

## Setup

1. Open the project in Android Studio (Ladybug or newer).
2. Let Gradle sync. The wrapper will be downloaded automatically on first sync.
3. Run on a device or emulator with Android 7.0 (API 24) or newer.

If you do not have Android Studio and want to generate the wrapper manually:

```
gradle wrapper --gradle-version 8.10.2
```

## API

Open-Meteo, no API key required.

- Geocoding: `https://geocoding-api.open-meteo.com/v1/search`
- Forecast: `https://api.open-meteo.com/v1/forecast`

## Module structure

```
app/
  data/        retrofit, room, repositories
  domain/      models the rest of the app uses
  ui/          screens, composables, viewmodels, theme
  di/          hilt modules
  navigation/  nav graph
```
