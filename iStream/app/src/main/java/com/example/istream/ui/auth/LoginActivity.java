package com.example.istream.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.istream.R;
import com.example.istream.data.db.AppDatabase;
import com.example.istream.data.entity.User;
import com.example.istream.session.SessionManager;
import com.example.istream.ui.home.HomeActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private EditText editTextUsername;
    private EditText editTextPassword;
    private SessionManager sessionManager;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        database = AppDatabase.getInstance(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView textSignUp = findViewById(R.id.textGoToSignup);

        buttonLogin.setOnClickListener(v -> login());
        textSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.error_fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            User user = database.userDao().login(username, password);
            runOnUiThread(() -> {
                if (user == null) {
                    Toast.makeText(this, R.string.error_invalid_credentials, Toast.LENGTH_SHORT).show();
                    return;
                }

                sessionManager.saveLogin(user.getId(), user.getUsername());
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}

