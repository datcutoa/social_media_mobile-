package com.example.myapplication.ui.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Entity.Post;
import com.example.myapplication.Entity.User;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class FocusPost extends AppCompatActivity {
    private Button btnBack;
    private TextView txtUserName, txtTime, txtContent;
    private ImageView imgUserAvatar, imgPost;
    private int postId ;  // ID của bài viết cần xem chi tiết

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.focuspost);// Layout hiển thị chi tiết bài viết

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FocusPost.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Kết nối các View với layout
        txtUserName = findViewById(R.id.txtUserName);
        txtTime = findViewById(R.id.txtTime);
        txtContent = findViewById(R.id.txtContent);
        imgUserAvatar = findViewById(R.id.imgUserAvatar);
        imgPost = findViewById(R.id.imgPost);

        // Lấy ID bài viết từ Intent
        postId = getIntent().getIntExtra("post_id", -1); // Lấy ID bài viết từ Intent

        // Lấy bài viết từ cơ sở dữ liệu
        SocialNetworkDatabase db = SocialNetworkDatabase.getInstance(this);
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            Post post = db.postDao().getPostById(postId);
            User user = db.userDao().getUserById(post.getUserId());
            runOnUiThread(() -> {
                // Cập nhật UI ở đây
                if (post != null) {
                    // Điền dữ liệu vào các View
                    txtUserName.setText(""+user.getUsername());
                    txtContent.setText(post.getContent());
                    txtTime.setText(post.getCreatedAt());
                    txtTime.setText(post.getCreatedAt());  // Hiển thị thời gian đăng bài
                    txtContent.setText(post.getContent());  // Nội dung bài viết

                    // Nếu có URL ảnh người dùng, hiển thị avatar
                    Glide.with(this)
                            .load(user.getProfilePicture())  // Nếu bạn có URL ảnh
                            .centerCrop()
                            .into(imgUserAvatar);
                    // Giả sử bạn có avatar của người dùng hoặc URL khác
                    // Glide.with(this).load(avatarUrl).circleCrop().into(imgUserAvatar);
                    imgPost.setVisibility(View.VISIBLE);  // Hiển thị ảnh nếu đang bị ẩn
                    imgPost.setImageURI(Uri.parse(post.getMediaUrl()));

                }

            });
        });



    }
    
}
