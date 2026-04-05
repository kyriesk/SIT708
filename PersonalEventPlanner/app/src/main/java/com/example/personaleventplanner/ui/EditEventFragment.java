package com.example.personaleventplanner.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.personaleventplanner.R;
import com.example.personaleventplanner.model.Event;
import com.example.personaleventplanner.viewmodel.EventViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditEventFragment extends Fragment {
    
    private EditText titleInput;
    private Spinner categorySpinner;
    private EditText locationInput;
    private TextView dateTimeDisplay;
    private Button updateButton;
    private Button deleteButton;
    private Button cancelButton;
    private EventViewModel viewModel;
    private Calendar selectedDateTime;
    private int eventId;
    private Event currentEvent;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        if (getArguments() != null) {
            eventId = getArguments().getInt("eventId");
        }
        
        titleInput = view.findViewById(R.id.eventTitleInput);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        locationInput = view.findViewById(R.id.eventLocationInput);
        dateTimeDisplay = view.findViewById(R.id.dateTimeDisplay);
        updateButton = view.findViewById(R.id.updateEventButton);
        deleteButton = view.findViewById(R.id.deleteEventButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        
        // Setup category spinner
        String[] categories = {"Work", "Social", "Travel", "Personal", "Health"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        
        // Initialize selected date time
        selectedDateTime = Calendar.getInstance();
        
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        
        // Load event data
        viewModel.getEventById(eventId).observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                currentEvent = event;
                titleInput.setText(event.getTitle());
                locationInput.setText(event.getLocation());
                
                // Set spinner to correct category
                ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) categorySpinner.getAdapter();
                int position = spinnerAdapter.getPosition(event.getCategory());
                categorySpinner.setSelection(position);
                
                selectedDateTime.setTimeInMillis(event.getDateTime());
                updateDateTimeDisplay();
            }
        });
        
        // Date/Time picker
        dateTimeDisplay.setOnClickListener(v -> showDateTimePicker());
        
        // Update button
        updateButton.setOnClickListener(v -> updateEvent());
        
        // Delete button
        deleteButton.setOnClickListener(v -> deleteEvent());
        
        // Cancel button
        cancelButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
    }
    
    private void showDateTimePicker() {
        DatePickerDialog.OnDateSetListener dateListener = (view, year, month, dayOfMonth) -> {
            selectedDateTime.set(year, month, dayOfMonth);
            showTimePicker();
        };
        
        new DatePickerDialog(getContext(), dateListener,
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)).show();
    }
    
    private void showTimePicker() {
        TimePickerDialog.OnTimeSetListener timeListener = (view, hourOfDay, minute) -> {
            selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedDateTime.set(Calendar.MINUTE, minute);
            updateDateTimeDisplay();
        };
        
        new TimePickerDialog(getContext(), timeListener,
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE), true).show();
    }
    
    private void updateDateTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        dateTimeDisplay.setText(sdf.format(selectedDateTime.getTime()));
    }
    
    private void updateEvent() {
        String title = titleInput.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String location = locationInput.getText().toString().trim();
        
        // Validation
        if (title.isEmpty()) {
            Snackbar.make(requireView(), "Please enter event title", Snackbar.LENGTH_SHORT).show();
            return;
        }
        
        if (location.isEmpty()) {
            Snackbar.make(requireView(), "Please enter event location", Snackbar.LENGTH_SHORT).show();
            return;
        }
        
        if (currentEvent != null) {
            currentEvent.setTitle(title);
            currentEvent.setCategory(category);
            currentEvent.setLocation(location);
            currentEvent.setDateTime(selectedDateTime.getTimeInMillis());
            
            viewModel.updateEvent(currentEvent);
            Snackbar.make(requireView(), "Event updated successfully", Snackbar.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).popBackStack();
        }
    }
    
    private void deleteEvent() {
        if (currentEvent != null) {
            viewModel.deleteEvent(currentEvent);
            Snackbar.make(requireView(), "Event deleted successfully", Snackbar.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).popBackStack();
        }
    }
}

