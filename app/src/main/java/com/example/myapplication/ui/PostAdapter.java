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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.DAO.CommentDao;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.DAO.UserDao;
import com.example.myapplication.Entity.Comment;
import com.example.myapplication.Entity.Post;
import com.example.myapplication.Entity.User;
import com.example.myapplication.R;

import java.util.List;
import java.util.concurrent.Executors;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;
    private SocialNetworkDatabase db;
     private static LinearLayout itemPost;
    public PostAdapter(List<Post> postList, SocialNetworkDatabase database) {
        this.postList = postList;
        this.db = database;
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

        // Load thông tin người dùng trong background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            UserDao userDao = db.userDao();
            User user = userDao.getUserById(post.getUserId());

            holder.itemView.post(() -> { // Cập nhật UI trên main thread
                if (user != null) {
                    holder.userName.setText(user.getUsername());

                    // Load ảnh đại diện bằng Glide vào holder.userAvatar
                    Glide.with(holder.itemView.getContext())
                            .load(user.getProfilePicture()) // URL ảnh từ database
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.avt1)
                                    .error(R.drawable.avt1))
                            .circleCrop()  // Làm tròn ảnh đại diện
                            .into(holder.userAvatar);
                } else {
                    holder.userName.setText("Người dùng không tồn tại");
//                    holder.userAvatar.setImageResource(R.drawable.avt1); // Ảnh mặc định
                }
            });
        });

        // Hiển thị nội dung bài viết
        holder.time.setText(post.getCreatedAt());
        holder.content.setText(post.getContent());

        // Load ảnh bài viết (nếu có)
        if (post.getMediaUrl() != null && !post.getMediaUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getMediaUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avt1)
                            .error(R.drawable.avt1))
                    .into(holder.postImage);
            holder.postImage.setVisibility(View.VISIBLE);
        } else {
            holder.postImage.setVisibility(View.GONE);  // Ẩn nếu không có ảnh
        }

        // Load số lượng bình luận trong background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            CommentDao commentDao = db.commentDao();
            List<Comment> commentList = commentDao.getCommentsByPost(post.getId());

            holder.itemView.post(() -> {
                holder.commentCount.setText(String.valueOf(commentList.size()));
            });
        });

        // Xử lý sự kiện Like
        holder.btnLike.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Liked!", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện Comment
        holder.btnComment.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Mở bình luận!", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    // Cập nhật danh sách bài viết mới
    public void updatePosts(List<Post> newPosts) {
        this.postList = newPosts;
        notifyDataSetChanged();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar, postImage, btnLike, btnComment;
        TextView userName, time, content, commentCount;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.imgUserAvatar);
            Glide.with(itemView.getContext())
                    .load(R.drawable.avt1)
                    .circleCrop()
                    .into(userAvatar);
            itemPost = itemView.findViewById(R.id.itempost);
            itemPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handelFocusPost();
                }
            });
            postImage = itemView.findViewById(R.id.imgPost);
            userName = itemView.findViewById(R.id.txtUserName);
            time = itemView.findViewById(R.id.txtTime);
            content = itemView.findViewById(R.id.txtContent);
            commentCount = itemView.findViewById(R.id.txtCommentCount);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
        }
        public void handelFocusPost(){
            Intent intent = new Intent(itemView.getContext(), FocusPost.class);
            itemView.getContext().startActivity(intent);
        }

    }
}
