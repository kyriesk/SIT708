
# iStream

iStream is an Android application for personal YouTube playlist management with secure local user authentication. It allows users to sign up, log in, add YouTube videos to their personal playlist, and play them directly within the app.

## Features

- **User Authentication:**
  - Sign up and login with credentials stored securely using Room database.
  - Session management with auto-login for returning users.

- **Home Screen:**
  - Input field for YouTube video URLs.
  - Play button to load and play videos using the YouTube iFrame Player API (embedded in a WebView).
  - Add to Playlist button to save the current video URL to the logged-in user's playlist.
  - My Playlist button to view and manage saved videos.
  - Logout button to end the session and return to the login screen.

- **Playlist Screen:**
  - Displays all saved video URLs for the current user only.
  - Each URL is clickable and loads the video in the Home screen player.
  - User-specific playlists (no cross-user access).

- **Error Handling:**
  - Invalid YouTube URLs are detected and handled gracefully with user-friendly error messages.

## Usage

1. **Sign Up:**
   - Enter your full name, choose a username, and set a password.
   - Confirm your password and create your account.

2. **Login:**
   - Enter your username and password to log in.

3. **Home Screen:**
   - Paste a YouTube video URL and tap Play to watch it.
   - Tap Add to Playlist to save the video for later.
   - Tap My Playlist to view your saved videos.
   - Tap Logout to end your session.

4. **Playlist Screen:**
   - Tap any video URL to play it on the Home screen.
   - Only your own videos are visible.

## Architecture

### Data Layer
- **Room Database:**
  - Stores user credentials and playlists locally.
  - Entities: `User`, `PlaylistItem` (each item links a user to a YouTube URL).
  - DAOs handle all database operations (insert, query, validate, etc.).

### Session Management
- **SharedPreferences:**
  - Manages user session state (logged-in user, auto-login, logout).

### UI Layer
- **Activities:**
  - `LoginActivity`: Handles user login and navigation to Home or Sign Up.
  - `SignupActivity`: Handles new user registration and validation.
  - `HomeActivity`: Main screen for video input, playback, and playlist actions.
  - `PlaylistActivity`: Displays the user's playlist and handles video selection.

- **Adapters & Utilities:**
  - Playlist adapter for displaying video URLs.
  - Utility functions for parsing and validating YouTube URLs.

### Video Playback
- **YouTube iFrame Player API:**
  - Embedded in a WebView for seamless video playback.
  - Only valid YouTube URLs are accepted; invalid URLs prompt an error.

## Project Structure

- `app/src/main/java/com/example/istream/data` - Room entities, DAOs, and database.
- `app/src/main/java/com/example/istream/session` - SharedPreferences session manager.
- `app/src/main/java/com/example/istream/ui/auth` - `LoginActivity`, `SignupActivity`.
- `app/src/main/java/com/example/istream/ui/home` - `HomeActivity` player and actions.
- `app/src/main/java/com/example/istream/ui/playlist` - Playlist list screen and adapter.
- `app/src/main/java/com/example/istream/util` - YouTube URL parsing helpers.

