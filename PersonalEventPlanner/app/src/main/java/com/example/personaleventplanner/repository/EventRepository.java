package com.example.personaleventplanner.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.personaleventplanner.database.EventDatabase;
import com.example.personaleventplanner.model.Event;

import java.util.List;

public class EventRepository {
    private EventDatabase database;
    private LiveData<List<Event>> allEvents;
    
    public EventRepository(Context context) {
        database = EventDatabase.getInstance(context);
        allEvents = database.eventDao().getAllEvents();
    }
    
    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }
    
    public LiveData<Event> getEventById(int id) {
        return database.eventDao().getEventById(id);
    }
    
    public void insertEvent(Event event) {
        new Thread(() -> database.eventDao().insertEvent(event)).start();
    }
    
    public void updateEvent(Event event) {
        new Thread(() -> database.eventDao().updateEvent(event)).start();
    }
    
    public void deleteEvent(Event event) {
        new Thread(() -> database.eventDao().deleteEvent(event)).start();
    }
    
    public void deleteEventById(int id) {
        new Thread(() -> database.eventDao().deleteEventById(id)).start();
    }
}

