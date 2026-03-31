package com.example.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.model.Question;
import com.example.quizapp.model.Quiz;
import com.example.quizapp.utils.ThemeManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class QuizActivity extends AppCompatActivity {
    private Quiz quiz;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private String userName;
    
    private TextView questionText;
    private TextView questionCounter;
    private TextView progressText;
    private ProgressBar progressBar;
    private LinearLayout optionsContainer;
    private Button submitButton;
    private SwitchMaterial themeToggle;
    private ThemeManager themeManager;
    
    private boolean answerSubmitted = false;
    private int selectedAnswer = -1;
    private MaterialButton[] answerButtons;
    private boolean isInitializing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize theme manager and apply saved theme
        themeManager = new ThemeManager(this);
        themeManager.applyTheme();
        
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        
        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.quizContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Get user name from Intent or savedInstanceState
        if (savedInstanceState != null) {
            userName = savedInstanceState.getString("userName");
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex", 0);
            correctAnswers = savedInstanceState.getInt("correctAnswers", 0);
            answerSubmitted = savedInstanceState.getBoolean("answerSubmitted", false);
            selectedAnswer = savedInstanceState.getInt("selectedAnswer", -1);
        } else {
            userName = getIntent().getStringExtra("userName");
            currentQuestionIndex = 0;
            correctAnswers = 0;
        }
        
        // Initialize quiz
        quiz = new Quiz();
        
        initializeViews();
        setupThemeToggle();
        loadQuestion();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userName", userName);
        outState.putInt("currentQuestionIndex", currentQuestionIndex);
        outState.putInt("correctAnswers", correctAnswers);
        outState.putBoolean("answerSubmitted", answerSubmitted);
        outState.putInt("selectedAnswer", selectedAnswer);
    }

    private void initializeViews() {
        questionText = findViewById(R.id.questionText);
        questionCounter = findViewById(R.id.questionCounter);
        progressText = findViewById(R.id.progressText);
        progressBar = findViewById(R.id.progressBar);
        optionsContainer = findViewById(R.id.optionsContainer);
        submitButton = findViewById(R.id.submitButton);
        themeToggle = findViewById(R.id.quizThemeToggle);
        
        // Set initial theme toggle state
        themeToggle.setChecked(themeManager.isDarkMode());
        
        submitButton.setOnClickListener(v -> onSubmitClicked());
        
        // Mark initialization as complete
        isInitializing = false;
    }

    private void setupThemeToggle() {
        themeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isInitializing) {
                return;
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

    private void loadQuestion() {
        if (currentQuestionIndex >= quiz.getTotalQuestions()) {
            // Quiz completed
            showResults();
            return;
        }
        
        Question question = quiz.getQuestion(currentQuestionIndex);
        if (question == null) {
            return;
        }
        
        // Update progress
        int progress = (int) ((currentQuestionIndex + 1) * 100.0 / quiz.getTotalQuestions());
        progressBar.setProgress(progress);
        progressText.setText(String.format(getString(R.string.progress_format), 
                currentQuestionIndex + 1, quiz.getTotalQuestions()));
        
        // Update question counter
        questionCounter.setText(String.format(getString(R.string.question_format), 
                currentQuestionIndex + 1, quiz.getTotalQuestions()));
        
        // Display question
        questionText.setText(question.getQuestionText());
        
        // Clear and rebuild options
        optionsContainer.removeAllViews();
        String[] options = question.getOptions();
        
        answerButtons = new MaterialButton[options.length];
        
        for (int i = 0; i < options.length; i++) {
            MaterialButton button = new MaterialButton(this);
            button.setText(options[i]);
            button.setId(i);
            button.setAllCaps(false);
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            button.setTextColor(ContextCompat.getColor(this, R.color.black));
            button.setStrokeWidth(2);
            button.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            final int buttonIndex = i;
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 8, 0, 8);
            button.setLayoutParams(params);
            
            button.setOnClickListener(v -> selectAnswer(buttonIndex));
            
            answerButtons[i] = button;
            optionsContainer.addView(button);
        }
        
        // If answer was already submitted, restore the feedback
        if (answerSubmitted) {
            restoreAnswerFeedback(question);
            submitButton.setText(R.string.next_question);
        } else {
            submitButton.setText(R.string.submit_answer);
        }
    }
    
    private void restoreAnswerFeedback(Question question) {
        boolean isCorrect = selectedAnswer == question.getCorrectAnswerIndex();
        
        for (int i = 0; i < answerButtons.length; i++) {
            if (i == question.getCorrectAnswerIndex()) {
                // Correct answer - green background
                answerButtons[i].setBackgroundColor(ContextCompat.getColor(this, R.color.correct_answer));
                answerButtons[i].setTextColor(Color.WHITE);
                answerButtons[i].setStrokeWidth(0);
            } else if (i == selectedAnswer && !isCorrect) {
                // Incorrect answer - red background
                answerButtons[i].setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect_answer));
                answerButtons[i].setTextColor(Color.WHITE);
                answerButtons[i].setStrokeWidth(0);
            } else {
                // Other incorrect answers - keep as normal
                answerButtons[i].setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                answerButtons[i].setTextColor(ContextCompat.getColor(this, R.color.black));
                answerButtons[i].setStrokeWidth(2);
                answerButtons[i].setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            }
            
            // Disable all buttons
            answerButtons[i].setEnabled(false);
        }
    }

    private void selectAnswer(int index) {
        if (answerSubmitted) {
            return; // Prevent changing answer after submission
        }
        
        selectedAnswer = index;
        
        for (int i = 0; i < answerButtons.length; i++) {
            if (i == index) {
                answerButtons[i].setStrokeWidth(4);
                answerButtons[i].setTextColor(ContextCompat.getColor(this, R.color.button_pressed));
                answerButtons[i].setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.button_pressed)));
            } else {
                answerButtons[i].setStrokeWidth(2);
                answerButtons[i].setTextColor(ContextCompat.getColor(this, R.color.black));
                answerButtons[i].setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            }
        }
    }

    private void onSubmitClicked() {
        if (answerSubmitted) {
            // Move to next question
            currentQuestionIndex++;
            loadQuestion();
            return;
        }
        
        if (selectedAnswer == -1) {
            Toast.makeText(this, R.string.please_select_answer, Toast.LENGTH_SHORT).show();
            return;
        }
        
        Question question = quiz.getQuestion(currentQuestionIndex);
        
        // Check if answer is correct
        boolean isCorrect = selectedAnswer == question.getCorrectAnswerIndex();
        if (isCorrect) {
            correctAnswers++;
        }
        
        // Highlight answers with colors after submission
        for (int i = 0; i < answerButtons.length; i++) {
            if (i == question.getCorrectAnswerIndex()) {
                // Correct answer - green background
                answerButtons[i].setBackgroundColor(ContextCompat.getColor(this, R.color.correct_answer));
                answerButtons[i].setTextColor(Color.WHITE);
                answerButtons[i].setStrokeWidth(0);
            } else if (i == selectedAnswer && !isCorrect) {
                // Incorrect answer - red background
                answerButtons[i].setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect_answer));
                answerButtons[i].setTextColor(Color.WHITE);
                answerButtons[i].setStrokeWidth(0);
            } else {
                // Other incorrect answers - keep as normal white background with black text
                answerButtons[i].setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                answerButtons[i].setTextColor(ContextCompat.getColor(this, R.color.black));
                answerButtons[i].setStrokeWidth(2);
                answerButtons[i].setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            }
            
            // Disable all buttons
            answerButtons[i].setEnabled(false);
        }
        
        answerSubmitted = true;
        submitButton.setText(R.string.next_question);
    }

    private void showResults() {
        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("score", correctAnswers);
        intent.putExtra("total", quiz.getTotalQuestions());
        startActivity(intent);
        finish();
    }
}




