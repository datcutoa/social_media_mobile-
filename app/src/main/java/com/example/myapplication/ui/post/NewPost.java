package com.example.myapplication.ui.post;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DAO.PostDao;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Entity.Post;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NewPost extends AppCompatActivity {
    private Button btnPost;
    private Button btnClose;
    private EditText edtPostContent;
    private PostDao postDao;
    private ImageView addImgPost;
    private ImageView imgPost; // ImageView để hiển thị ảnh đã chọn
    private Uri selectedImageUri; // Biến để lưu trữ URI của ảnh
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout);
        SocialNetworkDatabase db = SocialNetworkDatabase.getInstance(this);
        postDao = db.postDao(); // Khởi tạo PostDao

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewPost.this, MainActivity.class);
                startActivity(intent);
//                new Thread(()->{
//                    postDao.deleteAllPosts();
//                    runOnUiThread(()->{
//                        Toast.makeText(NewPost.this, "Xóa het post", Toast.LENGTH_SHORT).show();
//                        finish();
//                    });
//                }).start();
            }
        });

        btnPost = findViewById(R.id.btnPost);
        addImgPost = findViewById(R.id.btnAddImage);
        imgPost = findViewById(R.id.imgPost); // ImageView để hiển thị ảnh đã chọn

        // Xử lý sự kiện click vào nút Thêm ảnh
        addImgPost = findViewById(R.id.btnAddImage);
        imgPost = findViewById(R.id.imgPost);

        addImgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> imageNames = new ArrayList<>();
                List<Integer> imageResIds = new ArrayList<>();

                Field[] drawables = R.drawable.class.getFields();
                for (Field field : drawables) {
                    try {
                        String name = field.getName();
                        int resId = field.getInt(null);
                        imageNames.add(name);
                        imageResIds.add(resId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String[] namesArray = imageNames.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(NewPost.this);
                builder.setTitle("Chọn ảnh từ drawable")
                        .setItems(namesArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                int selectedResId = imageResIds.get(which);
                                imgPost.setVisibility(View.VISIBLE);
                                imgPost.setImageResource(selectedResId);

                                // Lưu lại resource ID thành URI để dùng khi đăng bài
                                selectedImageUri = Uri.parse("android.resource://" + getPackageName() + "/" + selectedResId);
                            }
                        })
                        .show();
            }
        });

        edtPostContent = findViewById(R.id.edtPostContent);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy nội dung bài viết từ EditText
                String postContent = edtPostContent.getText().toString().trim();

                // Kiểm tra nếu người dùng không nhập nội dung
                if (postContent.isEmpty()) {
                    Toast.makeText(NewPost.this, "Bạn chưa nhập nội dung bài viết", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra nếu người dùng không chọn ảnh
                if (selectedImageUri == null) {
                    Toast.makeText(NewPost.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                int userId = sharedPref.getInt("userId", -1); // Lấy userId

                if (userId == -1) {
                    Toast.makeText(NewPost.this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Lấy ngày giờ hiện tại
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentDateTime = sdf.format(new Date());

                // Tạo đối tượng Post với nội dung bài viết và ảnh
                Post post = new Post(userId, postContent, selectedImageUri.toString(), "public", currentDateTime);

                // Sử dụng Thread để thực hiện công việc insert vào database
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Insert bài viết vào database
                        postDao.insertPost(post);

                        // Quay lại main thread để thông báo và chuyển màn hình
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewPost.this, "Bài viết đã được đăng", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewPost.this, PostController.class);
                                startActivity(intent);
                            }
                        });
                    }
                });

                // Bắt đầu thread
                thread.start();
            }
        });
    }

    // Xử lý kết quả khi chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData(); // Lấy URI của ảnh đã chọn
            imgPost.setVisibility(View.VISIBLE); // Hiển thị ảnh nếu đang bị GONE
            imgPost.setImageURI(selectedImageUri); // Hiển thị ảnh trong ImageView
        }
    }


}

//        private void handleAvttoCicle(AppCompatActivity appCompatActivity, String avt, ImageView img){
//            Glide.with(appCompatActivity)
//                    .load(avt)  // URL ảnh hoặc resource nội bộ
//                    .circleCrop()  // Tự động cắt ảnh thành hình tròn
//                    .into(img);
//        }
