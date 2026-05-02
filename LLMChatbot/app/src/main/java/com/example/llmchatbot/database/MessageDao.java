package com.example.llmchatbot.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.llmchatbot.model.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insertMessage(Message message);
    
    @Query("SELECT * FROM messages WHERE username = :username ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesByUsername(String username);
    
    @Query("DELETE FROM messages WHERE username = :username")
    void deleteMessagesByUsername(String username);
    
    @Query("SELECT COUNT(*) FROM messages WHERE username = :username")
    LiveData<Integer> getMessageCount(String username);
}

