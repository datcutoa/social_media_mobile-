package com.example.myapplication.DAO;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.Entity.*;

@Database(entities = {
        User.class, Post.class, Comment.class, Like.class, Follow.class,
        Message.class, Notification.class,Friendship.class
}, version = 2)
public abstract class SocialNetworkDatabase extends RoomDatabase {

    private static volatile SocialNetworkDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract FriendshipDao friendshipDao();
    public abstract PostDao postDao();
    public abstract CommentDao commentDao();
    public abstract LikeDao likeDao();
    public abstract FollowDao followDao();
    public abstract MessageDao messageDao();
    public abstract NotificationDao notificationDao();

    public static SocialNetworkDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SocialNetworkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    SocialNetworkDatabase.class, "social_network")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
