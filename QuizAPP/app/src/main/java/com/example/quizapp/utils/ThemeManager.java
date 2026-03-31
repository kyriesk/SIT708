package com.example.quizapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    private static final String PREF_NAME = "quiz_app_prefs";
    private static final String THEME_KEY = "app_theme";
    private static final int LIGHT_MODE = AppCompatDelegate.MODE_NIGHT_NO;
    private static final int DARK_MODE = AppCompatDelegate.MODE_NIGHT_YES;
    private static final int DEFAULT_MODE = LIGHT_MODE;

    private SharedPreferences preferences;

    public ThemeManager(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setTheme(int theme) {
        preferences.edit().putInt(THEME_KEY, theme).apply();
        AppCompatDelegate.setDefaultNightMode(theme);
    }

    public int getTheme() {
        return preferences.getInt(THEME_KEY, DEFAULT_MODE);
    }

    public void applyTheme() {
        AppCompatDelegate.setDefaultNightMode(getTheme());
    }

    public boolean isDarkMode() {
        return getTheme() == DARK_MODE;
    }

    public void toggleTheme() {
        if (isDarkMode()) {
            setTheme(LIGHT_MODE);
        } else {
            setTheme(DARK_MODE);
        }
    }

    public static int getLightMode() {
        return LIGHT_MODE;
    }

    public static int getDarkMode() {
        return DARK_MODE;
    }
}

