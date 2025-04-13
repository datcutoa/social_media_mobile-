package com.example.myapplication.ui.message;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Emtity.Message;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.R;
import com.example.myapplication.ui.chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private int currentUserId;

    public MessageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView = root.findViewById(R.id.recyclerMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentUserId = getArguments().getInt("currentUserId", -1); // Lấy ID số

        adapter = new MessageListAdapter(new ArrayList<>(), componentMessage -> {
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            intent.putExtra("receiverId", componentMessage.getUserId());
            intent.putExtra("nameReceiver", componentMessage.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        loadRecentMessages();
        return root;
    }

    private void loadRecentMessages() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ComponentMessage> conversationList = new ArrayList<>();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Message message = messageSnapshot.getValue(Message.class);
                        if (message != null) {
                            int otherUserId = message.getSenderId() == currentUserId ? message.getReceiverId() : message.getSenderId();
                            fetchUserInfo(otherUserId, message.getContent(), conversationList);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi tải danh sách tin nhắn", error.toException());
            }
        });
    }

    private void fetchUserInfo(int userId, String lastMessage, List<ComponentMessage> conversationList) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId+"");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    conversationList.add(new ComponentMessage(
                            user.getId(),
                            user.getName(),
                            user.getProfilePicture(),
                            lastMessage,
                            "" // Thời gian có thể lấy từ Firebase
                    ));
                    adapter.setData(conversationList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi tải thông tin user", error.toException());
            }
        });
    }

}
