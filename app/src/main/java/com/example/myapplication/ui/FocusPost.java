package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Emtity.Comment;
import com.example.myapplication.Emtity.Post;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class FocusPost extends AppCompatActivity {

    private ImageView userAvatar, postImage, btnLike;
    private TextView userName, time, content, likeCount, commentCount;
    private ImageButton btnBack;
    private EditText edtCommentInput;
    private Button btnSubmitComment;
    private RecyclerView recyclerComments;
    private CommentAdapter commentAdapter;
    private int userId, postId;
    private SocialNetworkDatabase db;
    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.focuspost);

        // Initialize views
        userAvatar = findViewById(R.id.imgUserAvatar);
        postImage = findViewById(R.id.imgPost);
        btnLike = findViewById(R.id.btnLike);
        userName = findViewById(R.id.txtUserName);
        time = findViewById(R.id.txtTime);
        content = findViewById(R.id.txtContent);
        likeCount = findViewById(R.id.txtLikeCount);
        commentCount = findViewById(R.id.txtCommentCount);
        btnBack = findViewById(R.id.btnBack);
        recyclerComments = findViewById(R.id.recyclerComments);
        edtCommentInput = findViewById(R.id.edtCommentInput);
        btnSubmitComment = findViewById(R.id.btnSubmitComment);

        // Get data from Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        postId = intent.getIntExtra("postId", -1);

        // Initialize database
        db = SocialNetworkDatabase.getInstance(this);

        // Setup RecyclerView for comments
        recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, null, db);
        recyclerComments.setAdapter(commentAdapter);

        // Load post and comments data
        loadPostData();
        loadComments();

        // Setup buttons
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(FocusPost.this, MainActivity.class);
            startActivity(backIntent);
        });

        setupLikeButton();
        setupCommentSubmission();
    }

    private void loadPostData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().getUserById(userId);
            Post post = db.postDao().getPostById(postId);
            int commentCountValue = db.commentDao().getCommentsByPost(postId).size();

            runOnUiThread(() -> {
                if (user != null) {
                    userName.setText(user.getUsername());
                    Glide.with(this)
                            .load(user.getProfilePicture())
                            .apply(new RequestOptions().placeholder(R.drawable.avt1).error(R.drawable.avt1))
                            .circleCrop()
                            .into(userAvatar);
                }
                if (post != null) {
                    time.setText(post.getCreatedAt());
                    content.setText(post.getContent());
                    likeCount.setText(String.valueOf(post.getLikeCount()));
                    commentCount.setText(String.valueOf(commentCountValue));
                    if (post.getMediaUrl() != null && !post.getMediaUrl().isEmpty()) {
                        Glide.with(this)
                                .load(post.getMediaUrl())
                                .apply(new RequestOptions().placeholder(R.drawable.avt1).error(R.drawable.avt1))
                                .into(postImage);
                        postImage.setVisibility(View.VISIBLE);
                    } else {
                        postImage.setVisibility(View.GONE);
                    }
                }
            });
        });
    }

    private void loadComments() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Comment> comments = db.commentDao().getCommentsByPost(postId);
            runOnUiThread(() -> {
                commentAdapter.updateComments(comments);
                commentCount.setText(String.valueOf(comments.size()));
            });
        });
    }

    private void setupLikeButton() {
        btnLike.setColorFilter(ContextCompat.getColor(this, isLiked ? android.R.color.holo_red_light : android.R.color.darker_gray));
        btnLike.setOnClickListener(v -> {
            Post post = db.postDao().getPostById(postId);
            int newLikeCount = post.getLikeCount();
            if (isLiked) {
                newLikeCount--;
                isLiked = false;
                btnLike.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray));
            } else {
                newLikeCount++;
                isLiked = true;
                btnLike.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_light));
            }
            post.setLikeCount(newLikeCount);
            likeCount.setText(String.valueOf(newLikeCount));
            updateLikeCountInDatabase(post);
        });
    }

    private void setupCommentSubmission() {
        btnSubmitComment.setOnClickListener(v -> {
            String commentText = edtCommentInput.getText().toString().trim();
            if (!commentText.isEmpty()) {
                // Create a new comment
                String currentTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
                Comment newComment = new Comment(postId, userId, commentText, currentTime);

                // Insert the comment into the database
                Executors.newSingleThreadExecutor().execute(() -> {
                    db.commentDao().insertComment(newComment);
                    // Reload comments after insertion
                    runOnUiThread(() -> {
                        loadComments();
                        edtCommentInput.setText(""); // Clear the input field
                    });
                });
            }
        });
    }

    private void updateLikeCountInDatabase(Post post) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.postDao().updatePost(post);
        });
    }

    static class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

        private List<Comment> commentList;
        private Context context;
        private SocialNetworkDatabase db;

        public CommentAdapter(Context context, List<Comment> commentList, SocialNetworkDatabase db) {
            this.context = context;
            this.commentList = commentList;
            this.db = db;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            Comment comment = commentList.get(position);
            holder.txtCommentContent.setText(comment.getContent());
            holder.txtCommentTime.setText(comment.getCreatedAt());

            // Load user info for the comment
            Executors.newSingleThreadExecutor().execute(() -> {
                User user = db.userDao().getUserById(comment.getUserId());
                holder.itemView.post(() -> {
                    if (user != null) {
                        holder.txtCommentUserName.setText(user.getUsername());
                        Glide.with(context)
                                .load(user.getProfilePicture())
                                .apply(new RequestOptions().placeholder(R.drawable.avt1).error(R.drawable.avt1))
                                .circleCrop()
                                .into(holder.imgCommentUserAvatar);
                    }
                });
            });
        }

        @Override
        public int getItemCount() {
            return commentList != null ? commentList.size() : 0;
        }

        public void updateComments(List<Comment> newCommentList) {
            this.commentList = newCommentList;
            notifyDataSetChanged();
        }

        static class CommentViewHolder extends RecyclerView.ViewHolder {
            ImageView imgCommentUserAvatar;
            TextView txtCommentUserName, txtCommentContent, txtCommentTime;

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                imgCommentUserAvatar = itemView.findViewById(R.id.imgCommentUserAvatar);
                txtCommentUserName = itemView.findViewById(R.id.txtCommentUserName);
                txtCommentContent = itemView.findViewById(R.id.txtCommentContent);
                txtCommentTime = itemView.findViewById(R.id.txtCommentTime);
            }
        }
    }
}