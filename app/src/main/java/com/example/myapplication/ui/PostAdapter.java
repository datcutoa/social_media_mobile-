package com.example.myapplication.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;
    private SocialNetworkDatabase db;
    private Map<Integer, Boolean> likeStates;

    public PostAdapter(List<Post> postList, SocialNetworkDatabase database) {
        this.postList = postList != null ? postList : new ArrayList<>();
        this.db = database;
        this.likeStates = new HashMap<>();
        for (Post post : this.postList) {
            likeStates.put(post.getId(), false);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        loadUserData(post.getUserId(), holder);
        loadPostContent(post, holder);
        loadCommentCount(post.getId(), holder);
        setupLikeButton(post, holder);
        setupCommentButton(post, holder);

        holder.itemPost.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), FocusPost.class);
            intent.putExtra("userId", post.getUserId());
            intent.putExtra("postId", post.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    private void loadUserData(int userId, PostViewHolder holder) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().getUserById(userId);
            holder.itemView.post(() -> {
                if (user != null) {
                    holder.userName.setText(user.getUsername());
                    Glide.with(holder.itemView.getContext())
                            .load(user.getProfilePicture())
                            .apply(new RequestOptions().placeholder(R.drawable.avt1).error(R.drawable.avt1))
                            .circleCrop()
                            .into(holder.userAvatar);
                } else {
                    holder.userName.setText("Unknown User");
                }
            });
        });
    }

    private void loadPostContent(Post post, PostViewHolder holder) {
        holder.time.setText(post.getCreatedAt());
        holder.content.setText(post.getContent());
        if (post.getMediaUrl() != null && !post.getMediaUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getMediaUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.avt1).error(R.drawable.avt1))
                    .into(holder.postImage);
            holder.postImage.setVisibility(View.VISIBLE);
        } else {
            holder.postImage.setVisibility(View.GONE);
        }
    }

    private void loadCommentCount(int postId, PostViewHolder holder) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Comment> comments = db.commentDao().getCommentsByPost(postId);
            holder.itemView.post(() -> {
                holder.commentCount.setText(String.valueOf(comments != null ? comments.size() : 0));
            });
        });
    }

    private void loadComments(int postId, PostViewHolder holder) {
        // Ensure the RecyclerView has a LayoutManager (set it once, not repeatedly)
        if (holder.recyclerComments.getLayoutManager() == null) {
            holder.recyclerComments.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        }

        // Check if the adapter is already set; if not, create a new one
        if (holder.recyclerComments.getAdapter() == null) {
            FocusPost.CommentAdapter commentAdapter = new FocusPost.CommentAdapter(
                    holder.itemView.getContext(),
                    null, // Initially set to null, will update later
                    SocialNetworkDatabase.getInstance(holder.itemView.getContext())
            );
            holder.recyclerComments.setAdapter(commentAdapter);
        }

        // Load comments and update the adapter
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Comment> comments = db.commentDao().getCommentsByPost(postId);
            holder.itemView.post(() -> {
                FocusPost.CommentAdapter adapter = (FocusPost.CommentAdapter) holder.recyclerComments.getAdapter();
                adapter.updateComments(comments); // Update the adapter's data
                holder.commentsContainer.setVisibility(View.VISIBLE);
            });
        });
    }

    private void setupLikeButton(Post post, PostViewHolder holder) {
        holder.likeCount.setText(String.valueOf(post.getLikeCount()));
        boolean isLiked = likeStates.getOrDefault(post.getId(), false);
        holder.btnLike.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(),
                isLiked ? android.R.color.holo_red_light : android.R.color.darker_gray));

        holder.btnLike.setOnClickListener(v -> {
            boolean currentLikeState = likeStates.getOrDefault(post.getId(), false);
            int newLikeCount = post.getLikeCount();

            if (currentLikeState) {
                newLikeCount--;
                likeStates.put(post.getId(), false);
                holder.btnLike.setColorFilter(ContextCompat.getColor(v.getContext(), android.R.color.darker_gray));
                Toast.makeText(v.getContext(), "Unliked!", Toast.LENGTH_SHORT).show();
            } else {
                newLikeCount++;
                likeStates.put(post.getId(), true);
                holder.btnLike.setColorFilter(ContextCompat.getColor(v.getContext(), android.R.color.holo_red_light));
                Toast.makeText(v.getContext(), "Liked!", Toast.LENGTH_SHORT).show();
            }

            post.setLikeCount(newLikeCount);
            holder.likeCount.setText(String.valueOf(newLikeCount));
            updateLikeCountInDatabase(post);
        });
    }

    private void setupCommentButton(Post post, PostViewHolder holder) {
        holder.btnComment.setOnClickListener(v -> {
            if (holder.commentsContainer.getVisibility() == View.VISIBLE) {
                holder.commentsContainer.setVisibility(View.GONE);
            } else {
                loadComments(post.getId(), holder);
            }
        });
    }

    private void updateLikeCountInDatabase(Post post) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.postDao().updatePost(post);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar, postImage, btnLike, btnComment;
        TextView userName, time, content, likeCount, commentCount;
        LinearLayout itemPost, commentsContainer;
        RecyclerView recyclerComments;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.imgUserAvatar);
            postImage = itemView.findViewById(R.id.imgPost);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            userName = itemView.findViewById(R.id.txtUserName);
            time = itemView.findViewById(R.id.txtTime);
            content = itemView.findViewById(R.id.txtContent);
            likeCount = itemView.findViewById(R.id.txtLikeCount);
            commentCount = itemView.findViewById(R.id.txtCommentCount);
            itemPost = itemView.findViewById(R.id.itempost);
            commentsContainer = itemView.findViewById(R.id.commentsContainer);
            recyclerComments = itemView.findViewById(R.id.recyclerComments);
        }
    }
}