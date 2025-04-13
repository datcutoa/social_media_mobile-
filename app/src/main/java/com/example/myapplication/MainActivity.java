package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Emtity.Message;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.ui.message.MessageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        FirebaseDatabase.getInstance().setPersistenceEnabled(true); // Bật cache offline
//        addSampleUsers();
//        addSampleMessages();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userUid = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userUid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        currentUserId = snapshot.getValue(Integer.class);
                        openMessageFragment(currentUserId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Lỗi lấy ID người dùng", error.toException());
                }
            });
        }
//        else {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        }
    }

    private void openMessageFragment(int userId) {
        Bundle bundle = new Bundle();
        bundle.putInt("currentUserId", userId);
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, messageFragment)
                .commit();
    }

    private void addSampleUsers() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        User user1 = new User(1, "user1", "password123", "user1@gmail.com", "User One",
                "Hello, I am User One", "cover1.jpg", "profile1.jpg", "Male", "1998-01-01", "2025-04-02");
        User user2 = new User(2, "user2", "password123", "user2@gmail.com", "User Two",
                "Hello, I am User Two", "cover2.jpg", "profile2.jpg", "Female", "1999-02-02", "2025-04-02");

        usersRef.child(String.valueOf(user1.getId())).setValue(user1);
        usersRef.child(String.valueOf(user2.getId())).setValue(user2);
    }
    private void addSampleMessages() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");

        Message msg1 = new Message("msg_1", 1, 2, "Hello!", false, "2025-04-02 10:00:00");
        Message msg2 = new Message("msg_2", 2, 1, "Hi!", false, "2025-04-02 10:05:00");

        messagesRef.child(msg1.getId()).setValue(msg1);
        messagesRef.child(msg2.getId()).setValue(msg2);
    }


}
