package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    private Button createAdvertButton;
    private Button showItemsButton;
    private Button showMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        createAdvertButton = findViewById(R.id.createAdvertButton);
        showItemsButton = findViewById(R.id.showItemsButton);
        showMapButton = findViewById(R.id.showMapButton);
    }

    private void setupListeners() {
        createAdvertButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, PostItemActivity.class);
            startActivity(intent);
        });

        showItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        });

        showMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, MapActivity.class);
            intent.putExtra("radius", 50.0); // Default 50km radius
            startActivity(intent);
        });
    }
}
