# Personal Event Planner App

A simple yet functional Android application that allows users to manage their upcoming events, trips, and appointments with ease. Built with modern Android development practices including Room Database for persistence, Jetpack Navigation for seamless navigation, and Material Design for a polished user interface.

## Features

### 1. **Create Events**
- Users can add new events with the following details:
  - **Title**: Event name (mandatory)
  - **Category**: Select from predefined categories (Work, Social, Travel, Personal, Health)
  - **Location**: Event location (mandatory)
  - **Date & Time**: Pick specific date and time using Android's DatePickerDialog and TimePickerDialog
- Validation ensures dates are not in the past

### 2. **Read Events**
- Dashboard displays all upcoming events in a RecyclerView
- Events are automatically sorted by date and time (ascending order)
- Clean card-based UI with event details displayed clearly
- Shows: Title, Category, Location, and Date/Time

### 3. **Update Events**
- Users can tap on any event to open the edit screen
- Modify any event details including title, category, location, and date/time
- Changes are persisted to the database immediately
- Confirmation via Snackbar notification

### 4. **Delete Events**
- Quick delete button on each event card in the event list
- Dedicated delete button in the edit event screen
- Confirmation via Snackbar notification

## Technical Details

### Architecture
The app follows the **MVVM (Model-View-ViewModel)** architecture pattern for clean separation of concerns:

- **Model**: Event entity with Room annotations
- **View**: Fragments for UI presentation
- **ViewModel**: EventViewModel manages UI-related data and lifecycle-aware operations
- **Repository**: EventRepository serves as a data abstraction layer

### Technology Stack

#### Core Libraries
- **Android Framework**: API Level 23+ (minimum), Target 36
- **Jetpack Components**:
  - **Room**: Local SQLite database with type safety
  - **Navigation Component**: Fragment-based navigation with bottom navigation
  - **Lifecycle**: ViewModel and LiveData for lifecycle awareness
  - **Fragment**: Modern fragment management

#### Material Design
- **Material Components**: Material Design 3 implementation
- **ConstraintLayout**: Responsive layouts
- **CardView**: Enhanced card UI for events
- **BottomNavigationView**: Easy navigation between screens

#### Build Tools
- **Gradle**: Modern Gradle build system with version catalog
- **Kotlin Plugin**: Kapt for annotation processing

### Project Structure

```
PersonalEventPlanner/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/personaleventplanner/
│   │   │   │   ├── adapter/
│   │   │   │   │   └── EventAdapter.java          # RecyclerView adapter for events
│   │   │   │   ├── database/
│   │   │   │   │   ├── EventDao.java              # Data Access Object (CRUD operations)
│   │   │   │   │   └── EventDatabase.java         # Room database instance
│   │   │   │   ├── model/
│   │   │   │   │   └── Event.java                 # Event entity with Room annotations
│   │   │   │   ├── repository/
│   │   │   │   │   └── EventRepository.java       # Data repository pattern
│   │   │   │   ├── ui/
│   │   │   │   │   ├── EventListFragment.java     # Displays all events
│   │   │   │   │   ├── AddEventFragment.java      # Create new event form
│   │   │   │   │   └── EditEventFragment.java     # Edit or delete event
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── EventViewModel.java        # ViewModel for UI data
│   │   │   │   └── MainActivity.java              # Main activity with navigation setup
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       │   ├── activity_main.xml          # Main activity layout with nav host
│   │   │       │   ├── fragment_event_list.xml    # Event list screen
│   │   │       │   ├── fragment_add_event.xml     # Add event form
│   │   │       │   ├── fragment_edit_event.xml    # Edit event form
│   │   │       │   └── item_event.xml             # Single event card layout
│   │   │       ├── navigation/
│   │   │       │   └── nav_graph.xml              # Navigation graph for Jetpack Navigation
│   │   │       ├── menu/
│   │   │       │   └── bottom_nav_menu.xml        # Bottom navigation menu items
│   │   │       ├── drawable/
│   │   │       │   └── edit_text_background.xml   # EditText styling
│   │   │       ├── values/
│   │   │       │   ├── colors.xml                 # Color definitions
│   │   │       │   ├── strings.xml                # String resources
│   │   │       │   └── styles.xml                 # Theme and style definitions
│   │   │       └── AndroidManifest.xml
│   │   ├── test/                                  # Unit tests
│   │   └── androidTest/                           # Instrumented tests
│   ├── build.gradle.kts                          # App-level build configuration
│   └── proguard-rules.pro                        # ProGuard rules
├── gradle/
│   ├── libs.versions.toml                        # Centralized dependency management
│   └── wrapper/
├── build.gradle.kts                             # Project-level build configuration
├── settings.gradle.kts                          # Gradle settings
└── README.md                                    # This file
```

### Database Schema

#### Events Table
```
Table: events

Columns:
- id (INTEGER, PRIMARY KEY, AUTO INCREMENT)  - Unique event identifier
- title (TEXT)                                - Event name
- category (TEXT)                             - Category (Work, Social, Travel, etc.)
- location (TEXT)                             - Event location
- dateTime (INTEGER)                          - Event date and time in milliseconds
- createdAt (INTEGER)                         - Timestamp when event was created
```

