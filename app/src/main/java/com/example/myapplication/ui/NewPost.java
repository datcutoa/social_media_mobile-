    package com.example.myapplication.ui;

    import android.content.Intent;
    import android.os.Bundle;
    import android.provider.ContactsContract;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;

    import com.bumptech.glide.Glide;
    import com.example.myapplication.DAO.SocialNetworkDatabase;
    import com.example.myapplication.MainActivity;
    import com.example.myapplication.R;
    import com.example.myapplication.MainActivity;
    import java.util.ArrayList;

    public class NewPost extends AppCompatActivity {
        private  String avtDefault="C:\\SocialMediaMobile\\app\\src\\main\\res\\drawable\\avt1.png";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.post_layout);

            Button btnClose= findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(NewPost.this, MainActivity.class);
                    startActivity(intent);
                }
            });


        }
        private void handleAvttoCicle(AppCompatActivity appCompatActivity, String avt, ImageView img){
            Glide.with(appCompatActivity)
                    .load(avt)  // URL ảnh hoặc resource nội bộ
                    .circleCrop()  // Tự động cắt ảnh thành hình tròn
                    .into(img);
        }

    }
