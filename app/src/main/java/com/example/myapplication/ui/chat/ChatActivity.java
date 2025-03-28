package com.example.myapplication.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Emtity.Message;
import com.example.myapplication.ui.message.MessageAdapter;

import java.io.Serializable;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerViewChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        // Lấy dữ liệu từ Intent
        currentUserId = getIntent().getIntExtra("currentUserId", -1);
        receiverId = getIntent().getIntExtra("receiverId", -1);



        if (currentUserId == -1 || receiverId == -1) {
            finish();
            return;
        }

        Serializable serializable = getIntent().getSerializableExtra("messages");
        List<Message> allMessages = (serializable != null) ? (List<Message>) serializable : new ArrayList<>();

        messageList = getFilteredMessages(allMessages,currentUserId, receiverId);
        messageAdapter = new MessageAdapter(messageList, currentUserId,null);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        btnSend.setOnClickListener(view -> sendMessage());
    }

    private List<Message> getFilteredMessages(List<Message> allMessages,int currentUserId, int receiverId) {
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
            Message newMessage = new Message(messageList.size() + 1, currentUserId, receiverId, content, false, "Now");
            messageList.add(newMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
            edtMessage.setText("");
        }
    }
}
