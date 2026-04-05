# Personal Event Planner - Implementation Guide

## Quick Start Guide

This guide walks you through the complete implementation of the Personal Event Planner Android app.

## Project Structure Overview

```
app/src/main/
├── java/com/example/personaleventplanner/
│   ├── MainActivity.java                    ← Main entry point
│   ├── adapter/
│   │   └── EventAdapter.java               ← RecyclerView adapter
│   ├── database/
│   │   ├── EventDao.java                   ← Database queries
│   │   └── EventDatabase.java              ← Room database setup
│   ├── model/
│   │   └── Event.java                      ← Data model
│   ├── repository/
│   │   └── EventRepository.java            ← Data access layer
│   ├── ui/
│   │   ├── AddEventFragment.java           ← Add event UI
│   │   ├── EditEventFragment.java          ← Edit event UI
│   │   └── EventListFragment.java          ← List events UI
│   └── viewmodel/
│       └── EventViewModel.java             ← ViewModel layer
└── res/
    ├── layout/
    │   ├── activity_main.xml               ← Main activity layout
    │   ├── fragment_add_event.xml
    │   ├── fragment_edit_event.xml
    │   ├── fragment_event_list.xml
    │   └── item_event.xml                  ← Event card item
    ├── menu/
    │   └── bottom_nav_menu.xml             ← Bottom navigation items
    ├── navigation/
    │   └── nav_graph.xml                   ← Navigation routes
    ├── drawable/
    │   └── edit_text_background.xml        ← Custom drawable
    └── values/
        ├── colors.xml
        ├── strings.xml
        └── styles.xml
```

## File Descriptions

### Core Architecture Files

#### 1. **model/Event.java**
- Represents a single event entity
- Uses @Entity annotation for Room database
- Fields: id, title, category, location, dateTime, createdAt
- Includes getters/setters for all fields

#### 2. **database/EventDao.java**
- Data Access Object (DAO) interface
- Defines CRUD operations using annotations
- Methods: insertEvent, updateEvent, deleteEvent, getAllEvents, getEventById
- Returns LiveData for reactive updates

#### 3. **database/EventDatabase.java**
- Abstract class extending RoomDatabase
- Singleton pattern for database instance
- Creates/returns EventDao
- Ensures single database connection

#### 4. **repository/EventRepository.java**
- Abstraction layer between database and UI
- Handles all database operations on background threads
- Exposes LiveData streams for UI observation
- Prevents tight coupling between UI and database

#### 5. **viewmodel/EventViewModel.java**
- Extends AndroidViewModel for app context access
- Manages UI-related data
- Survives configuration changes (rotation, etc.)
- Communicates with repository
- Exposes LiveData to UI fragments

### UI Layer Files

#### 6. **ui/EventListFragment.java**
- Displays all events in a RecyclerView
- Observes LiveData from ViewModel
- Implements click listeners for edit/delete
- Navigates to EditEventFragment on event click
- Shows Snackbar on deletion

#### 7. **ui/AddEventFragment.java**
- Form for creating new events
- Input fields: title, category, location, date/time
- Date/time picker integration
- Form validation:
  - Title not empty
  - Location not empty
  - Date not in past
- Inserts event into database via ViewModel

#### 8. **ui/EditEventFragment.java**
- Form for editing existing events
- Loads event data from database
- Pre-fills all fields
- Allows modification of any field
- Delete button for removing event
- Validation same as AddEventFragment
- Updates database on save

### Adapter and Navigation

#### 9. **adapter/EventAdapter.java**
- RecyclerView adapter for event list
- Binds event data to card views
- Handles click events (edit and delete)
- Formats date/time for display
- Implements OnEventClickListener interface

#### 10. **MainActivity.java**
- Only Activity in the application
- Sets up NavHostFragment for navigation
- Configures BottomNavigationView
- Uses NavigationUI for automatic routing
- Handles fragment transactions

### Resource Files

#### 11. **layout/activity_main.xml**
- Contains NavHostFragment (fragment container)
- Contains BottomNavigationView for navigation
- Linear layout structure for organization

#### 12. **layout/fragment_event_list.xml**
- Title and RecyclerView
- RecyclerView displays events

#### 13. **layout/fragment_add_event.xml**
- Form fields: title, category, location, date/time
- Cancel and Save buttons
- ScrollView for vertical scrolling

#### 14. **layout/fragment_edit_event.xml**
- Same form fields as add_event
- Cancel and Update buttons
- Additional Delete Event button

