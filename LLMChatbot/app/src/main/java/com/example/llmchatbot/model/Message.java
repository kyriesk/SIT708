package com.example.llmchatbot.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String content;
    public boolean isFromUser;
    public long timestamp;
    public String username;
    
    public Message(String content, boolean isFromUser, long timestamp, String username) {
        this.content = content;
        this.isFromUser = isFromUser;
        this.timestamp = timestamp;
        this.username = username;
    }
}

