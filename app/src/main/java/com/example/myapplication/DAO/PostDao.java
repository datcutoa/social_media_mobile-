package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Entity.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPost(Post post); // Sửa từ void → long

    @Query("SELECT * FROM post WHERE id = :postId")
    Post getPostById(int postId);

    @Query("SELECT * FROM post")
    List<Post> getAllPosts();

    @Query("SELECT * FROM post WHERE userId = :userId")
    List<Post> getPostsByUser(int userId);

    @Query("DELETE FROM post")
    void deleteAll();

}

