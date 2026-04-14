package com.example.istream;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.istream.session.SessionManager;
import com.example.istream.ui.auth.LoginActivity;
import com.example.istream.ui.home.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager sessionManager = new SessionManager(this);
        Intent intent = sessionManager.isLoggedIn()
                ? new Intent(this, HomeActivity.class)
                : new Intent(this, LoginActivity.class);

        startActivity(intent);
        finish();
    }
}