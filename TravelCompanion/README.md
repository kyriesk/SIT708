# TravelCompanion

TravelCompanion is a handy Android utility application designed to assist travelers with common conversions they might need while on the go. The app provides a simple, tabbed interface to quickly switch between different conversion tools.

## Features

### 1. Currency Converter
Convert between popular global currencies using predefined exchange rates.
- **Supported Currencies:** USD, AUD, EUR, JPY, GBP.
- **How to use:** Enter the amount, select the source currency, select the target currency, and tap the convert button.

### 2. Fuel & Distance Converter
A versatile tool for vehicle-related conversions.
- **Fuel Efficiency:** Convert between km/L, L/100km, and MPG (US).
- **Volume:** Convert between Liters and Gallons (US).
- **Distance:** Convert between Kilometers and Nautical Miles.

### 3. Temperature Converter
Easily switch between different temperature scales.
- **Supported Units:** Celsius, Fahrenheit, and Kelvin.
- **Logic:** Handles conversions between all three units accurately.

## Technical Details

- **Language:** Java
- **Architecture:** Fragment-based UI with a `ViewPager2` and `TabLayout` for navigation.
- **UI Components:** 
    - `Material Components` for tabs and layout.
    - `Spinners` for unit selection.
    - `Fragments` for modular feature implementation.
- **Minimum SDK:** Uses Modern Android features like `EdgeToEdge` and `ViewBinding` concepts (via IDs).

## Installation

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an Android emulator or a physical device.

## Project Structure

- `MainActivity.java`: The entry point that sets up the `ViewPager2` and `TabLayoutMediator`.
- `ViewPagerAdapter.java`: Manages the fragments within the ViewPager.
- `CurrencyFragment.java`: Logic for currency conversions.
- `FuelFragment.java`: Logic for fuel and distance conversions.
- `TemperatureFragment.java`: Logic for temperature conversions.
- `res/layout/`: Contains the XML layout files for the activity and fragments.
- `res/values/strings.xml`: Contains the arrays for the spinners (e.g., `currency_array`, `fuel_array`, `temp_array`).


