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

import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Entity.Friendship;
import com.example.myapplication.Entity.Message;
import com.example.myapplication.Entity.User;
import com.example.myapplication.R;
import com.example.myapplication.ui.chat.ChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class MessageFragment extends Fragment {
    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private SocialNetworkDatabase db;

    private int currentUserId;

    public MessageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView = root.findViewById(R.id.recyclerMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d("MessageFragment","OnCreateView MessageFragment");

        db = SocialNetworkDatabase.getInstance(requireContext());


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

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MessageFragment", "onResume được gọi — load lại danh sách tin nhắn");
        loadRecentMessages();
    }


    private void loadRecentMessages() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Friendship> friendships = db.friendshipDao().getFriendshipsByUserIdAndStatus(currentUserId);
            List<ComponentMessage> conversationList = new ArrayList<>();
            Log.d("MessageFragment", "Đã lấy được " + friendships.size() + " friendship.");

            int totalFriendCount = friendships.size();
            int[] completedCount = {0};  // Đếm số lần fetch xong

            for (Friendship friendship : friendships) {
                int otherUserId = (friendship.getUserId() == currentUserId)
                        ? friendship.getFriendId() : friendship.getUserId();

                fetchUserInfo(otherUserId, conversationList, totalFriendCount, completedCount);
            }

            if (totalFriendCount == 0) {
                // Không có bạn bè — cập nhật luôn
                requireActivity().runOnUiThread(() -> adapter.setData(new ArrayList<>()));
            }
        });
    }

    private void fetchUserInfo(int userId, List<ComponentMessage> conversationList, int totalFriendCount, int[] completedCount) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().getUserById(userId);
            if (user != null) {
                DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");
                messagesRef.child(getConversationKey(currentUserId, userId))
                        .limitToLast(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String finalLastMessage = "Chưa có tin nhắn nào";
                                String createdAt="";
                                for (DataSnapshot msgSnap : snapshot.getChildren()) {
                                    Message msg = msgSnap.getValue(Message.class);
                                    if (msg != null) {
                                        finalLastMessage = msg.getContent();
                                        createdAt = formatTimestamp(msg.getCreatedAt());
                                    }
                                }
                                conversationList.add(new ComponentMessage(
                                        user.getId(),
                                        user.getUsername(),
                                        user.getProfilePicture(),
                                        finalLastMessage,
                                        createdAt
                                ));

                                completedCount[0]++;
                                if (completedCount[0] == totalFriendCount) {
                                    // Khi đã fetch xong tất cả
                                    requireActivity().runOnUiThread(() -> adapter.setData(conversationList));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Firebase", "Lỗi lấy message", error.toException());
                                completedCount[0]++;
                                if (completedCount[0] == totalFriendCount) {
                                    requireActivity().runOnUiThread(() -> adapter.setData(conversationList));
                                }
                            }
                        });
            } else {
                completedCount[0]++;
                if (completedCount[0] == totalFriendCount) {
                    requireActivity().runOnUiThread(() -> adapter.setData(conversationList));
                }
            }
        });
    }


    private String formatTimestamp(String timestamp) {
        try {
            long timeMillis = Long.parseLong(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());
            return sdf.format(new Date(timeMillis));
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private String getConversationKey(int userId1, int userId2) {
        return (userId1 < userId2)
                ? userId1 + "_" + userId2
                : userId2 + "_" + userId1;
    }

}
