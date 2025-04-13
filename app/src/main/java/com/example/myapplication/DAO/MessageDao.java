package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Emtity.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    @Query("SELECT * FROM messages")
    List<Message> getAllMessages();

    @Query("SELECT * FROM messages WHERE receiverId = :userId")
    List<Message> getMessagesForUser(String userId);
}
