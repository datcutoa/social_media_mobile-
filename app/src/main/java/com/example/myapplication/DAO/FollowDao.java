package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Entity.Follow;

import java.util.List;

@Dao
public interface FollowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFollow(Follow follow);

    @Query("SELECT * FROM follows WHERE followerId = :userId")
    List<Follow> getFollowing(int userId);
}