#### 15. **layout/item_event.xml**
- CardView for event presentation
- Text views for all event details
- Delete button for quick deletion

#### 16. **navigation/nav_graph.xml**
- Defines all fragments
- Specifies navigation actions
- Maps bottom nav items to destinations
- Passes arguments between fragments

#### 17. **menu/bottom_nav_menu.xml**
- Two menu items: Events and Add Event
- IDs must match fragment IDs for automatic routing

## Data Flow Architecture

### Creating an Event (Adding)

```
User Input (AddEventFragment)
    ↓
Form Validation (check title, location, date)
    ↓
ViewModel.insertEvent(event)
    ↓
Repository.insertEvent(event) [Background Thread]
    ↓
EventDao.insertEvent(event)
    ↓
Room Database writes to SQLite
    ↓
Snackbar confirms "Event saved successfully"
    ↓
Navigate back to EventListFragment
    ↓
LiveData update triggers EventListFragment refresh
```

### Reading Events (Listing)

```
EventListFragment created
    ↓
ViewModel.getAllEvents() [Returns LiveData<List<Event>>]
    ↓
Repository returns LiveData from EventDao.getAllEvents()
    ↓
Database query: "SELECT * FROM events ORDER BY dateTime ASC"
    ↓
LiveData emits updated list
    ↓
EventListFragment.observe() receives list
    ↓
EventAdapter updates with new data
    ↓
RecyclerView renders event cards
```

### Updating an Event (Editing)

```
User clicks event card (EventListFragment)
    ↓
Navigate to EditEventFragment with eventId
    ↓
EditEventFragment loads event via ViewModel
    ↓
User modifies fields and clicks Update
    ↓
Form Validation
    ↓
ViewModel.updateEvent(event)
    ↓
Repository.updateEvent(event) [Background Thread]
    ↓
EventDao.updateEvent(event)
    ↓
Room Database updates SQLite record
    ↓
Snackbar confirms "Event updated successfully"
    ↓
Navigate back to EventListFragment
    ↓
LiveData update triggers refresh
```

### Deleting an Event

```
User clicks Delete (EventListFragment or EditEventFragment)
    ↓
ViewModel.deleteEvent(event)
    ↓
Repository.deleteEvent(event) [Background Thread]
    ↓
EventDao.deleteEvent(event)
    ↓
Room Database deletes record from SQLite
    ↓
Snackbar confirms "Event deleted successfully"
    ↓
If in EditEventFragment, navigate back to EventListFragment
    ↓
LiveData update triggers refresh
```

## Key Design Patterns Used

### 1. **MVVM (Model-View-ViewModel)**
- Clean separation of concerns
- UI state survives configuration changes
- Automatic observer pattern with LiveData

### 2. **Repository Pattern**
- Abstracts data sources
- Centralizes data access logic
- Enables easy switching between local/remote data

### 3. **LiveData Pattern**
- Lifecycle-aware observable data
- Automatic UI updates on data change
- No memory leaks from retained fragments

### 4. **Singleton Pattern**
- EventDatabase ensures single database instance
- Prevents multiple database connections

### 5. **Observer Pattern**
- Fragments observe LiveData
- Changes automatically trigger UI updates
- Reduces coupling between components

## Technology Stack Justification

### Android Jetpack Components
- **Navigation**: Eliminates manual fragment transaction code
- **Room**: Type-safe database abstraction, prevents SQL injection
- **ViewModel**: Survives configuration changes, clean architecture
- **LiveData**: Lifecycle-aware, no manual subscription management
- **Fragment**: Lightweight UI components, reusable

### Material Design
- Modern, familiar user interface
- Built-in accessibility features
- Consistent with Android design guidelines

### Threading
- Database operations on background threads prevent ANR
- ViewModel handles lifecycle properly
- No explicit Thread management (handled by Room/Repository)

## How to Build and Run

### Prerequisites
1. Android Studio 2024.1+
2. Android SDK 23+ installed
3. Gradle properly configured
4. Java 11+ installed

### Build Steps
1. **Clone/Open Project**
   - File → Open → Select PersonalEventPlanner folder

2. **Sync Gradle**
   - File → Sync Now (or Ctrl+Shift+O)
   - Wait for build to complete

3. **Choose Run Target**
   - Virtual device or physical device
   - Device must have Android 6.0+

4. **Run App**
   - Run → Run 'app' (or Shift+F10)
   - App installs and launches on target device

### Testing the App

**Test Case 1: Add Event**
1. Tap "Add Event"
2. Fill in all fields
3. Select future date/time
4. Tap Save
5. Verify: Event appears in list, sorted by date

