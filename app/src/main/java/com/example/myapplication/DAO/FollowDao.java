package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;
import com.example.myapplication.Emtity.Follow;
import java.util.List;

@Dao
public interface FollowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFollow(Follow follow);

    @Delete
    void deleteFollow(Follow follow);

    @Query("SELECT * FROM follows WHERE followerId = :userId")
    List<Follow> getFollowsByFollowerId(int userId);

    @Query("SELECT * FROM follows WHERE followerId = :userId")
    List<Follow> getFollowersByUserId(int userId);

    @Query("SELECT COUNT(*) FROM follows WHERE followerId = :followerId AND followerId = :followedId")
    int doesUserFollow(int followerId, int followedId);

    @Query("SELECT COUNT(*) FROM follows WHERE followerId = :userId")
    int getFollowingCount(int userId);

    @Query("SELECT COUNT(*) FROM follows WHERE followerId = :userId")
    int getFollowerCount(int userId);


}