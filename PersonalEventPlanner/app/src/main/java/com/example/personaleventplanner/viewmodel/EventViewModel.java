package com.example.personaleventplanner.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.personaleventplanner.model.Event;
import com.example.personaleventplanner.repository.EventRepository;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private EventRepository repository;
    private LiveData<List<Event>> allEvents;
    
    public EventViewModel(Application application) {
        super(application);
        repository = new EventRepository(application);
        allEvents = repository.getAllEvents();
    }
    
    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }
    
    public LiveData<Event> getEventById(int id) {
        return repository.getEventById(id);
    }
    
    public void insertEvent(Event event) {
        repository.insertEvent(event);
    }
    
    public void updateEvent(Event event) {
        repository.updateEvent(event);
    }
    
    public void deleteEvent(Event event) {
        repository.deleteEvent(event);
    }
    
    public void deleteEventById(int id) {
        repository.deleteEventById(id);
    }
}

