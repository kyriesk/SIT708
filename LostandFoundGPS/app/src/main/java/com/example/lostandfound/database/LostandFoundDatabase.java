package com.example.lostandfound.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LostandFoundDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "lostandfound.db";
    private static final int DATABASE_VERSION = 3;

    // Table names
    public static final String TABLE_ITEMS = "items";
    public static final String TABLE_CATEGORIES = "categories";

    // Items table columns
    public static final String COLUMN_ITEM_ID = "id";
    public static final String COLUMN_ITEM_TITLE = "title";
    public static final String COLUMN_ITEM_DESCRIPTION = "description";
    public static final String COLUMN_ITEM_CATEGORY_ID = "category_id";
    public static final String COLUMN_ITEM_STATUS = "status"; // LOST or FOUND
    public static final String COLUMN_ITEM_DATE_POSTED = "date_posted";
    public static final String COLUMN_ITEM_IMAGE_PATH = "image_path";
    public static final String COLUMN_ITEM_PHONE = "phone";
    public static final String COLUMN_ITEM_LOCATION = "location";
    public static final String COLUMN_ITEM_LATITUDE = "latitude";
    public static final String COLUMN_ITEM_LONGITUDE = "longitude";
    public static final String COLUMN_ITEM_CREATED_AT = "created_at";

    // Categories table columns
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "name";

    // SQL Create statements
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
            COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CATEGORY_NAME + " TEXT NOT NULL" +
            ");";

    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE " + TABLE_ITEMS + " (" +
            COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ITEM_TITLE + " TEXT NOT NULL, " +
            COLUMN_ITEM_DESCRIPTION + " TEXT, " +
            COLUMN_ITEM_CATEGORY_ID + " INTEGER NOT NULL, " +
            COLUMN_ITEM_STATUS + " TEXT NOT NULL, " +
            COLUMN_ITEM_DATE_POSTED + " TEXT NOT NULL, " +
            COLUMN_ITEM_IMAGE_PATH + " TEXT, " +
            COLUMN_ITEM_PHONE + " TEXT, " +
            COLUMN_ITEM_LOCATION + " TEXT, " +
            COLUMN_ITEM_LATITUDE + " REAL DEFAULT 0.0, " +
            COLUMN_ITEM_LONGITUDE + " REAL DEFAULT 0.0, " +
            COLUMN_ITEM_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (" + COLUMN_ITEM_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + ")" +
            ");";

    public LostandFoundDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_ITEMS);

        // Insert default categories
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Electronics');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Pets');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Wallets');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Keys');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Documents');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Clothing');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Jewelry');");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Other');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + COLUMN_ITEM_LATITUDE + " REAL DEFAULT 0.0;");
                db.execSQL("ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + COLUMN_ITEM_LONGITUDE + " REAL DEFAULT 0.0;");
            } catch (Exception e) {
                // Columns may already exist
            }
        }
        if (oldVersion < newVersion) {
            // Drop existing tables if they exist
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            onCreate(db);
        }
    }
}

