# LLM Chatbot Android Application

A fully functional Android chatbot application that integrates with an LLM (Large Language Model) backend to provide intelligent conversational experiences.

## Features

 **User Authentication**
- Login screen with username input
- Blue background with green "Go" button
- Session persistence via SharedPreferences
- Auto-login on app restart

 **Chat Interface**
- Clean, modern message UI
- User messages (right-aligned, green bubbles)
- LLM messages (left-aligned, blue bubbles)
- Circular avatars for both user and LLM
- Timestamps on each message

 **Persistent Chat History**
- SQLite database with Room ORM
- Automatic message storage
- Load previous conversations by username
- Clear conversation history if needed

 **LLM Integration**
- Easy integration with real LLM backends
- Retrofit for HTTP client
- JSON serialization with Gson

## Project Structure

```
LLMChatbot/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/llmchatbot/
│   │   │   ├── MainActivity.java (Entry point, handles auth routing)
│   │   │   ├── LoginActivity.java (Login screen)
│   │   │   ├── ChatActivity.java (Chat interface)
│   │   │   ├── adapter/
│   │   │   │   └── MessageAdapter.java (RecyclerView adapter)
│   │   │   ├── model/
│   │   │   │   └── Message.java (Message entity)
│   │   │   ├── database/
│   │   │   │   ├── AppDatabase.java (Room database)
│   │   │   │   └── MessageDao.java (Data access object)
│   │   │   ├── network/
│   │   │   │   ├── LLMApiService.java (Retrofit service)
│   │   │   │   ├── LLMRequest.java
│   │   │   │   ├── LLMResponse.java
│   │   │   │   └── MockLLMService.java (Demo service)
│   │   │   ├── repository/
│   │   │   │   └── MessageRepository.java (Data layer)
│   │   │   ├── viewmodel/
│   │   │   │   └── ChatViewModel.java (MVVM ViewModel)
│   │   │   └── util/
│   │   │       └── AvatarUtil.java (Avatar utilities)
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_login.xml
│   │   │   │   ├── activity_chat.xml
│   │   │   │   ├── item_message_user.xml
│   │   │   │   └── item_message_llm.xml
│   │   │   ├── drawable/
│   │   │   │   ├── ic_send.xml (Paper airplane send icon)
│   │   │   │   ├── message_bubble_bg_user.xml
│   │   │   │   ├── message_bubble_bg_llm.xml
│   │   │   │   └── avatar_circle_bg.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── xml/
│   │   │       ├── backup_rules.xml
│   │   │       └── data_extraction_rules.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── build.gradle.kts
├── gradlew & gradlew.bat
└── README.md
```

## Usage

### First Run
1. Launch the app
2. Enter a username on the login screen
3. Tap the green "Go" button
4. You'll be taken to the chat screen

### Chat
1. Type your message in the input field at the bottom
2. Tap the paper airplane send button (or press Enter)
3. The app will:
   - Save your message to the database
   - Send it to the LLM backend
   - Display both your message and the response in the chat
4. Messages are automatically saved with timestamps

## LLM Integration

### Integrating a Real LLM Backend

#### Ollama (Local/Self-Hosted)

1. **Install Ollama** from https://ollama.ai
2. **Pull a model**: `ollama pull llama2`
3. **Start Ollama**: `ollama serve` (default runs on http://localhost:11434)
4. **Update the base URL** in `MessageRepository.java`:

```java
private static final String LLM_BASE_URL = "http://192.168.1.100:11434/"; 
// Change to your server IP if not localhost
```

## Database Schema

### Messages Table

| Column | Type | Notes |
|--------|------|-------|
| id | INTEGER PRIMARY KEY | Auto-increment |
| content | TEXT | Message text |
| isFromUser | BOOLEAN | true = user, false = LLM |
| timestamp | LONG | Unix timestamp in milliseconds |
| username | TEXT | Associated user |

## Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture:

- **Model**: `Message` entity, database entities
- **View**: Activities, Layouts, Adapters
- **ViewModel**: `ChatViewModel` manages chat state and database operations
- **Repository**: `MessageRepository` abstracts data sources