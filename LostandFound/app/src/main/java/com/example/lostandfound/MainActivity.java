package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfound.adapter.ItemAdapter;
import com.example.lostandfound.model.Category;
import com.example.lostandfound.model.Item;
import com.example.lostandfound.repository.CategoryRepository;
import com.example.lostandfound.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView itemsRecyclerView;
    private EditText searchEditText;
    private Spinner categorySpinner;
    private TextView emptyStateTextView;
    private ItemAdapter adapter;
    private ItemRepository itemRepository;
    private CategoryRepository categoryRepository;
    private List<Item> allItems;
    private int selectedCategoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupRepositories();
        setupRecyclerView();
        setupCategorySpinner();
        loadItems();
        setupSearchListener();
    }

    private void initializeViews() {
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);
    }

    private void setupRepositories() {
        itemRepository = new ItemRepository(this);
        categoryRepository = new CategoryRepository(this);
    }

    private void setupRecyclerView() {
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allItems = new ArrayList<>();
        adapter = new ItemAdapter(this, allItems, item -> {
            Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        });
        itemsRecyclerView.setAdapter(adapter);
    }

    private void setupCategorySpinner() {
        List<Category> categories = categoryRepository.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("All Categories");
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, categoryNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, 
                                      int position, long id) {
                if (position == 0) {
                    selectedCategoryId = 0;
                    loadItems();
                } else {
                    selectedCategoryId = position;
                    filterByCategory(position);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void loadItems() {
        try {
            allItems.clear();
            allItems.addAll(itemRepository.getAllItems());
            adapter.updateItems(allItems);
            updateEmptyState();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading items", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterByCategory(int categoryId) {
        try {
            allItems.clear();
            allItems.addAll(itemRepository.getItemsByCategory(categoryId));
            adapter.updateItems(allItems);
            updateEmptyState();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error filtering items", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchItems(String searchTerm) {
        if (searchTerm.trim().isEmpty()) {
            loadItems();
        } else {
            allItems.clear();
            allItems.addAll(itemRepository.searchItems(searchTerm));
            adapter.updateItems(allItems);
            updateEmptyState();
        }
    }


    private void updateEmptyState() {
        if (allItems.isEmpty()) {
            emptyStateTextView.setVisibility(android.view.View.VISIBLE);
            itemsRecyclerView.setVisibility(android.view.View.GONE);
        } else {
            emptyStateTextView.setVisibility(android.view.View.GONE);
            itemsRecyclerView.setVisibility(android.view.View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (itemRepository != null) {
            itemRepository.close();
        }
        if (categoryRepository != null) {
            categoryRepository.close();
        }
    }
}