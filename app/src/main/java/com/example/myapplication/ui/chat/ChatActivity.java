package com.example.myapplication.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Emtity.Message;
import com.example.myapplication.data.DataManager;
import com.example.myapplication.ui.message.MessageAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList; // tin nhắn giữa currentUser và receiver
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

        // Lấy dữ liệu từ Intent
        currentUserId = getIntent().getIntExtra("currentUserId", -1);
        receiverId = getIntent().getIntExtra("receiverId", -1);
        nameReceiver=getIntent().getStringExtra("nameReceiver");

        if (currentUserId == -1 || receiverId == -1) {
            finish();
            return;
        }

        // Hiển thị tên receiver (ở đây bạn có thể lấy tên từ DataManager hoặc truyền qua Intent)
        tvReceiverName.setText("" + nameReceiver);

        btnBack.setOnClickListener(v -> finish()); // Quay lại MessageFragment

        // Lấy danh sách tin nhắn từ DataManager
        List<Message> allMessages = DataManager.getInstance().getMessages();
        // Lọc tin nhắn giữa currentUserId và receiverId
        messageList = getFilteredMessages(allMessages, currentUserId, receiverId);
        messageAdapter = new MessageAdapter(messageList, currentUserId, null);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        btnSend.setOnClickListener(view -> sendMessage());

        edtMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                edtMessage.post(() -> {
                    edtMessage.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(edtMessage, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        super.finish();
    }


    private List<Message> getFilteredMessages(List<Message> allMessages, int currentUserId, int receiverId) {
        List<Message> filteredMessages = new ArrayList<>();
        for (Message message : allMessages) {
            if ((message.getSenderId() == currentUserId && message.getReceiverId() == receiverId) ||
                    (message.getSenderId() == receiverId && message.getReceiverId() == currentUserId)) {
                filteredMessages.add(message);
            }
        }
        return filteredMessages;
    }

    private void sendMessage() {
        String content = edtMessage.getText().toString().trim();
        if (!content.isEmpty()) {
            // Tạo tin nhắn mới
            Message newMessage = new Message(
                    DataManager.getInstance().getMessages().size() + 1, // ID mới dựa trên tổng số tin nhắn
                    currentUserId,
                    receiverId,
                    content,
                    false,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())
            );
            // Cập nhật DataManager
            DataManager.getInstance().addMessage(newMessage);
            // Nếu tin nhắn thỏa mãn bộ lọc (với receiver hiện tại) thì cập nhật messageList
            if ((newMessage.getSenderId() == currentUserId && newMessage.getReceiverId() == receiverId) ||
                    (newMessage.getSenderId() == receiverId && newMessage.getReceiverId() == currentUserId)) {
                messageList.add(newMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
            }
            edtMessage.setText("");
        }
    }
}
