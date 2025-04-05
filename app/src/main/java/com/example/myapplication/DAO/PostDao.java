package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Emtity.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(Post post);
    @Query("DELETE FROM Post")
    void deleteAllPosts();

    @Query("SELECT * FROM post WHERE userId = :userId")
    List<Post> getPostsByUser(int userId);

    @Query("SELECT * FROM post ORDER BY id DESC")
    List<Post> getAllPosts();

    @Query("SELECT * FROM post WHERE id = :postId")
    Post getPostById(int postId);

}
