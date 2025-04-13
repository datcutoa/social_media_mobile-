package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;
import com.example.myapplication.Emtity.Notification;
import java.util.List;

@Dao
public interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotification(Notification notification);

    @Delete
    void deleteNotification(Notification notification);

    @Query("SELECT * FROM notifications WHERE userId = :userId")
    List<Notification> getNotificationsForUser(int userId);

    @Query("SELECT * FROM notifications WHERE userId = :userId AND type = :type")
    List<Notification> getNotificationsByType(int userId, String type);

    @Query("UPDATE notifications SET readStatus = 1 WHERE userId = :userId AND readStatus = 0")
    void markNotificationsAsRead(int userId);
}