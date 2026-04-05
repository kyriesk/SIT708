package com.example.personaleventplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personaleventplanner.R;
import com.example.personaleventplanner.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events = new ArrayList<>();
    private OnEventClickListener listener;
    
    public interface OnEventClickListener {
        void onEventClick(Event event);
        void onEventDelete(Event event);
    }
    
    public EventAdapter(OnEventClickListener listener) {
        this.listener = listener;
    }
    
    public void setEvents(List<Event> events) {
        if (events == null) {
            this.events = new ArrayList<>();
        } else {
            this.events = events;
        }
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view, listener);
    }
    
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }
    
    @Override
    public int getItemCount() {
        return events.size();
    }
    
    static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private TextView categoryView;
        private TextView locationView;
        private TextView dateTimeView;
        private View deleteButton;
        private OnEventClickListener listener;
        private Event currentEvent;
        
        EventViewHolder(@NonNull View itemView, OnEventClickListener listener) {
            super(itemView);
            this.listener = listener;
            titleView = itemView.findViewById(R.id.eventTitle);
            categoryView = itemView.findViewById(R.id.eventCategory);
            locationView = itemView.findViewById(R.id.eventLocation);
            dateTimeView = itemView.findViewById(R.id.eventDateTime);
            deleteButton = itemView.findViewById(R.id.deleteEventButton);
            
            itemView.setOnClickListener(v -> {
                if (listener != null && currentEvent != null) {
                    listener.onEventClick(currentEvent);
                }
            });
            
            deleteButton.setOnClickListener(v -> {
                if (listener != null && currentEvent != null) {
                    listener.onEventDelete(currentEvent);
                }
            });
        }
        
        void bind(Event event) {
            this.currentEvent = event;
            titleView.setText(event.getTitle());
            categoryView.setText("Category: " + event.getCategory());
            locationView.setText("Location: " + event.getLocation());
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
            String dateStr = sdf.format(new Date(event.getDateTime()));
            dateTimeView.setText("Date: " + dateStr);
        }
    }
}

