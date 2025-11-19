# Setup Guide

## Quick Start

### 1. Get Spotify Access Token

You need a Spotify access token to use the API. Here are two options:

#### Option A: Client Credentials Flow (Quick Test)

This is the simplest way to get started for testing:

1. Go to [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Create a new app
3. Note your **Client ID** and **Client Secret**
4. Use curl or Postman to get an access token:

```bash
curl -X POST "https://accounts.spotify.com/api/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET"
```

5. Copy the `access_token` from the response
6. Update `App.kt`:

```kotlin
initKoin(accessToken = "YOUR_ACCESS_TOKEN_HERE")
```

#### Option B: OAuth 2.0 Flow (Production)

For production apps, implement the full OAuth 2.0 Authorization Code flow:

1. Register your app in Spotify Developer Dashboard
2. Set redirect URI (e.g., `myapp://callback`)
3. Implement OAuth flow in your app
4. Exchange authorization code for access token
5. Store and refresh tokens securely

### 2. Update Access Token

In `composeApp/src/commonMain/kotlin/com/chachadev/spotifycmpclone/App.kt`:

```kotlin
remember { 
    try {
        org.koin.core.context.GlobalContext.get()
    } catch (e: Exception) {
        initKoin(accessToken = "YOUR_TOKEN_HERE")  // Replace with your token
    }
}
```

### 3. Build and Run

#### Android
```bash
./gradlew :composeApp:assembleDebug
```

#### iOS
```bash
./gradlew :composeApp:iosSimulatorArm64Binaries
```

#### Desktop
```bash
./gradlew :composeApp:runDistributable
```

#### Web
```bash
./gradlew :composeApp:jsBrowserRun
```

## Troubleshooting

### "401 Unauthorized" Error
- Your access token has expired (Client Credentials tokens expire after 1 hour)
- Get a new token and update it in the code
- For production, implement token refresh logic

### "403 Forbidden" Error
- Some endpoints require specific scopes
- Check Spotify API documentation for required scopes
- Use Authorization Code flow instead of Client Credentials

### Images Not Loading
- Check network permissions (Android/iOS)
- Verify image URLs are valid
- Some platforms may need additional configuration

## Next Steps

1. **Implement OAuth Flow**: Replace hardcoded token with proper OAuth
2. **Add Token Refresh**: Implement automatic token refresh
3. **Add Caching**: Cache API responses for better performance
4. **Add Offline Support**: Store data locally for offline access
5. **Add Player**: Implement music playback (requires Spotify Premium SDK)

## API Rate Limits

Spotify API has rate limits:
- **Rate Limit**: 10 requests per second per access token
- **Burst Limit**: Higher burst capacity for short periods

Implement retry logic with exponential backoff for production.

