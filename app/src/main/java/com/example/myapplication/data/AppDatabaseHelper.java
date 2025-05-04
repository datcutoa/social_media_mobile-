package com.example.myapplication.data;

import android.util.Log;

import com.example.myapplication.DAO.SocialNetworkDatabase;

import java.util.concurrent.Executors;

public class AppDatabaseHelper {
    public static void clearAllData(SocialNetworkDatabase db) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.likeDao().deleteAll();
            db.commentDao().deleteAll();
            db.notificationDao().deleteAll();
            db.friendshipDao().deleteAll();
            db.postDao().deleteAll();
            db.userDao().deleteAll();
            Log.d("DatabaseClear", "Đã xóa toàn bộ dữ liệu trong database.");
        });
    }
}

