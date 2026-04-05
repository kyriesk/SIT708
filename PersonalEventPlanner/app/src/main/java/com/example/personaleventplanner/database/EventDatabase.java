package com.example.personaleventplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.personaleventplanner.model.Event;

@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase {
    
    public abstract EventDao eventDao();
    
    private static EventDatabase instance;
    
    public static synchronized EventDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    EventDatabase.class,
                    "event_database"
            )
            .allowMainThreadQueries()  // Allow queries on main thread for simple apps
            .build();
        }
        return instance;
    }
}

