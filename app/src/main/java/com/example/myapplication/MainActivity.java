package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Entity.Friendship;
import com.example.myapplication.Entity.User;
import com.example.myapplication.ui.message.MessageFragment;
import com.example.myapplication.ui.post.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    private SocialNetworkDatabase database;
    private int currentUserId;
    private ConstraintLayout mainContentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        logout();

        mainContentLayout = findViewById(R.id.mainContentLayout);


        // Lấy userId từ Intent
        currentUserId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getInt("userId", -1);
//        try {
            database = SocialNetworkDatabase.getInstance(this);
            Log.d("MainActivity", "Database instance ok");
//            createTestFriendshipRoom(database);
//            Log.d("MainActivity", "createTestFriendshipRoom ok");
//
            fetchUserInfo(database);
//        } catch (Exception e) {
//            Log.d("MainActivity","Lỗi database: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//
//
//        Log.d("MainActivity","Đã tạo friendship");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                openPostFragment();
                return true;

            } else if (itemId == R.id.nav_profile) {
                // Hiển thị profile fragment
                return true;

            } else if (itemId == R.id.nav_messages) {
                if (currentUserId != -1) {
                    Log.e("MainActivity", "qua MessageFragment với currentUserId:" +currentUserId);

                    openMessageFragment(currentUserId);
                } else {
                    Log.e("MainActivity", "qua MessageFragment thất bai");

                    Intent intent = new Intent(MainActivity.this, com.example.myapplication.ui.login.Login.class);
                    startActivity(intent);
                    finish();
                }
                return true;

            } else if (itemId == R.id.navigation_notifications) {
                // Hiển thị notifications fragment
                return true;
            }

            return false;
        });


    }
    public void openPostFragment() {
        Log.e("MainActivity", "Mở PostFragment");

        // Hiện lại layout chính
        mainContentLayout.setVisibility(View.VISIBLE);

        PostFragment fragment = new PostFragment();


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    public void openMessageFragment(int currentUserId) {
        Log.e("MainActivity", "Mở MessageFragment với currentUserId:" + currentUserId);

        // Ẩn layout chính khi vào message
        mainContentLayout.setVisibility(View.GONE);

        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt("currentUserId", currentUserId);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

//    hàm logout
private void logout() {
        Log.d("MainActivity","logout user id"+currentUserId);
    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.remove("userId");
    editor.apply();

}


private void fetchUserInfo(SocialNetworkDatabase db) {
    Executors.newSingleThreadExecutor().execute(() -> {
        List<User> allUsers = db.userDao().getAllUsers();
        for (User u : allUsers) {
            Log.d("MainActivity", "User: " + u.getId() + " - " + u.getUsername());
        }

    });
}



    private void createTestFriendshipRoom(SocialNetworkDatabase db) {
        Executors.newSingleThreadExecutor().execute(() -> {
            int user1Id = 1;
            User user1 = new User(
                    user1Id,
                    "user1",
                    "password123",
                    "user1@example.com",
                    "User One",
                    "This is User 1's bio",
                    "default_cover.jpg",
                    "default_avatar.jpg",
                    "male",
                    "2000-01-01",
                    "2025-04-14"
            );

            db.userDao().insertUser(user1);
            int user2Id = 2;
            User user2 = new User(
                    user2Id,
                    "user2",
                    "password123",
                    "user2@example.com",
                    "User Two",
                    "This is User 2's bio",
                    "default_cover.jpg",
                    "default_avatar.jpg",
                    "male",
                    "2000-01-01",
                    "2025-04-14"
            );

            db.userDao().insertUser(user2);

            String createdAt = "2025-04-14";
            Friendship friendship1 = new Friendship(1, 2, "accepted", createdAt);
            Friendship friendship2 = new Friendship(2, 1, "accepted", createdAt);

            db.friendshipDao().insertFriendship(friendship1);
            db.friendshipDao().insertFriendship(friendship2);

            Log.d("TestFriendship", "Đã thêm user2 và kết bạn với user1");
        });
    }




}
