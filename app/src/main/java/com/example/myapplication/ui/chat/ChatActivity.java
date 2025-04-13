package com.example.myapplication.ui.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Emtity.Message;
import com.example.myapplication.ui.message.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText edtMessage;
    private ImageButton btnSend;
    private int currentUserId;
    private int receiverId;
    private String nameReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerViewChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        ImageButton btnBack = findViewById(R.id.btnBack);
        TextView tvReceiverName = findViewById(R.id.tvReceiverName);

        currentUserId = getIntent().getIntExtra("currentUserId",-1);
        receiverId = getIntent().getIntExtra("receiverId",-1);
        nameReceiver = getIntent().getStringExtra("nameReceiver");

        tvReceiverName.setText(nameReceiver);
        btnBack.setOnClickListener(v -> finish());

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUserId, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        btnSend.setOnClickListener(view -> sendMessage());
        listenForMessages();
    }

    private void listenForMessages() {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("messages").child(getChatId());

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi tải tin nhắn", error.toException());
            }
        });
    }

    private void sendMessage() {
        String content = edtMessage.getText().toString().trim();
        if (!content.isEmpty()) {
            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("messages").child(getChatId());
            String messageId = chatRef.push().getKey();
            if (messageId == null) return;


            Message newMessage = new Message(messageId, currentUserId, receiverId, content, false,System.currentTimeMillis()+"");
            chatRef.child(messageId).setValue(newMessage).addOnSuccessListener(aVoid -> {
                edtMessage.setText("");
            });
        }
    }

    private String getChatId() {
        return currentUserId < receiverId ? currentUserId + "_" + receiverId : receiverId + "_" + currentUserId;
    }

}
