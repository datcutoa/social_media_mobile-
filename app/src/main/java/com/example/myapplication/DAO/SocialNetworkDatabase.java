package com.example.myapplication.DAO;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.Emtity.User;
import com.example.myapplication.Emtity.Message;
import com.example.myapplication.Emtity.Post;
import com.example.myapplication.Emtity.Comment;
import com.example.myapplication.Emtity.Follow;
import com.example.myapplication.Emtity.Like;
import com.example.myapplication.Emtity.Notification;


@Database(
        entities = {User.class, Message.class, Post.class, Comment.class, Follow.class, Like.class, Notification.class},
        version = 2, // Tăng version
        exportSchema = false
)
public abstract class SocialNetworkDatabase extends RoomDatabase {

    private static volatile SocialNetworkDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract MessageDao messageDao();
    public abstract PostDao postDao();
    public abstract CommentDao commentDao();
    public abstract FollowDao followDao();
    public abstract LikeDao likeDao();
    public abstract NotificationDao notificationDao();

    public static SocialNetworkDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SocialNetworkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = createDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static SocialNetworkDatabase createDatabase(Context context) {
        return Room.databaseBuilder(
                        context.getApplicationContext(),
                        SocialNetworkDatabase.class,
                        "social_network"
                )
                .allowMainThreadQueries()
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        db.execSQL("PRAGMA foreign_keys=ON;");
                    }
                })
                .fallbackToDestructiveMigration() // ⚠️ Chỉ dùng trong giai đoạn dev
                .build();
    }

    public static void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}
