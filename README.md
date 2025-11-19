# Spotify Clone - Clean Architecture

A Spotify clone built with Kotlin Multiplatform (KMP) and Jetpack Compose Multiplatform, following Clean Architecture principles.

## Architecture

This project follows Clean Architecture with three main layers:

### Domain Layer
- **Models**: Core business entities (Track, Album, Artist, Playlist, etc.)
- **Repository Interfaces**: Define contracts for data access
- **Use Cases**: Business logic operations

### Data Layer
- **DTOs**: Data Transfer Objects for API responses
- **API Client**: Ktor-based HTTP client for Spotify Web API
- **Repository Implementations**: Concrete implementations of domain repositories
- **Network**: HTTP client factory and configuration

### Presentation Layer
- **ViewModels**: State management using StateFlow
- **UI Screens**: Compose UI for Home and Search
- **Components**: Reusable UI components (cards, lists, etc.)
- **Navigation**: Screen navigation logic
- **Theme**: Spotify-inspired dark theme

## Setup

### Prerequisites
- Android Studio or IntelliJ IDEA
- JDK 11 or higher
- Gradle 8.x

### Spotify API Access

To use this app, you need a Spotify access token. You can obtain one by:

1. **Client Credentials Flow** (for public data):
   - Register your app at [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
   - Get your Client ID and Client Secret
   - Use these to get an access token via the Client Credentials flow

2. **Authorization Code Flow** (for user-specific data):
   - Implement OAuth 2.0 flow
   - Redirect users to Spotify authorization
   - Exchange authorization code for access token

### Configuration

Update the access token in `App.kt`:

```kotlin
LaunchedEffect(Unit) {
    initKoin(accessToken = "YOUR_ACCESS_TOKEN_HERE")
}
```

Or implement a proper OAuth flow to get the token dynamically.

## Features

- ✅ Search for tracks, albums, artists, and playlists
- ✅ Browse new releases
- ✅ View featured playlists
- ✅ Clean Architecture structure
- ✅ Multiplatform support (Android, iOS, Desktop, Web)
- ✅ Material 3 UI with Spotify-inspired theme
- ✅ Dependency Injection with Koin

## Project Structure

```
composeApp/src/commonMain/kotlin/com/chachadev/spotifycmpclone/
├── domain/
│   ├── model/          # Domain entities
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic use cases
├── data/
│   ├── api/            # API client
│   ├── dto/            # Data Transfer Objects
│   ├── network/        # HTTP client setup
│   └── repository/     # Repository implementations
├── presentation/
│   ├── navigation/     # Navigation logic
│   ├── ui/
│   │   ├── component/  # Reusable UI components
│   │   ├── screen/     # Screen composables
│   │   └── theme/       # App theme
│   └── viewmodel/      # ViewModels
└── di/                 # Dependency Injection setup
```

## Dependencies

- **Ktor**: HTTP client for API calls
- **Kotlinx Serialization**: JSON serialization
- **Koin**: Dependency injection
- **Jetpack Compose Multiplatform**: UI framework
- **Material 3**: Material Design components
- **Coroutines**: Asynchronous programming

## Building

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
```bash
./gradlew :composeApp:iosSimulatorArm64Binaries
```

### Desktop
```bash
./gradlew :composeApp:runDistributable
```

### Web
```bash
./gradlew :composeApp:jsBrowserRun
```

## API Endpoints Used

- `GET /v1/search` - Search for tracks, albums, artists, playlists
- `GET /v1/tracks/{id}` - Get track details
- `GET /v1/albums/{id}` - Get album details
- `GET /v1/artists/{id}` - Get artist details
- `GET /v1/playlists/{id}` - Get playlist details
- `GET /v1/browse/new-releases` - Get new album releases
- `GET /v1/browse/featured-playlists` - Get featured playlists
- `GET /v1/artists/{id}/top-tracks` - Get artist's top tracks
- `GET /v1/albums/{id}/tracks` - Get album tracks
- `GET /v1/playlists/{id}/tracks` - Get playlist tracks

## Notes

- The app requires a valid Spotify access token to function
- Some endpoints may require specific scopes (e.g., user-specific data)
- Image loading is implemented per platform (Android, iOS, JVM, JS)
- Error handling is implemented at the repository level

## License

This is a clone project for educational purposes. Spotify and Spotify API are trademarks of Spotify AB.
# SpotifyCmpClone
