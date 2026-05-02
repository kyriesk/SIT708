package com.example.llmchatbot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llmchatbot.adapter.MessageAdapter;
import com.example.llmchatbot.model.Message;
import com.example.llmchatbot.repository.MessageRepository;
import com.example.llmchatbot.viewmodel.ChatViewModel;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private EditText messageInput;
    private ImageButton sendButton;
    private Button logoutButton;
    private RecyclerView messagesRecycler;
    private ChatViewModel viewModel;
    private MessageAdapter adapter;
    private String currentUsername;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get username from intent
        currentUsername = getIntent().getStringExtra("username");
        if (currentUsername == null) {
            finish();
            return;
        }

        initializeViews();
        initializeViewModel();
        setupRecyclerView();
        setupListeners();
    }

    private void initializeViews() {
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        logoutButton = findViewById(R.id.logout_button);
        messagesRecycler = findViewById(R.id.messages_recycler);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.setUsername(currentUsername);

        // Observe messages
        viewModel.getMessages().observe(this, messages -> {
            if (adapter != null) {
                adapter.updateMessages(messages);
                // Scroll to bottom
                if (messages.size() > 0) {
                    messagesRecycler.scrollToPosition(messages.size() - 1);
                }
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new MessageAdapter(new ArrayList<>(), currentUsername);
        messagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        messagesRecycler.setAdapter(adapter);
    }

    private void setupListeners() {
        sendButton.setOnClickListener(v -> sendMessage());

        // Allow sending with Enter key
        messageInput.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });

        // Logout button - goes back to login screen
        logoutButton.setOnClickListener(v -> logout());

        // Long-press logout button to clear data (for testing)
        logoutButton.setOnLongClickListener(v -> {
            clearCurrentUserData();
            return true;
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();

        if (messageText.isEmpty() || isLoading) {
            return;
        }

        isLoading = true;
        messageInput.setText("");

        // Send message to LLM
        viewModel.sendMessage(messageText, new MessageRepository.LLMResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    isLoading = false;
                    // Messages are already in the database and will update via LiveData
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    isLoading = false;
                    Toast.makeText(ChatActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();

                    // Add error message to chat
                    Message errorMsg = new Message(error, false, System.currentTimeMillis(), currentUsername);
                    viewModel.insertMessage(errorMsg);
                });
            }
        });
    }

    private void logout() {
        // Clear the stored username to return to login screen
        SharedPreferences sharedPreferences = getSharedPreferences("chat_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("current_username");
        editor.apply();

        // Navigate back to login
        Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // NEW METHOD: Clear all messages for current user (for testing)
    // To use: Call this from a menu option or long-press on logout button
    private void clearCurrentUserData() {
        viewModel.clearMessages();
        Toast.makeText(this, "Messages cleared for this user", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        // When back button is pressed, logout and go to login screen
        logout();
    }
}
