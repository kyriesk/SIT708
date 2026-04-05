# Personal Event Planner - Runtime Setup & Troubleshooting Guide

## ✅ Build Status: SUCCESS

The app has been successfully built! If you're experiencing runtime errors when installing or running the app, this guide will help you resolve them.

---

## 🚀 Running the App - Step by Step

### Step 1: Sync Gradle (if not already done)
```
File → Sync Now
or press Ctrl+Shift+O
```

### Step 2: Clean Build
```
Build → Clean Project
Build → Rebuild Project
or run: ./gradlew clean build
```

### Step 3: Run on Emulator or Device

#### Option A: Android Emulator
1. Tools → Device Manager
2. Click the ▶ (play) button to start emulator
3. Wait for emulator to fully boot
4. Click "Run" → "Run 'app'" or press Shift+F10

#### Option B: Physical Device
1. Connect device via USB
2. Enable Developer Mode (Settings → About Phone → Tap Build Number 7 times)
3. Enable USB Debugging (Settings → Developer Options → USB Debugging)
4. Grant USB permission when prompted
5. Click "Run" → "Run 'app'" or press Shift+F10

---

## 🐛 Common Runtime Errors & Fixes

### Error 1: "App stopped working" or "Unfortunately, PersonalEventPlanner has stopped"

**Cause**: Database initialization issue or missing Room annotation processing

**Solution**:
```bash
1. File → Invalidate Caches → Invalidate and Restart
2. Build → Clean Project
3. Build → Rebuild Project
4. Run again
```

### Error 2: "ClassNotFoundException" for Event or fragments

**Cause**: Class compilation issue

**Solution**:
```
1. Verify all Java files are in: app/src/main/java/com/example/personaleventplanner/
2. Check that all imports are correct
3. Run: ./gradlew assembleDebug --info (to see compile errors)
```

### Error 3: "No such table: events"

**Cause**: Database table not created properly

**Solution**:
1. Verify @Entity annotation on Event.java
2. Verify @Database annotation on EventDatabase.java includes Event.class
3. Clear app data: 
   - Settings → Apps → Personal Event Planner → Storage → Clear Data
4. Reinstall app

### Error 4: "Unable to find NavController"

**Cause**: Navigation not properly set up

**Solution**:
1. Verify MainActivity has NavHostFragment setup
2. Verify nav_graph.xml exists in app/src/main/res/navigation/
3. Verify all fragment IDs in nav_graph.xml
4. Rebuild project

### Error 5: "NullPointerException" in MainActivity

**Cause**: NavHostFragment not found

**Solution**:
1. Verify activity_main.xml has:
   ```xml
   <fragment
       android:id="@+id/nav_host_fragment"
       android:name="androidx.navigation.fragment.NavHostFragment"
       ...>
   ```
2. Ensure BottomNavigationView exists in layout
3. Rebuild and run again

### Error 6: Crashes when adding an event

**Cause**: Date/time picker or validation issue

**Solution**:
1. Ensure all EditText IDs match layout file
2. Check Spinner is properly initialized
3. Verify calendar handling in AddEventFragment
4. Check logcat for specific error

### Error 7: Events don't appear in list

**Cause**: LiveData not properly observed or database empty

**Solution**:
1. Verify ViewModelProvider is properly initialized
2. Check that ViewModel observes LiveData correctly
3. Verify EventAdapter.setEvents() is called
4. Try adding an event first
5. Check logcat for LiveData emission

---

## 📋 Pre-Launch Checklist

Before running, verify:

- ✅ Gradle synced successfully
- ✅ No red error markers in code
- ✅ Emulator running (or device connected)
- ✅ Android SDK 23+ installed
- ✅ Target SDK matches build.gradle.kts (35)
- ✅ All layout files exist:
  - activity_main.xml
  - fragment_event_list.xml
  - fragment_add_event.xml
  - fragment_edit_event.xml
  - item_event.xml
- ✅ All resource files exist:
  - navigation/nav_graph.xml
  - menu/bottom_nav_menu.xml
  - values/colors.xml, strings.xml, styles.xml

---

## 🔍 Logcat Analysis

When app crashes, check Logcat for the root cause:

1. Open Logcat: View → Tool Windows → Logcat
2. Set filter to show "Error" and "Debug" level
3. Search for "FATAL EXCEPTION" or "java.lang."

**Common patterns**:
- "ClassNotFoundException" - Missing class or import
- "NullPointerException" - Null object access
- "IllegalStateException" - Invalid state operation
- "SQLiteException" - Database error

---

## 🛠️ Advanced Debugging

### Enable Verbose Logging
```
./gradlew assembleDebug --info --debug
```

