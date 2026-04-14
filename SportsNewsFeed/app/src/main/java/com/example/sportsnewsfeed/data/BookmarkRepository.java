package com.example.sportsnewsfeed.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sportsnewsfeed.models.SportsNews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkRepository {
    private static final String PREF_NAME = "sports_bookmarks";
    private static final String BOOKMARKS_KEY = "bookmarked_stories";
    private final SharedPreferences preferences;
    private final Gson gson;

    public BookmarkRepository(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void addBookmark(SportsNews news) {
        List<SportsNews> bookmarks = getAllBookmarks();
        // Check if already bookmarked
        for (SportsNews bookmark : bookmarks) {
            if (bookmark.getId() == news.getId()) {
                return; // Already bookmarked
            }
        }
        bookmarks.add(news);
        saveBookmarks(bookmarks);
    }

    public void removeBookmark(int newsId) {
        List<SportsNews> bookmarks = getAllBookmarks();
        for (int i = bookmarks.size() - 1; i >= 0; i--) {
            if (bookmarks.get(i).getId() == newsId) {
                bookmarks.remove(i);
            }
        }
        saveBookmarks(bookmarks);
    }

    public List<SportsNews> getAllBookmarks() {
        String json = preferences.getString(BOOKMARKS_KEY, "[]");
        Type type = new TypeToken<List<SportsNews>>() {}.getType();
        List<SportsNews> bookmarks = gson.fromJson(json, type);
        return bookmarks != null ? bookmarks : new ArrayList<>();
    }

    public boolean isBookmarked(int newsId) {
        List<SportsNews> bookmarks = getAllBookmarks();
        for (SportsNews bookmark : bookmarks) {
            if (bookmark.getId() == newsId) {
                return true;
            }
        }
        return false;
    }

    private void saveBookmarks(List<SportsNews> bookmarks) {
        String json = gson.toJson(bookmarks);
        preferences.edit().putString(BOOKMARKS_KEY, json).apply();
    }

    public void clearAllBookmarks() {
        preferences.edit().clear().apply();
    }
}


