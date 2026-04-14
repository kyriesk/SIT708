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

public class SignupActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private EditText editTextFullName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private AppDatabase database;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        database = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUsername = findViewById(R.id.editTextSignupUsername);
        editTextPassword = findViewById(R.id.editTextSignupPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        TextView textGoToLogin = findViewById(R.id.textGoToLogin);

        buttonCreateAccount.setOnClickListener(v -> signup());
        textGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void signup() {
        String fullName = editTextFullName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)
                || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, R.string.error_fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, R.string.error_password_mismatch, Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            if (database.userDao().findByUsername(username) != null) {
                runOnUiThread(() -> Toast.makeText(this, R.string.error_username_taken, Toast.LENGTH_SHORT).show());
                return;
            }

            long id = database.userDao().insert(new User(fullName, username, password));
            runOnUiThread(() -> {
                sessionManager.saveLogin(id, username);
                Toast.makeText(this, R.string.account_created, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
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

