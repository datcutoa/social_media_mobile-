package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Emtity.Post;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private SocialNetworkDatabase database;
    private ImageView profileImage, coverPhoto;
    private TextView txtUsername, txtBio;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize views
        profileImage = findViewById(R.id.profileImage);
        coverPhoto = findViewById(R.id.coverPhoto);
        txtUsername = findViewById(R.id.txtUsername);
        txtBio = findViewById(R.id.txtBio);
        ImageButton btnReturn = findViewById(R.id.btnReturn);
        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        recyclerView = findViewById(R.id.recyclerPosts);

        // Get userId from Intent (default to 1 if not provided)
        userId = getIntent().getIntExtra("userId", 1);

        // Initialize database
        database = SocialNetworkDatabase.getInstance(this);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, database);
        recyclerView.setAdapter(postAdapter);

        // Load user profile and posts
        loadUserProfile(userId);
        loadUserPosts(userId);

        // Return button
        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Edit profile button (placeholder for future implementation)
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profile feature coming soon!", Toast.LENGTH_SHORT).show();
            // Future: Intent to EditProfileActivity or show dialog
        });
    }
//truyền vào id user khi đăng nhập

    private void loadUserProfile(int userId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                User user = database.userDao().getUserById(userId);
                runOnUiThread(() -> {
                    if (user != null) {
                        txtUsername.setText(user.getUsername());
                        txtBio.setText(user.getBio() != null && !user.getBio().isEmpty() ? user.getBio() : "No bio available");
                        Glide.with(this)
                                .load(user.getProfilePicture())
                                .apply(new RequestOptions().placeholder(R.drawable.avt1).error(R.drawable.avt1))
                                .circleCrop()
                                .into(profileImage);
                        // Load cover photo (assuming User has coverPhoto field)
                        Glide.with(this)
                                .load(user.getCoverPhoto() != null && !user.getCoverPhoto().isEmpty() ? user.getCoverPhoto() : R.drawable.avt1)
                                .apply(new RequestOptions().placeholder(R.drawable.avt1).error(R.drawable.avt1))
                                .centerCrop()
                                .into(coverPhoto);
                    } else {
                        txtUsername.setText("User not found");
                        txtBio.setText("");
                        Glide.with(this)
                                .load(R.drawable.avt1)
                                .circleCrop()
                                .into(profileImage);
                        Glide.with(this)
                                .load(R.drawable.avt1)
                                .centerCrop()
                                .into(coverPhoto);
                        Toast.makeText(this, "User not found in database", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void loadUserPosts(int userId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Post> posts = database.postDao().getPostsByUserId(userId);
                runOnUiThread(() -> {
                    postList.clear();
                    if (posts != null && !posts.isEmpty()) {
                        postList.addAll(posts);
                    } else {
                        Toast.makeText(this, "No posts found for this user", Toast.LENGTH_SHORT).show();
                    }
                    postAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserPosts(userId);
    }
}