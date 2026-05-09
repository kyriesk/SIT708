package com.example.lostandfound.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lostandfound.database.LostandFoundDatabase;
import com.example.lostandfound.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private LostandFoundDatabase dbHelper;
    private SQLiteDatabase database;

    public CategoryRepository(Context context) {
        dbHelper = new LostandFoundDatabase(context);
        try {
            database = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ensureDatabase() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getReadableDatabase();
        }
    }

    public List<Category> getAllCategories() {
        ensureDatabase();
        List<Category> categories = new ArrayList<>();
        Cursor cursor = database.query(
                LostandFoundDatabase.TABLE_CATEGORIES,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_CATEGORY_ID);
            int nameIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_CATEGORY_NAME);

            Category category = new Category();
            category.setId(cursor.getInt(idIndex));
            category.setName(cursor.getString(nameIndex));
            categories.add(category);
        }
        cursor.close();
        return categories;
    }

    public Category getCategoryById(int id) {
        Cursor cursor = database.query(
                LostandFoundDatabase.TABLE_CATEGORIES,
                null,
                LostandFoundDatabase.COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        Category category = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_CATEGORY_ID);
            int nameIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_CATEGORY_NAME);

            category = new Category();
            category.setId(cursor.getInt(idIndex));
            category.setName(cursor.getString(nameIndex));
        }
        cursor.close();
        return category;
    }

    public String getCategoryNameById(int id) {
        ensureDatabase();
        Cursor cursor = database.query(
                LostandFoundDatabase.TABLE_CATEGORIES,
                new String[]{LostandFoundDatabase.COLUMN_CATEGORY_NAME},
                LostandFoundDatabase.COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

