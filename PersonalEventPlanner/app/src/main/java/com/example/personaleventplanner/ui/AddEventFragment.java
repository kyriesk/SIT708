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

public class AddEventFragment extends Fragment {
    
    private EditText titleInput;
    private Spinner categorySpinner;
    private EditText locationInput;
    private TextView dateTimeDisplay;
    private Button saveButton;
    private Button cancelButton;
    private EventViewModel viewModel;
    private Calendar selectedDateTime;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        titleInput = view.findViewById(R.id.eventTitleInput);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        locationInput = view.findViewById(R.id.eventLocationInput);
        dateTimeDisplay = view.findViewById(R.id.dateTimeDisplay);
        saveButton = view.findViewById(R.id.saveEventButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        
        // Setup category spinner
        String[] categories = {"Work", "Social", "Travel", "Personal", "Health"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        
        // Initialize selected date time to current time
        selectedDateTime = Calendar.getInstance();
        updateDateTimeDisplay();
        
        // Date/Time picker
        dateTimeDisplay.setOnClickListener(v -> showDateTimePicker());
        
        // Save button
        saveButton.setOnClickListener(v -> saveEvent());
        
        // Cancel button
        cancelButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
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
    
    private void saveEvent() {
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
        
        // Check if date is in the past
        if (selectedDateTime.getTimeInMillis() < System.currentTimeMillis()) {
            Snackbar.make(requireView(), "Event date cannot be in the past", Snackbar.LENGTH_SHORT).show();
            return;
        }
        
        Event event = new Event(title, category, location, selectedDateTime.getTimeInMillis());
        viewModel.insertEvent(event);
        
        Snackbar.make(requireView(), "Event saved successfully", Snackbar.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).popBackStack();
    }
}


