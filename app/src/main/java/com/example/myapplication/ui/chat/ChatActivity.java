package com.example.myapplication.ui.chat;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class ChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int receiverId = getIntent().getIntExtra("receiverId", -1);
        TextView txtChatTitle = findViewById(R.id.txtChatTitle);
        txtChatTitle.setText("Chat với User " + receiverId);
    }
}
