package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import com.example.myapplication.Emtity.Post;
import java.util.List;

@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(Post post);
    @Query("SELECT * FROM post WHERE id = :id")
    Post getPostById(int id);

    @Update
    void updatePost(Post post);

    @Delete
    void deletePost(Post post);

    @Query("SELECT * FROM post ")
    List<Post> getAllPosts();

    @Query("SELECT * FROM post WHERE userId = :userId")
    List<Post> getPostsByUserId(int userId);

    @Query("SELECT * FROM post WHERE privacy = :privacy")
    List<Post> getPostsByPrivacy(String privacy);

    @Query("SELECT * FROM post")
    List<Post> getAllPosts();

}