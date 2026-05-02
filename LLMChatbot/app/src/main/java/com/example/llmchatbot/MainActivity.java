package com.example.llmchatbot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("chat_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("current_username", null);

        if (username != null) {
            // User is logged in, go to chat
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else {
            // User is not logged in, go to login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}