## Installation & Setup

- Step 1: Clone the Repository
- Step 2: Open in Android Studio
- Step 3: Sync Gradle Files
- Step 4: Configure Local SDK Path (if needed)
- Step 5: Run the Application
- Step 6: Verify Installation

## Usage Guide

### Adding a New Event

1. **Launch the App**: Tap the Personal Event Planner icon
2. **Navigate to Add Event**: Tap "Add Event" in the bottom navigation
3. **Fill in Details**:
   - Enter the event title (required)
   - Select a category from the dropdown
   - Enter the location (required)
   - Tap the date/time field to select date and time
4. **Validate**: Ensure date is not in the past
5. **Save**: Tap "Save" button
6. **Confirmation**: See success notification and return to event list

### Viewing All Events

1. **Navigate to Events**: Tap "Events" in the bottom navigation
2. **View List**: All upcoming events appear sorted by date
3. **Event Details**: Each card shows:
   - Event title
   - Category
   - Location
   - Date and time

### Editing an Event

1. **View Events**: Go to "Events" screen
2. **Select Event**: Tap on any event card
3. **Edit Screen Opens**: Modify any field
4. **Update**: Tap "Update" button
5. **Confirmation**: Changes saved immediately

### Deleting an Event

**Option 1: From Event List**
1. Go to "Events" screen
2. Tap "Delete" button on the event card
3. See confirmation notification

**Option 2: From Edit Screen**
1. Tap on an event to edit it
2. Tap "Delete Event" button at the bottom
3. Event removed immediately

## Input Validation & Error Handling

### Validation Rules

1. **Title Validation**
   - Cannot be empty
   - Shows error: "Please enter event title"

2. **Location Validation**
   - Cannot be empty
   - Shows error: "Please enter event location"

3. **Date Validation**
   - Cannot be in the past
   - Shows error: "Event date cannot be in the past"
   - Compares selected date with current system time

### User Feedback

- **Success Messages**: Green Snackbar with checkmark
  - "Event saved successfully"
  - "Event updated successfully"
  - "Event deleted successfully"

- **Error Messages**: Red Snackbar with error details
  - Validation errors appear immediately
  - User can fix and retry

## Development Guidelines

### Adding a New Feature

1. **Create Model**: Add new entity if needed in `model/` package
2. **Create DAO**: Add database queries in `database/` package
3. **Update Repository**: Add methods to access new data in `repository/` package
4. **Create ViewModel**: Add LiveData and methods in `viewmodel/` package
5. **Create UI**: Add Fragment in `ui/` package
6. **Update Navigation**: Add route in `navigation/nav_graph.xml`
7. **Create Layout**: Add XML layout in `res/layout/`

### Code Style

- Follow Google Android Code Style Guide
- Use meaningful variable names
- Add comments for complex logic
- Keep methods small and focused (single responsibility)

### Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Generate coverage report
./gradlew jacocoTestReport
```

## Troubleshooting

### Build Issues

**Problem**: Gradle sync fails
```
Solution:
1. Go to File → Invalidate Caches → Invalidate and Restart
2. Delete .gradle and .idea folders
3. Sync again
```

**Problem**: Missing RecyclerView dependency
```
Solution:
1. Open build.gradle.kts
2. Add: implementation("androidx.recyclerview:recyclerview:1.3.0")
```

### Runtime Issues

**Problem**: App crashes on startup
```
Solution:
1. Check logcat (View → Tool Windows → Logcat)
2. Look for exception messages
3. Verify AndroidManifest.xml is correct
4. Check that all fragments are declared in nav_graph.xml
```

**Problem**: Events not saving
```
Solution:
1. Verify Room database is initialized
2. Check that database thread runs on background
3. Verify EventDao methods are correct
4. Check database file exists in device storage
```

**Problem**: Navigation not working
```
Solution:
1. Verify NavHostFragment is in activity_main.xml
2. Check nav_graph.xml has correct fragment IDs
3. Verify action IDs match between code and XML
4. Check BottomNavigationView menu items match nav destinations
```

## Performance Optimization

- **Database Queries**: Used LiveData for automatic UI updates
- **Threading**: Database operations run on background threads
- **Memory**: RecyclerView uses view recycling for efficient list rendering
- **UI Responsiveness**: ViewModel preserves data during configuration changes

## Security Considerations

- Data stored locally in SQLite (encrypted storage recommended for sensitive apps)
- No external API calls (data never leaves device)
- Input validation prevents SQL injection
- Proper permission model for Android 6.0+

## Future Enhancements

Potential features for future versions:
- Event reminders/notifications
- Event categories with custom colors
- Search and filter functionality
- Event recurrence (repeat events)
- Export events as CSV/PDF
- Cloud backup integration
- Dark theme support
- Multi-language support
- Event attachments (photos, documents)
- Event location maps integration
- Event sharing with other users

## License

This project is created for educational purposes as part of SIT708 (Mobile Application Development).

## Contact & Support

For questions or issues, please contact the development team or open an issue in the repository.

---

**Version**: 1.0  
**Last Updated**: March 2026  
**Minimum SDK**: Android 6.0 (API 23)  
**Target SDK**: Android 15 (API 36)

