package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;
import com.example.myapplication.Emtity.Like;
import java.util.List;

@Dao
public interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLike(Like like);

    @Delete
    void deleteLike(Like like);

    @Query("SELECT * FROM likes WHERE postId = :postId")
    List<Like> getLikesByPostId(int postId);

    @Query("SELECT COUNT(*) FROM likes WHERE postId = :postId")
    int getLikeCountForPost(int postId);

    @Query("SELECT COUNT(*) FROM likes WHERE postId = :postId AND userId = :userId")
    int doesUserLikePost(int postId, int userId);


}