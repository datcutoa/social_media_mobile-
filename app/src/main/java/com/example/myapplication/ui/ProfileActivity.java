package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Emtity.Post;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.post.PostAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private SocialNetworkDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ImageView imgavt= findViewById(R.id.profileImage);
        Button btnReturn = findViewById(R.id.btnReturn);
        Glide.with(this)
                .load(R.drawable.avt1)  // URL ảnh hoặc resource nội bộ
                .circleCrop()  // Tự động cắt ảnh thành hình tròn
                .into(imgavt);
        recyclerView = findViewById(R.id.recyclerPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo database
        database = SocialNetworkDatabase.getInstance(this);

        // Khởi tạo danh sách & adapter trước khi load dữ liệu
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, database);
        recyclerView.setAdapter(postAdapter);

        // Thêm dữ liệu mẫu vào database
        addSamplePosts();
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    private void addSamplePosts() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Thêm bài post mẫu vào database
            Post post1 = new Post(1, "Bài viết đầu tiên!", "", "public", "2025-03-17 10:00:00");
            Post post2 = new Post(2, "Chào mọi người, đây là bài viết thứ hai.", "https://example.com/image.jpg", "friends", "2025-03-17 14:30:00");

            database.postDao().insertPost(post1);
            database.postDao().insertPost(post2);

            // Lấy danh sách bài viết từ database
            List<Post> posts = database.postDao().getAllPosts();
            if (posts == null) {
                posts = new ArrayList<>();
            }

            // Cập nhật UI
            List<Post> finalPosts = posts;
            runOnUiThread(() -> {
                postList.clear();
                postList.addAll(finalPosts);
                postAdapter.notifyDataSetChanged();
            });
        });
    }
}
