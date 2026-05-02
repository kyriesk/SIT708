package com.example.llmchatbot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private Button goButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username_input);
        goButton = findViewById(R.id.go_button);

        sharedPreferences = getSharedPreferences("chat_prefs", MODE_PRIVATE);

        goButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameInput.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save username to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_username", username);
        editor.apply();

        // Navigate to ChatActivity
        Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}

