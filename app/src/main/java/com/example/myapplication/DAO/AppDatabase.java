package com.example.myapplication.DAO;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.example.myapplication.DAO.UserDao;
import com.example.myapplication.DAO.MessageDao;
import com.example.myapplication.DAO.PostDao;
import com.example.myapplication.DAO.CommentDao;
import com.example.myapplication.DAO.FollowDao;
import com.example.myapplication.DAO.LikeDao;
import com.example.myapplication.DAO.NotificationDao;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.Emtity.Message;
import com.example.myapplication.Emtity.Post;
import com.example.myapplication.Emtity.Comment;
import com.example.myapplication.Emtity.Follow;
import com.example.myapplication.Emtity.Like;
import com.example.myapplication.Emtity.Notification;

@Database(entities = {User.class, Message.class, Post.class, Comment.class, Follow.class, Like.class, Notification.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract MessageDao messageDao();
    public abstract PostDao postDao();
    public abstract CommentDao commentDao();
    public abstract FollowDao followDao();
    public abstract LikeDao likeDao();
    public abstract NotificationDao notificationDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "socialNetwork.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}