### Check Database Creation
```
adb shell
cd /data/data/com.example.personaleventplanner/databases
ls -la
```

### View Logcat Output
```
adb logcat | grep PersonalEventPlanner
```

### Clear App Cache & Data
```
adb shell pm clear com.example.personaleventplanner
```

---

## 📱 Device/Emulator Requirements

### Minimum Requirements
- Android 6.0 (API 23) or higher
- 100 MB storage space
- 1 GB RAM minimum

### Recommended
- Android 8.0+ (API 26+)
- 2+ GB RAM
- Latest emulator system image

### Emulator Settings
1. Device Manager → Create Virtual Device
2. Select: Phone (e.g., Pixel 5)
3. System Image: Android 13 or higher (API 33+)
4. RAM: 2GB+
5. Storage: 2GB+

---

## 🔧 Gradle Troubleshooting

### Gradle Sync Fails
```
1. File → Settings → Build, Execution, Deployment → Gradle
2. Gradle JDK: Use embedded (11)
3. File → Invalidate Caches
4. File → Sync Now
```

### APK Build Fails
```bash
./gradlew clean build -x lint
```

### Dependency Resolution Issues
```bash
./gradlew dependencies
./gradlew dependencies --graph
```

---

## ⚡ Quick Fix Commands

### Reset Everything
```bash
./gradlew clean
./gradlew build
```

### Full Clean & Rebuild
```bash
rm -rf app/build
./gradlew clean build
```

### Android Studio Clean
1. File → Invalidate Caches → Invalidate and Restart
2. Quit Android Studio
3. Delete: `.gradle` and `.idea` folders
4. Reopen project

---

## 📊 Database Verification

### Check if database created:
```bash
adb shell
su
cd /data/data/com.example.personaleventplanner/databases
ls -la event_database
```

### Clear database if corrupted:
```bash
adb shell rm /data/data/com.example.personaleventplanner/databases/event_database
```

---

## 🎯 Testing After Installation

### Test Sequence:
1. **App launches** - Home screen shows empty event list
2. **Bottom navigation visible** - Two buttons: "Events" and "Add Event"
3. **Add event** - Tap "Add Event", fill form, save
4. **Event appears** - Should see event in list
5. **Edit event** - Tap event, verify pre-filled form
6. **Delete event** - Remove event, verify removal
7. **Navigation works** - Switch between tabs smoothly

---

## 🆘 If All Else Fails

### Nuclear Option - Full Reset
```bash
# Close Android Studio first
1. cd PersonalEventPlanner
2. ./gradlew clean
3. Delete: .gradle folder, .idea folder
4. Delete: app/build folder
5. Reopen project in Android Studio
6. File → Sync Now
7. Build → Clean Project
8. Build → Rebuild Project
9. Run → Run 'app'
```

### Contact Support Information
If issues persist:
1. Save full logcat output
2. Note exact error message
3. Provide: Device model, Android version, Android Studio version
4. Check README.md and IMPLEMENTATION_GUIDE.md for detailed info

---

## ✅ Success Indicators

You know the app is working correctly when:
- ✅ App installs without errors
- ✅ App launches without crashing
- ✅ Event list screen displays (empty initially)
- ✅ Bottom navigation is clickable
- ✅ Can navigate between screens
- ✅ Can add events
- ✅ Events persist after app close
- ✅ No runtime errors in logcat

---

## 📞 Build Information

- **Build Status**: ✅ SUCCESS
- **APK Generated**: Yes (app/build/outputs/apk/debug/app-debug.apk)
- **Minimum SDK**: 23 (Android 6.0)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35
- **Java Version**: 11
- **Gradle Version**: 8.1+

---

## 📝 Key Configuration Summary

### build.gradle.kts
- ✅ Plugins configured correctly
- ✅ Dependencies added for Room, Navigation, LiveData
- ✅ Annotation processor configured for Room
- ✅ allowMainThreadQueries() enabled for development

### AndroidManifest.xml
- ✅ Theme set to Theme.PersonalEventPlanner
- ✅ MainActivity exported and set as launcher
- ✅ Correct application namespace

### Navigation
- ✅ nav_graph.xml defined
- ✅ Three fragments configured
- ✅ Navigation actions set up
- ✅ Arguments passed correctly

### Database
- ✅ EventDatabase configured with allowMainThreadQueries()
- ✅ Event entity created
- ✅ EventDao with all CRUD operations
- ✅ EventRepository for data access

---

**Last Updated**: March 31, 2026  
**Status**: Ready for Deployment ✅  
**Support**: Refer to README.md and IMPLEMENTATION_GUIDE.md

