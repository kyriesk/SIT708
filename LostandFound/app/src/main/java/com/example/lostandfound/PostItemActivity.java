package com.example.lostandfound;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lostandfound.model.Category;
import com.example.lostandfound.model.Item;
import com.example.lostandfound.repository.CategoryRepository;
import com.example.lostandfound.repository.ItemRepository;
import com.example.lostandfound.util.DateTimeUtil;
import com.example.lostandfound.util.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class PostItemActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText phoneEditText;
    private EditText dateEditText;
    private EditText locationEditText;
    private Spinner categorySpinner;
    private RadioGroup statusRadioGroup;
    private RadioButton statusLostRadio;
    private RadioButton statusFoundRadio;
    private ImageView imagePreviewImageView;
    private Button selectImageButton;
    private Button takePhotoButton;
    private Button postButton;
    private Button cancelButton;

    private ItemRepository itemRepository;
    private CategoryRepository categoryRepository;
    private String selectedImagePath;
    private int selectedCategoryId = 1;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<Intent> takePhotoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupRepositories();
        setupCategorySpinner();
        setupImagePickers();
        setupListeners();
    }

    private void initializeViews() {
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        dateEditText = findViewById(R.id.dateEditText);
        locationEditText = findViewById(R.id.locationEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        statusRadioGroup = findViewById(R.id.statusRadioGroup);
        statusLostRadio = findViewById(R.id.statusLostRadio);
        statusFoundRadio = findViewById(R.id.statusFoundRadio);
        imagePreviewImageView = findViewById(R.id.imagePreviewImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        postButton = findViewById(R.id.postButton);
        cancelButton = findViewById(R.id.cancelButton);

        statusLostRadio.setChecked(true);
    }

    private void setupRepositories() {
        itemRepository = new ItemRepository(this);
        categoryRepository = new CategoryRepository(this);
    }

    private void setupCategorySpinner() {
        List<Category> categories = categoryRepository.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view,
                                      int position, long id) {
                selectedCategoryId = position + 1;
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void setupImagePickers() {
        pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        selectedImagePath = ImageManager.saveImage(this, imageUri);
                        if (selectedImagePath != null) {
                            imagePreviewImageView.setImageBitmap(ImageManager.loadImage(selectedImagePath));
                            Toast.makeText(this, R.string.image_selected, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        takePhotoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    android.graphics.Bitmap photo = (android.graphics.Bitmap) result.getData().getParcelableExtra("data");
                    if (photo != null) {
                        // Save the bitmap to file and get the path
                        selectedImagePath = ImageManager.saveBitmap(this, photo);
                        if (selectedImagePath != null) {
                            imagePreviewImageView.setImageBitmap(photo);
                            Toast.makeText(this, R.string.image_selected, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

    private void setupListeners() {
        selectImageButton.setOnClickListener(v -> selectImageFromGallery());
        takePhotoButton.setOnClickListener(v -> takePhoto());
        postButton.setOnClickListener(v -> postItem());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoLauncher.launch(takePictureIntent);
    }

    private void postItem() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String status = statusLostRadio.isChecked() ? "Lost" : "Found";

        if (title.isEmpty()) {
            Toast.makeText(this, "Item name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Date is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (location.isEmpty()) {
            Toast.makeText(this, "Location is required", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Item item = new Item();
            item.setTitle(title);
            item.setDescription(description);
            item.setPhone(phone);
            item.setLocation(location);
            item.setCategoryId(selectedCategoryId);
            item.setStatus(status);
            item.setDatePosted(date);
            if (selectedImagePath != null) {
                item.setImagePath(selectedImagePath);
            }

            long result = itemRepository.insertItem(item);

            if (result > 0) {
                Toast.makeText(this, "Item posted successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error posting item. Please check your data and try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