**Test Case 2: View Events**
1. Check event list
2. Verify: Events sorted by date ascending
3. Verify: All details displayed correctly

**Test Case 3: Edit Event**
1. Tap any event
2. Modify some fields
3. Tap Update
4. Verify: Changes reflected in list

**Test Case 4: Delete Event**
1. Option A: Tap Delete on event card
2. Option B: Edit event, tap Delete Event
3. Verify: Event removed from list

**Test Case 5: Validation**
1. Try adding event without title → Error shown
2. Try adding event without location → Error shown
3. Try selecting past date → Error shown

## Troubleshooting Common Issues

### Issue 1: Gradle Build Fails
**Error**: "Failed to find dependency"
```
Solution:
1. File → Invalidate Caches → Invalidate and Restart
2. Delete .gradle folder
3. Sync again
```

### Issue 2: RecyclerView Not Showing
**Error**: Empty list even though data exists
```
Solution:
1. Verify EventAdapter.setEvents() called
2. Check LiveData is being observed
3. Verify layout_manager set to RecyclerView
4. Check item_event.xml layout is valid
```

### Issue 3: Navigation Not Working
**Error**: Fragments not switching on bottom nav click
```
Solution:
1. Verify nav_graph.xml has correct fragment IDs
2. Check bottom_nav_menu.xml item IDs match nav destinations
3. Verify MainActivity setup NavigationUI.setupWithNavController
4. Check action IDs in nav_graph.xml
```

### Issue 4: Database Not Persisting
**Error**: Events disappear after app close
```
Solution:
1. Verify Room database is properly initialized
2. Check @Entity annotation on Event class
3. Verify @Database annotation on EventDatabase
4. Check EventDao methods have proper @Insert/@Update/@Delete
5. Ensure database operations complete before close
```

### Issue 5: App Crashes on Fragment Creation
**Error**: ClassNotFoundException for fragments
```
Solution:
1. Verify fragment class names match nav_graph.xml
2. Check package name is correct in navigation routes
3. Verify fragments extend androidx.fragment.app.Fragment
4. Check onCreateView returns valid layout
```

## Performance Considerations

### Database Optimization
- Room automatically creates indexes on primary keys
- Queries return LiveData for efficient updates
- Background threads prevent UI blocking

### UI Optimization
- RecyclerView recycles item views
- Fragment transitions are animated smoothly
- Only visible fragment is loaded

### Memory Management
- ViewModel persists across config changes
- No retained fragments to cause leaks
- LiveData is lifecycle-aware

## Future Enhancement Ideas

1. **Notifications**
   - Schedule AlarmManager for event reminders
   - Show notification at event time

2. **Categories**
   - Custom colors for categories
   - Filter by category

3. **Search**
   - Search events by title
   - Filter by date range

4. **Export**
   - Export events as CSV
   - Share events with other apps

5. **Recurring Events**
   - Repeat daily/weekly/monthly
   - Modify series or single event

6. **Maps Integration**
   - Show event location on map
   - Get directions from current location

7. **Cloud Sync**
   - Backup to Firebase
   - Sync across devices

8. **Dark Mode**
   - System theme support
   - Custom dark colors

## Code Quality Standards

### Naming Conventions
- Classes: PascalCase (EventAdapter)
- Methods: camelCase (saveEvent)
- Constants: UPPER_CASE (EVENT_TABLE)
- Variables: camelCase (eventTitle)

### Documentation
- Add Javadoc for public methods
- Use inline comments for complex logic
- Keep comments up-to-date

### Testing
- Write unit tests for ViewModel
- Write integration tests for database
- Test edge cases (empty lists, past dates)

## Submitting the Assignment

Required Files:
- ✅ Source code in app/src/main/java/
- ✅ Layout files in app/src/main/res/layout/
- ✅ Navigation graph in app/src/main/res/navigation/
- ✅ Database implementation (Room)
- ✅ README.md with full documentation
- ✅ Gradle files with all dependencies

Submission Checklist:
- [ ] All CRUD operations working
- [ ] Data persists after app close
- [ ] Navigation working smoothly
- [ ] Validation preventing invalid input
- [ ] No crashes or ANRs
- [ ] Clean code style maintained
- [ ] README is comprehensive
- [ ] Project builds without errors
- [ ] Runs on Android 6.0+ devices
- [ ] All features tested manually

---

**Last Updated**: March 2026  
**Version**: 1.0  
**Status**: Production Ready

