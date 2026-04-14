package com.example.istream.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.istream.data.entity.PlaylistItem;

import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert
    long insert(PlaylistItem item);

    @Query("SELECT * FROM playlist_items WHERE user_id = :userId ORDER BY created_at DESC")
    List<PlaylistItem> getByUserId(long userId);
}

