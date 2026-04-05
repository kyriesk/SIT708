package com.example.personaleventplanner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personaleventplanner.R;
import com.example.personaleventplanner.adapter.EventAdapter;
import com.example.personaleventplanner.model.Event;
import com.example.personaleventplanner.viewmodel.EventViewModel;
import com.google.android.material.snackbar.Snackbar;

public class EventListFragment extends Fragment implements EventAdapter.OnEventClickListener {
    
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private EventViewModel viewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerView = view.findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new EventAdapter(this);
        recyclerView.setAdapter(adapter);
        
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        
        viewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.setEvents(events);
        });
    }
    
    @Override
    public void onEventClick(Event event) {
        Bundle bundle = new Bundle();
        bundle.putInt("eventId", event.getId());
        Navigation.findNavController(requireView()).navigate(R.id.action_eventListFragment_to_editEventFragment, bundle);
    }
    
    @Override
    public void onEventDelete(Event event) {
        viewModel.deleteEvent(event);
        Snackbar.make(requireView(), "Event deleted successfully", Snackbar.LENGTH_SHORT).show();
    }
}

