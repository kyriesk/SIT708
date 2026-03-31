package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.utils.ThemeManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ResultsActivity extends AppCompatActivity {
    private TextView congratsMessage;
    private TextView scoreDisplay;
    private Button newQuizButton;
    private Button finishButton;
    private SwitchMaterial themeToggle;
    private ThemeManager themeManager;
    
    private String userName;
    private int score;
    private int total;
    private boolean isInitializing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize theme manager and apply saved theme
        themeManager = new ThemeManager(this);
        themeManager.applyTheme();
        
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);
        
        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.resultsContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Get data from savedInstanceState or intent
        if (savedInstanceState != null) {
            userName = savedInstanceState.getString("userName");
            score = savedInstanceState.getInt("score", 0);
            total = savedInstanceState.getInt("total", 0);
        } else {
            userName = getIntent().getStringExtra("userName");
            score = getIntent().getIntExtra("score", 0);
            total = getIntent().getIntExtra("total", 0);
        }
        
        initializeViews();
        setupThemeToggle();
        displayResults();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userName", userName);
        outState.putInt("score", score);
        outState.putInt("total", total);
    }

    private void initializeViews() {
        congratsMessage = findViewById(R.id.congratsMessage);
        scoreDisplay = findViewById(R.id.scoreDisplay);
        newQuizButton = findViewById(R.id.newQuizButton);
        finishButton = findViewById(R.id.finishButton);
        themeToggle = findViewById(R.id.resultsThemeToggle);
        
        // Set initial theme toggle state
        themeToggle.setChecked(themeManager.isDarkMode());
        
        // Mark initialization as complete
        isInitializing = false;
    }

    private void setupThemeToggle() {
        themeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isInitializing) {
                return; // Skip during initialization
            }
            if (isChecked) {
                themeManager.setTheme(ThemeManager.getDarkMode());
            } else {
                themeManager.setTheme(ThemeManager.getLightMode());
            }
            // Recreate activity to apply theme
            recreate();
        });
    }

    private void displayResults() {
        // Set congratulations message
        congratsMessage.setText(String.format(getString(R.string.congratulations), userName));
        
        // Display score
        scoreDisplay.setText(String.format(getString(R.string.score_format), score, total));
        
        // Setup button listeners
        newQuizButton.setOnClickListener(v -> startNewQuiz());
        finishButton.setOnClickListener(v -> finishApp());
    }

    private void startNewQuiz() {
        // Return to MainActivity with user name retained
        Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
        intent.putExtra("userName", userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void finishApp() {
        // Close the application
        finishAffinity();
    }
}

