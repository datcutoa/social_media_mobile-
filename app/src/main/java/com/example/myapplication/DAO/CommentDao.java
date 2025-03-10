package com.example.myapplication.DAO;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Emtity.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment comment);

    @Query("SELECT * FROM comments WHERE postId = :postId")
    List<Comment> getCommentsByPost(int postId);
}
