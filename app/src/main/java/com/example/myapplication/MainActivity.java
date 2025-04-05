package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ui.post.NewPost;
import com.example.myapplication.ui.message.MessageFragment;
import com.example.myapplication.ui.post.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Mặc định hiển thị MessageFragment khi mở ứng dụng
//        if (savedInstanceState == null) {
//            Log.d("MainActivity","fragment_container  + savedInstanceState");
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, new MessageFragment())
//                    .commit();
//        }
        // Mặc định hiển thị PostFragment khi mở ứng dụng
        if (savedInstanceState == null) {
            Log.d("MainActivity","fragment_container  + savedInstanceState");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PostFragment())
                    .commit();
        }



        // Đặt sự kiện click cho EditText
        EditText editText = findViewById(R.id.txtText);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewPost.class);
                startActivity(intent);
            }
        });



        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                // Comment lại các fragment chưa tạo
                // case R.id.navigation_home:
                //     selectedFragment = new HomeFragment();
                //     break;
                // case R.id.navigation_dashboard:
                //     selectedFragment = new DashboardFragment();
                //     break;
                case 2131230890:
                    Log.d("MainActivity","MessageFragment");
                    selectedFragment = new MessageFragment();
                    break;
                // case R.id.navigation_notifications:
                //     selectedFragment = new NotificationsFragment();
                //     break;
            }

            if (selectedFragment != null) {
                Log.d("MainActivity","selctedFragment");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}
