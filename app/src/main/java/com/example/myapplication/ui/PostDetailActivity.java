package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Emtity.Comment;
import com.example.myapplication.Emtity.Post;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView imgAvatar, imgPost;
    private TextView txtUserName, txtTime, txtContent, txtLikeCount, txtCommentCount;
    private RecyclerView recyclerComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private SocialNetworkDatabase db;
    private int postId; // Dynamic post ID from Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);

        // Initialize views
        imgAvatar = findViewById(R.id.imgUserAvatar);
        imgPost = findViewById(R.id.imgPost);
        txtUserName = findViewById(R.id.txtUserName);
        txtTime = findViewById(R.id.txtTime);
        txtContent = findViewById(R.id.txtContent);
        txtLikeCount = findViewById(R.id.txtLikeCount);
        txtCommentCount = findViewById(R.id.txtCommentCount);
        recyclerComments = findViewById(R.id.recyclerComments);

        // Get postId from Intent
        postId = getIntent().getIntExtra("postId", -1);
        if (postId == -1) {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize database
        db = SocialNetworkDatabase.getInstance(this);

        // Setup comments RecyclerView
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerComments.setAdapter(commentAdapter);

        // Load post and comments
        loadPostData(postId);
        loadComments(postId);
    }

    private void loadPostData(int postId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Post post = db.postDao().getPostById(postId);
                User user = post != null ? db.userDao().getUserById(post.getUserId()) : null;
                runOnUiThread(() -> {
                    if (post != null && user != null) {
                        txtUserName.setText(user.getUsername());
                        txtTime.setText(post.getCreatedAt());
                        txtContent.setText(post.getContent());
                        txtLikeCount.setText(String.valueOf(post.getLikeCount()));
                        Glide.with(this)
                                .load(user.getProfilePicture())
                                .apply(new RequestOptions().placeholder(R.drawable.avt1).error(R.drawable.avt1))
                                .circleCrop()
                                .into(imgAvatar);
                        if (post.getMediaUrl() != null && !post.getMediaUrl().isEmpty()) {
                            Glide.with(this)
                                    .load(post.getMediaUrl())
                                    .apply(new RequestOptions().placeholder(R.drawable.avt1).error(R.drawable.avt1))
                                    .into(imgPost);
                            imgPost.setVisibility(View.VISIBLE);
                        } else {
                            imgPost.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(this, "Post or user not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void loadComments(int postId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Comment> comments = db.commentDao().getCommentsByPost(postId);
                runOnUiThread(() -> {
                    commentList.clear();
                    if (comments != null && !comments.isEmpty()) {
                        commentList.addAll(comments);
                        txtCommentCount.setText(String.valueOf(comments.size()));
                    } else {
                        txtCommentCount.setText("0");
                        Toast.makeText(this, "No comments found", Toast.LENGTH_SHORT).show();
                    }
                    commentAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading comments: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // Nested Comment Adapter
    private class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
        private List<Comment> comments;

        public CommentAdapter(List<Comment> comments) {
            this.comments = comments;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new CommentViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            Comment comment = comments.get(position);
            holder.text1.setText(comment.getContent());
            holder.text2.setText(comment.getCreatedAt());
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        class CommentViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}