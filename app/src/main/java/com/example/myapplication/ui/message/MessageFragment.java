package com.example.myapplication.ui.message;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Emtity.Message;
import com.example.myapplication.R;
import com.example.myapplication.ui.chat.ChatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Fragment hiển thị danh sách cuộc trò chuyện
public class MessageFragment extends Fragment {
    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private int currentUserId = 1001; // ID của user đang đăng nhập

    public MessageFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView = root.findViewById(R.id.recyclerMessages);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy danh sách tin nhắn
        List<Message> messages = getSampleMessages();
        List<ComponentMessage> conversationList = extractLastMessages(messages);

        adapter = new MessageListAdapter(conversationList, componentMessage -> {
            Log.d("MessageFragment", "User clicked: " + componentMessage.getName());
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            intent.putExtra("receiverId", componentMessage.getUserId());
            intent.putExtra("messages",(Serializable) getSampleMessages());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        return root;
    }

    private List<Message> getSampleMessages() {
        List<Message> msgs = new ArrayList<>();
        msgs.add(new Message(1, 1001, 1002, "Hello 1002", true, "2024-03-21"));
        msgs.add(new Message(2, 1003, 1001, "How are you 1001?", false, "2024-03-21"));
        msgs.add(new Message(3, 1001, 1003, "I'm good 1003!", false, "2024-03-22"));
        msgs.add(new Message(4, 1004, 1001, "Hey, what's up?", false, "2024-03-20"));
        msgs.add(new Message(5, 1002, 1001, "Hello earliest 1001", true, "2024-03-23"));
        return msgs;
    }

    private List<ComponentMessage> extractLastMessages(List<Message> messages) {
        Map<Integer, Message> lastMessageMap = new HashMap<>();

        for (Message msg : messages) {
            int otherUserId = (msg.getSenderId() == currentUserId) ? msg.getReceiverId() : msg.getSenderId();

            // Cập nhật tin nhắn mới nhất dựa vào thời gian
            if (!lastMessageMap.containsKey(otherUserId) ||
                    msg.getCreatedAt().compareTo(lastMessageMap.get(otherUserId).getCreatedAt()) > 0) {
                lastMessageMap.put(otherUserId, msg);
            }
        }

        List<ComponentMessage> result = new ArrayList<>();
        for (Map.Entry<Integer, Message> entry : lastMessageMap.entrySet()) {
            int userId = entry.getKey();
            Message lastMsg = entry.getValue();

            result.add(new ComponentMessage(
                    userId,
                    "User " + userId,
                    "https://example.com/avatar/" + userId + ".png",
                    lastMsg.getContent(),
                    lastMsg.getCreatedAt()
            ));
        }
        return result;
    }
}
