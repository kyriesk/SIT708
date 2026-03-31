package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.utils.ThemeManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {
    private EditText userNameInput;
    private Button startButton;
    private SwitchMaterial themeToggle;
    private ThemeManager themeManager;
    private boolean isInitializing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize theme manager and apply saved theme
        themeManager = new ThemeManager(this);
        themeManager.applyTheme();
        
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        userNameInput = findViewById(R.id.userNameInput);
        startButton = findViewById(R.id.startButton);
        themeToggle = findViewById(R.id.themeToggle);
        
        // Check if userName was passed from ResultsActivity
        String userName = getIntent().getStringExtra("userName");
        if (userName != null && !userName.isEmpty()) {
            userNameInput.setText(userName);
        }
        
        // Set initial theme toggle state
        themeToggle.setChecked(themeManager.isDarkMode());
        
        // Mark initialization as complete
        isInitializing = false;
    }

    private void setupListeners() {
        startButton.setOnClickListener(v -> startQuiz());
        
        themeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isInitializing) {
                return; // Skip during initialization
            }
            if (isChecked) {
                themeManager.setTheme(ThemeManager.getDarkMode());
            } else {
                themeManager.setTheme(ThemeManager.getLightMode());
            }
            recreate();
        });
    }

    private void startQuiz() {
        String userName = userNameInput.getText().toString().trim();
        
        if (userName.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_name, Toast.LENGTH_SHORT).show();
            return;
        }
        
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }
}