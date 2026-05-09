# Lost & Found Mobile App

## Overview

The **Lost & Found** mobile application is an Android-based platform designed to help users connect lost items with their owners. The app enables users to post advertisements for lost or found items, search through listings by category, manage their posts efficiently, and use geo-location features to find items near their current location.

## Features

### Core Functionality
- **Post Lost/Found Items**: Users can easily post new items with title, description, category, status (Lost/Found), location, and photos
- **Browse Items**: Browse all posted items with a clean card-based interface
- **Search Functionality**: Full-text search across item titles and descriptions
- **Category Filtering**: Filter items by predefined categories (Electronics, Pets, Wallets, Keys, Documents, Clothing, Jewelry, Other)
- **Item Details**: View detailed information about each item including images, descriptions, posted date, location, and status
- **Image Support**: Upload and store images with each post for better item identification
- **Date/Time Tracking**: Automatic timestamps on all posts showing when items were posted and time ago format for recent posts
- **Delete Items**: Remove items from the listing once found or no longer needed

### Geo-Location Features (Task 7.1P)
- **Map View**: Display all lost and found items on an interactive map
- **Radius-Based Search**: Filter items within a specified radius (default 50km) from user's current location
- **Current Location Detection**: Get user's current location using GPS
- **Location Autocomplete**: Enter location using address autocomplete with reverse geocoding
- **Distance Calculation**: Calculate and display distance of items from user location
- **Marker Clustering**: Different colored markers for LOST (red) and FOUND (green) items
- **Search Radius Visualization**: Circle overlay showing the search radius on the map

### Technical Features
- **SQLite Database**: Local storage for all items and categories with geographic coordinates
- **Image Management**: Efficient image compression and storage
- **Responsive UI**: Intuitive user interface with RecyclerView for smooth scrolling
- **Category Management**: Pre-configured categories for quick filtering
- **Google Maps Integration**: Real-time map display with markers and radius circles
- **Location Services**: GPS-based location tracking and address geocoding
- **Permission Management**: Proper handling of location and camera permissions

## Project Structure

```
app/src/main/
├── java/com/example/lostandfound/
│   ├── MainActivity.java                 # Main activity with item listing and map button
│   ├── MapActivity.java                  # Activity for displaying items on map
│   ├── PostItemActivity.java            # Activity for posting new items
│   ├── ItemDetailActivity.java          # Activity for viewing item details
│   ├── adapter/
│   │   └── ItemAdapter.java             # RecyclerView adapter for items
│   ├── database/
│   │   └── LostandFoundDatabase.java    # SQLite database helper
│   ├── model/
│   │   ├── Item.java                    # Item data model with coordinates
│   │   └── Category.java                # Category data model
│   ├── repository/
│   │   ├── ItemRepository.java          # Data access object for items with radius search
│   │   └── CategoryRepository.java      # Data access object for categories
│   └── util/
│       ├── ImageManager.java            # Image handling utilities
│       ├── DateTimeUtil.java            # Date/time formatting utilities
│       └── LocationManager.java         # Location and geocoding utilities
├── res/
│   ├── layout/
│   │   ├── activity_main.xml            # Main activity layout with map button
│   │   ├── activity_map.xml             # Map activity layout
│   │   ├── activity_post_item.xml       # Post item activity layout
│   │   ├── activity_item_detail.xml     # Item detail activity layout
│   │   └── item_card.xml                # Item list card layout
│   ├── drawable/
│   │   ├── rounded_search_bg.xml        # Search bar background
│   │   ├── status_badge_lost.xml        # Lost status badge
│   │   └── status_badge_found.xml       # Found status badge
│   ├── values/
│   │   ├── strings.xml                  # String resources
│   │   ├── colors.xml                   # Color definitions
│   │   └── arrays.xml                   # Array resources
│   └── values-night/
│       └── themes.xml                   # Night mode theme
└── AndroidManifest.xml                  # Application manifest with permissions
```

## Database Schema

### Items Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER (PK) | Unique identifier |
| title | TEXT | Item title |
| description | TEXT | Item description |
| category_id | INTEGER (FK) | Reference to category |
| status | TEXT | LOST or FOUND |
| date_posted | TEXT | ISO timestamp |
| image_path | TEXT | File path to image |
| phone | TEXT | Contact phone number |
| location | TEXT | Address location |
| latitude | REAL | Geographic latitude coordinate |
| longitude | REAL | Geographic longitude coordinate |
| created_at | TIMESTAMP | Creation timestamp |

### Categories Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER (PK) | Unique identifier |
| name | TEXT | Category name |

**Pre-configured Categories:**
- Electronics
- Pets
- Wallets
- Keys
- Documents
- Clothing
- Jewelry
- Other

## Usage Guide

### Posting an Item

1. Tap the "Post Lost/Found Item" button on the main screen
2. Enter the item title and description
3. Select the appropriate category
4. Choose Lost or Found status
5. Enter or select the location (with autocomplete)
6. Upload an image (from gallery or camera)
7. Tap "Post Item" to submit

### Searching for Items

1. Use the search bar to find items by title or description
2. Use the category filter dropdown to narrow results by category
3. Tap on any item card to view full details

### Finding Items on Map

1. Tap "Show On Map" button on the main screen
2. The app will request location permission (if not already granted)
3. Your current location will be displayed with a blue marker
4. All items within the 50km radius will be displayed:
   - **Red markers** = Lost items
   - **Green markers** = Found items
5. A blue circle shows the search radius boundary
6. Tap on any marker to view item details

### Managing Items

1. Navigate to item details to view complete information
2. Use "Delete Item" to remove posted items
3. Items automatically display time-ago format (e.g., "2 hours ago")

## Technical Highlights

### Database Architecture
- Implemented using SQLiteOpenHelper for robust database management
- Uses Data Access Objects (DAO) pattern for clean separation of concerns
- Efficient queries with proper indexing on foreign keys and location columns
- Support for geographic coordinate storage and retrieval

### Geo-Location Features
- **Haversine Formula**: Implements distance calculation between coordinates
- **GPS Integration**: Uses Google Play Services for accurate location tracking
- **Geocoding**: Converts addresses to coordinates and vice versa using Android Geocoder
- **Radius Search**: SQL-based distance filtering for efficient queries
- **Map Visualization**: Google Maps API for real-time item display

### Image Handling
- Images are compressed to optimize storage space
- Stored in app's cache directory for security
- Thumbnail loading for efficient list rendering
- Automatic cleanup when items are deleted

### User Interface
- Material Design 3 components for modern appearance
- RecyclerView with smooth scrolling and efficient rendering
- Responsive layouts that adapt to different screen sizes
- Intuitive navigation between activities
- Map interface with interactive markers and zoom controls

### Date/Time Management
- Automatic timestamp generation using system clock
- Human-readable date formatting (e.g., "Mar 27, 2026 14:30")
- Time-ago display for recent posts (e.g., "2 hours ago")
- Proper handling of timezone considerations

## Permissions Required

- **CAMERA**: For taking photos of items
- **READ_EXTERNAL_STORAGE**: For selecting images from gallery
- **WRITE_EXTERNAL_STORAGE**: For saving captured images
- **ACCESS_FINE_LOCATION**: For precise GPS location tracking
- **ACCESS_COARSE_LOCATION**: For network-based location tracking
- **INTERNET**: For Google Maps and location services





