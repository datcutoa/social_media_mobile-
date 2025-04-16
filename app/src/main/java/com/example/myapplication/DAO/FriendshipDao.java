package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Entity.Friendship;

import java.util.List;

@Dao
public interface FriendshipDao {
    @Insert
    void insertFriendship(Friendship friendship);

    @Query("SELECT * FROM friendships WHERE userId = :userId AND friendId = :friendId")
    Friendship getFriendship(int userId, int friendId);

    @Query("SELECT * FROM friendships WHERE userId = :userId")
    List<Friendship> getFriendshipsOfUser(int userId);

//    OR friendId = :userId
    @Query("SELECT * FROM friendships WHERE (userId = :userId ) ")
    List<Friendship> getFriendshipsByUserIdAndStatus(int userId);

    // Xóa một friendship theo userId và friendId
    @Query("DELETE FROM friendships WHERE userId = :userId AND friendId = :friendId")
    void deleteFriendship(int userId, int friendId);

    // Xóa tất cả friendship của một user
    @Query("DELETE FROM friendships WHERE userId = :userId OR friendId = :userId")
    void deleteAllFriendshipsOfUser(int userId);

    // Xóa toàn bộ bảng friendship (nếu cần)
    @Query("DELETE FROM friendships")
    void deleteAllFriendships();


}
