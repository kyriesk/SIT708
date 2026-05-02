package com.example.llmchatbot.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.llmchatbot.model.Message;
import com.example.llmchatbot.repository.MessageRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private final MessageRepository repository;
    private LiveData<List<Message>> messages;
    private String currentUsername;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        repository = new MessageRepository(application);
    }

    public void setUsername(String username) {
        this.currentUsername = username;
        messages = repository.getMessagesByUsername(username);
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public void sendMessage(String message, MessageRepository.LLMResponseCallback callback) {
        if (currentUsername != null) {
            repository.sendMessage(message, currentUsername, callback);
        }
    }

    public void insertMessage(Message message) {
        repository.insertMessage(message);
    }

    public void clearMessages() {
        if (currentUsername != null) {
            repository.clearMessages(currentUsername);
        }
    }
}

