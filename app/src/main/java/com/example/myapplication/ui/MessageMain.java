//package com.example.myapplication.ui;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//
//import com.example.myapplication.R;
//import com.example.myapplication.ui.message.MessageFragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class MessageMain extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_masage);
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//
//        // Mặc định hiển thị MessageFragment khi mở ứng dụng
//        if (savedInstanceState == null) {
//            Log.d("MainActivity","fragment_container  + savedInstanceState");
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, new MessageFragment())
//                    .commit();
//        }
//
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//
//            switch (item.getItemId()) {
//                // Comment lại các fragment chưa tạo
//                // case R.id.navigation_home:
//                //     selectedFragment = new HomeFragment();
//                //     break;
//                // case R.id.navigation_dashboard:
//                //     selectedFragment = new DashboardFragment();
//                //     break;
//                case 2131230890:
//                    Log.d("MainActivity","MessageFragment");
//                    selectedFragment = new MessageFragment();
//                    break;
//                // case R.id.navigation_notifications:
//                //     selectedFragment = new NotificationsFragment();
//                //     break;
//            }
//
//            if (selectedFragment != null) {
//                Log.d("MainActivity","selctedFragment");
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, selectedFragment)
//                        .commit();
//            }
//            return true;
//        });
//    }
//}
