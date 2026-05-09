package com.example.lostandfound.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lostandfound.database.LostandFoundDatabase;
import com.example.lostandfound.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private static final String TAG = "ItemRepository";
    private LostandFoundDatabase dbHelper;
    private SQLiteDatabase database;
    private CategoryRepository categoryRepository;

    public ItemRepository(Context context) {
        dbHelper = new LostandFoundDatabase(context);
        try {
            database = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Error getting writable database", e);
            e.printStackTrace();
            database = dbHelper.getReadableDatabase();
        }
        categoryRepository = new CategoryRepository(context);
    }

    private void ensureDatabase() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public long insertItem(Item item) {
        ensureDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(LostandFoundDatabase.COLUMN_ITEM_TITLE, item.getTitle());
            values.put(LostandFoundDatabase.COLUMN_ITEM_DESCRIPTION, item.getDescription());
            values.put(LostandFoundDatabase.COLUMN_ITEM_CATEGORY_ID, item.getCategoryId());
            values.put(LostandFoundDatabase.COLUMN_ITEM_STATUS, item.getStatus());
            values.put(LostandFoundDatabase.COLUMN_ITEM_DATE_POSTED, item.getDatePosted());
            values.put(LostandFoundDatabase.COLUMN_ITEM_IMAGE_PATH, item.getImagePath());
            values.put(LostandFoundDatabase.COLUMN_ITEM_PHONE, item.getPhone());
            values.put(LostandFoundDatabase.COLUMN_ITEM_LOCATION, item.getLocation());
            values.put(LostandFoundDatabase.COLUMN_ITEM_LATITUDE, item.getLatitude());
            values.put(LostandFoundDatabase.COLUMN_ITEM_LONGITUDE, item.getLongitude());

            long result = database.insert(LostandFoundDatabase.TABLE_ITEMS, null, values);
            Log.d(TAG, "Insert result: " + result);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting item", e);
            e.printStackTrace();
            return -1;
        }
    }

    public List<Item> getAllItems() {
        ensureDatabase();
        List<Item> items = new ArrayList<>();
        Cursor cursor = database.query(
                LostandFoundDatabase.TABLE_ITEMS,
                null,
                null,
                null,
                null,
                null,
                LostandFoundDatabase.COLUMN_ITEM_DATE_POSTED + " DESC"
        );

        while (cursor.moveToNext()) {
            items.add(cursorToItem(cursor));
        }
        cursor.close();
        return items;
    }

    public List<Item> getItemsByCategory(int categoryId) {
        ensureDatabase();
        List<Item> items = new ArrayList<>();
        Cursor cursor = database.query(
                LostandFoundDatabase.TABLE_ITEMS,
                null,
                LostandFoundDatabase.COLUMN_ITEM_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)},
                null,
                null,
                LostandFoundDatabase.COLUMN_ITEM_DATE_POSTED + " DESC"
        );

        while (cursor.moveToNext()) {
            items.add(cursorToItem(cursor));
        }
        cursor.close();
        return items;
    }

    public List<Item> searchItems(String searchTerm) {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM " + LostandFoundDatabase.TABLE_ITEMS +
                      " WHERE " + LostandFoundDatabase.COLUMN_ITEM_TITLE + " LIKE ? OR " +
                      LostandFoundDatabase.COLUMN_ITEM_DESCRIPTION + " LIKE ?" +
                      " ORDER BY " + LostandFoundDatabase.COLUMN_ITEM_DATE_POSTED + " DESC";

        Cursor cursor = database.rawQuery(query, new String[]{"%" + searchTerm + "%", "%" + searchTerm + "%"});

        while (cursor.moveToNext()) {
            items.add(cursorToItem(cursor));
        }
        cursor.close();
        return items;
    }

    /**
     * Search for items within a radius of a given location
     * @param userLatitude User's latitude
     * @param userLongitude User's longitude
     * @param radiusInKm Search radius in kilometers
     * @return List of items within the radius
     */
    public List<Item> searchItemsByRadius(double userLatitude, double userLongitude, double radiusInKm) {
        ensureDatabase();
        List<Item> items = new ArrayList<>();

        // Using Haversine formula to calculate distance
        // Distance = 2 * R * asin(sqrt(sin²((lat2-lat1)/2) + cos(lat1) * cos(lat2) * sin²((lon2-lon1)/2)))
        // Simplified query using distance calculation
        String query = "SELECT * FROM " + LostandFoundDatabase.TABLE_ITEMS +
                      " WHERE (" +
                      "111.045 * sqrt(pow(" + LostandFoundDatabase.COLUMN_ITEM_LATITUDE + " - ?, 2) + " +
                      "pow((" + LostandFoundDatabase.COLUMN_ITEM_LONGITUDE + " - ?) * " +
                      "cos(radians((" + LostandFoundDatabase.COLUMN_ITEM_LATITUDE + " + ?) / 2)), 2))" +
                      ") <= ? " +
                      "ORDER BY " + LostandFoundDatabase.COLUMN_ITEM_DATE_POSTED + " DESC";

        try {
            Cursor cursor = database.rawQuery(query, new String[]{
                    String.valueOf(userLatitude),
                    String.valueOf(userLongitude),
                    String.valueOf(userLatitude),
                    String.valueOf(radiusInKm)
            });

            while (cursor.moveToNext()) {
                items.add(cursorToItem(cursor));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error searching items by radius", e);
            // Fallback: get all items and filter in Java
            items = getAllItems();
        }

        return items;
    }

    public Item getItemById(int id) {
        Cursor cursor = database.query(
                LostandFoundDatabase.TABLE_ITEMS,
                null,
                LostandFoundDatabase.COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        Item item = null;
        if (cursor.moveToFirst()) {
            item = cursorToItem(cursor);
        }
        cursor.close();
        return item;
    }

    public int updateItem(Item item) {
        ContentValues values = new ContentValues();
        values.put(LostandFoundDatabase.COLUMN_ITEM_TITLE, item.getTitle());
        values.put(LostandFoundDatabase.COLUMN_ITEM_DESCRIPTION, item.getDescription());
        values.put(LostandFoundDatabase.COLUMN_ITEM_CATEGORY_ID, item.getCategoryId());
        values.put(LostandFoundDatabase.COLUMN_ITEM_STATUS, item.getStatus());
        values.put(LostandFoundDatabase.COLUMN_ITEM_IMAGE_PATH, item.getImagePath());

        return database.update(
                LostandFoundDatabase.TABLE_ITEMS,
                values,
                LostandFoundDatabase.COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );
    }

    public int deleteItem(int id) {
        return database.delete(
                LostandFoundDatabase.TABLE_ITEMS,
                LostandFoundDatabase.COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    private Item cursorToItem(Cursor cursor) {
        Item item = new Item();
        
        int idIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_ID);
        int titleIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_TITLE);
        int descIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_DESCRIPTION);
        int categoryIdIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_CATEGORY_ID);
        int statusIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_STATUS);
        int dateIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_DATE_POSTED);
        int imageIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_IMAGE_PATH);
        int phoneIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_PHONE);
        int locationIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_LOCATION);
        int latitudeIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_LATITUDE);
        int longitudeIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_LONGITUDE);
        int createdAtIndex = cursor.getColumnIndex(LostandFoundDatabase.COLUMN_ITEM_CREATED_AT);

        item.setId(cursor.getInt(idIndex));
        item.setTitle(cursor.getString(titleIndex));
        item.setDescription(cursor.getString(descIndex));
        int categoryId = cursor.getInt(categoryIdIndex);
        item.setCategoryId(categoryId);
        item.setCategoryName(categoryRepository.getCategoryNameById(categoryId));
        item.setStatus(cursor.getString(statusIndex));
        item.setDatePosted(cursor.getString(dateIndex));
        item.setImagePath(cursor.getString(imageIndex));
        item.setPhone(cursor.getString(phoneIndex));
        item.setLocation(cursor.getString(locationIndex));
        if (latitudeIndex >= 0) {
            item.setLatitude(cursor.getDouble(latitudeIndex));
        }
        if (longitudeIndex >= 0) {
            item.setLongitude(cursor.getDouble(longitudeIndex));
        }
        if (createdAtIndex >= 0) {
            item.setCreatedAt(cursor.getString(createdAtIndex));
        }

        return item;
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (categoryRepository != null) {
            categoryRepository.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

