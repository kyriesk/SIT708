# Quiz App

A modern Android application for taking multiple-choice quizzes, tracking progress, and viewing results, with full support for light/dark mode and session persistence.

---

## Features

- **Multiple-Choice Quiz:** Users can answer a series of questions, with instant feedback on correctness.
- **Visual Feedback:** Correct answers turn green, incorrect selections turn red, and the correct answer is always highlighted after submission.
- **Progress Tracking:** A progress bar and question counter update in real time as the user advances through the quiz.
- **Results Screen:** Displays the user's score, congratulatory message, and options to retake the quiz or exit.
- **Theme Switching:** Toggle between light and dark mode instantly from any screen. The selected theme persists across sessions and screens.
- **Session Persistence:** User name and quiz state are retained as appropriate.
- **Responsive UI:** Clean, modern Material Design layouts for all screens.

---

## Installation

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an Android emulator or a physical device.


---

## Usage Guide

1. **Start Screen:**
   - Enter your name.
   - Toggle light/dark mode using the switch.
   - Tap "Start Quiz" to begin.
2. **Quiz Screen:**
   - Select an answer and tap "Submit Answer".
   - Correct answers turn green; incorrect selections turn red, and the correct answer is highlighted.
   - Progress bar and question counter update as you proceed.
   - Toggle theme at any time.
3. **Results Screen:**
   - View your score and a congratulatory message.
   - Tap "Take New Quiz" to restart (your name is retained).
   - Tap "Finish" to exit the app.
   - Theme toggle is available here as well.

---

## Project Structure

- **app/src/main/java/com/example/quizapp/**
  - `MainActivity.java`: Handles the start screen, user name input, and navigation to the quiz.
  - `QuizActivity.java`: Manages quiz logic, answer selection, progress tracking, and visual feedback.
  - `ResultsActivity.java`: Displays the final score, congratulatory message, and handles quiz restart/exit.
  - **model/**
    - `Question.java`: Data model representing a quiz question, its options, and the correct answer.
    - `Quiz.java`: Holds the list of questions and provides quiz data access.
  - **utils/**
    - `ThemeManager.java`: Utility for saving, applying, and toggling light/dark mode across the app.

- **app/src/main/res/layout/**
  - `activity_main.xml`: Layout for the start screen (user name input, start button, theme toggle).
  - `activity_quiz.xml`: Layout for the quiz screen (progress bar, question, options, submit button, theme toggle).
  - `activity_results.xml`: Layout for the results screen (score, message, buttons, theme toggle).

- **app/src/main/res/values/**
  - `strings.xml`: All user-facing strings and messages.
  - `colors.xml`: Color definitions for themes, buttons, and feedback.
  - `themes.xml` & `themes-night.xml`: Light and dark theme definitions.

- **app/src/main/AndroidManifest.xml**: Declares app components and main entry points.



