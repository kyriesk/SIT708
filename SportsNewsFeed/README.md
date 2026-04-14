# Sports News Feed App 

## Project Overview
A fully functional Android Sports News Feed App using **Single Activity Architecture** with Fragment-based navigation. The app features hardcoded dummy data for sports news and featured matches with full bookmark functionality.

## Key Features

**Single Activity Architecture** - One MainActivity with Fragment navigation
**RecyclerView Integration** - Horizontal and vertical layouts with adapters
**Search & Filtering** - Real-time category filtering
**Local Bookmarks** - Persistent storage with SharedPreferences + Gson
**Smooth Navigation** - Fragment transitions with animations
**Responsive Layout** - ConstraintLayout and NestedScrollView
**Hardcoded Dummy Data** - Multiple news items across 3 categories
**Material Design** - Material icons and components
**Empty States** - Proper handling of empty bookmark list

## Architecture

### Single Activity + Fragments
- **MainActivity**: Contains NavHostFragment and BottomNavigationView
- **HomeFragment**: Main feed with search and two RecyclerViews
- **DetailFragment**: Full article view with related stories
- **BookmarkFragment**: Saved articles management

### Data Models
```
SportCategory (Enum)
├── FOOTBALL
├── BASKETBALL
├── CRICKET
└── ALL

SportsNews (Serializable)
├── id: int
├── title: String
├── description: String
├── imageResId: String
├── category: SportCategory
├── author: String
└── publishDate: String

FeaturedMatch (Serializable)
├── id: int
├── team1: String
├── team2: String
├── matchTime: String
├── imageResId: String
└── category: SportCategory
```

### Adapters
- **FeaturedMatchesAdapter**: Horizontal RecyclerView for featured matches
- **SportsNewsAdapter**: Vertical RecyclerView for news feed
- **RelatedStoriesAdapter**: Related articles in detail view
- **BookmarkedStoriesAdapter**: Bookmarks management with remove button

### Local Storage
- **BookmarkRepository**: Manages SharedPreferences operations
  - `addBookmark(news)`: Save news to bookmarks
  - `removeBookmark(newsId)`: Remove from bookmarks
  - `isBookmarked(newsId)`: Check bookmark status
  - `getAllBookmarks()`: Retrieve all saved stories
  - JSON serialization using Gson






