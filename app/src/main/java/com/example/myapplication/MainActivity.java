package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.ui.NewPost;
import com.example.myapplication.ui.ProfileActivity;
import com.example.myapplication.ui.message.MessageFragment;
import com.example.myapplication.ui.post.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private SocialNetworkDatabase db;
    private int currentUserId = 1; // Replace with dynamic user ID from login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        ImageView imgUserAvatar = findViewById(R.id.imgUserAvt);
        EditText txtText = findViewById(R.id.txtText);
        LinearLayout thanhCamXuc = findViewById(R.id.thanhcamxuc);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        // Set up Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thread");
        }

        // Initialize database
        db = SocialNetworkDatabase.getInstance(this);

        // Load user avatar
        loadUserAvatar(imgUserAvatar);

        // Set click listeners
        txtText.setOnClickListener(this::showPopup);
        thanhCamXuc.setOnClickListener(this::showPopup);
        imgUserAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("userId", currentUserId);
            startActivity(intent);
        });
        btnSearch.setOnClickListener(v -> {
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
        });

        // Set up BottomNavigationView
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new PostFragment();
            } else if (itemId == R.id.nav_messages) {
                selectedFragment = new MessageFragment();
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("userId", currentUserId);
                startActivity(intent);
                return true;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PostFragment())
                    .commit();
        }
    }

    private void showPopup(View anchorView) {
        Intent intent = new Intent(this, NewPost.class);
        intent.putExtra("userId", currentUserId);
        startActivity(intent);
    }

    private void loadUserAvatar(ImageView imgUserAvatar) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().getUserById(currentUserId);
            runOnUiThread(() -> {
                if (user != null && user.getProfilePicture() != null) {
                    Glide.with(this)
                            .load(user.getProfilePicture())
                            .circleCrop()
                            .placeholder(R.drawable.avt1)
                            .error(R.drawable.avt1)
                            .into(imgUserAvatar);
                } else {
                    Glide.with(this)
                            .load(R.drawable.avt1)
                            .circleCrop()
                            .into(imgUserAvatar);
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserAvatar(findViewById(R.id.imgUserAvt));
    }

}