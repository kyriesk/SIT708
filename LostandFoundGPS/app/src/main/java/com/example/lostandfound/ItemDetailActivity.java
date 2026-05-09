package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lostandfound.model.Item;
import com.example.lostandfound.repository.ItemRepository;
import com.example.lostandfound.util.DateTimeUtil;
import com.example.lostandfound.util.ImageManager;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView detailImageView;
    private TextView detailTitleTextView;
    private TextView detailStatusTextView;
    private TextView detailCategoryTextView;
    private TextView detailDateTextView;
    private TextView detailLocationTextView;
    private TextView detailPhoneTextView;
    private TextView detailDescriptionTextView;
    private Button deleteButton;
    private Button backButton;

    private ItemRepository itemRepository;
    private Item currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        itemRepository = new ItemRepository(this);
        displayItemDetails();
        setupListeners();
    }

    private void initializeViews() {
        detailImageView = findViewById(R.id.detailImageView);
        detailTitleTextView = findViewById(R.id.detailTitleTextView);
        detailStatusTextView = findViewById(R.id.detailStatusTextView);
        detailCategoryTextView = findViewById(R.id.detailCategoryTextView);
        detailDateTextView = findViewById(R.id.detailDateTextView);
        detailLocationTextView = findViewById(R.id.detailLocationTextView);
        detailPhoneTextView = findViewById(R.id.detailPhoneTextView);
        detailDescriptionTextView = findViewById(R.id.detailDescriptionTextView);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
    }

    private void displayItemDetails() {
        Intent intent = getIntent();
        currentItem = (Item) intent.getSerializableExtra("item");

        if (currentItem != null) {
            detailTitleTextView.setText(currentItem.getTitle());
            detailStatusTextView.setText(currentItem.getStatus());
            detailCategoryTextView.setText(currentItem.getCategoryName());
            detailDateTextView.setText(currentItem.getDatePosted());
            detailLocationTextView.setText(currentItem.getLocation());
            detailPhoneTextView.setText(currentItem.getPhone());
            detailDescriptionTextView.setText(currentItem.getDescription());

            // Load and display image or show placeholder
            if (currentItem.getImagePath() != null && !currentItem.getImagePath().isEmpty()) {
                detailImageView.setImageBitmap(ImageManager.loadImage(currentItem.getImagePath()));
            } else {
                detailImageView.setBackgroundResource(R.drawable.placeholder_image);
                detailImageView.setImageBitmap(null);
            }

            // Set status color
            if ("Lost".equals(currentItem.getStatus())) {
                detailStatusTextView.setTextColor(getResources().getColor(R.color.status_lost, null));
            } else {
                detailStatusTextView.setTextColor(getResources().getColor(R.color.status_found, null));
            }
        }
    }

    private void setupListeners() {
        deleteButton.setOnClickListener(v -> showDeleteConfirmDialog());
        backButton.setOnClickListener(v -> finish());
    }


    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage(getString(R.string.delete_confirm))
            .setPositiveButton("Delete", (dialog, which) -> deleteItem())
            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
            .show();
    }

    private void deleteItem() {
        if (currentItem != null) {
            int result = itemRepository.deleteItem(currentItem.getId());
            if (result > 0) {
                // Delete image file
                if (currentItem.getImagePath() != null) {
                    ImageManager.deleteImage(currentItem.getImagePath());
                }
                Toast.makeText(this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.delete_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (itemRepository != null) {
            itemRepository.close();
        }
    }
}

