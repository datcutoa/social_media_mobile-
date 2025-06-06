package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Entity.Like;

import java.util.List;

@Dao
public interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLike(Like like);

    @Query("SELECT * FROM likes WHERE postId = :postId")
    List<Like> getLikesByPost(int postId);

    @Query("DELETE FROM likes")
    void deleteAll();

}
