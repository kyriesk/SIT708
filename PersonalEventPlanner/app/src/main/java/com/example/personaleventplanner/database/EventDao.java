package com.example.personaleventplanner.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.personaleventplanner.model.Event;

import java.util.List;

@Dao
public interface EventDao {
    
    @Insert
    long insertEvent(Event event);
    
    @Update
    void updateEvent(Event event);
    
    @Delete
    void deleteEvent(Event event);
    
    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    LiveData<List<Event>> getAllEvents();
    
    @Query("SELECT * FROM events WHERE id = :id")
    LiveData<Event> getEventById(int id);
    
    @Query("DELETE FROM events WHERE id = :id")
    void deleteEventById(int id);
}

