package com.example.myapplication.ui.message;

import static android.app.Activity.RESULT_OK;

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
import com.example.myapplication.data.DataManager;
import com.example.myapplication.ui.chat.ChatActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessageFragment extends Fragment {
    private static final int REQUEST_CODE_CHAT = 1001;
    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private int currentUserId = 1001; // ID của user đang đăng nhập

    public MessageFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView = root.findViewById(R.id.recyclerMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy danh sách tin nhắn từ DataManager
        List<Message> messages = DataManager.getInstance().getMessages();
        List<ComponentMessage> conversationList = extractLastMessages(messages);

        adapter = new MessageListAdapter(conversationList, componentMessage -> {
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            intent.putExtra("receiverId", componentMessage.getUserId());
            // Nếu cần gửi thêm dữ liệu (ở đây bạn có thể không cần gửi lại vì DataManager giữ dữ liệu)
            startActivityForResult(intent, REQUEST_CODE_CHAT);
        });

        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHAT && resultCode == RESULT_OK && data != null) {
            // Lấy lại danh sách tin nhắn cập nhật từ DataManager
            List<Message> updatedMessages = DataManager.getInstance().getMessages();
            // Xây dựng lại conversation list từ tin nhắn cập nhật
            List<ComponentMessage> updatedConversationList = extractLastMessages(updatedMessages);
            // Cập nhật dữ liệu cho adapter
            adapter.setData(updatedConversationList);

            extractLastMessages(updatedMessages);
        }
    }

    private List<ComponentMessage> extractLastMessages(List<Message> messages) {
        Map<Integer, Message> lastMessageMap = new HashMap<>();
        // Khởi tạo SimpleDateFormat với định dạng "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        for (Message msg : messages) {
            int otherUserId = (msg.getSenderId() == currentUserId) ? msg.getReceiverId() : msg.getSenderId();
            // Lấy đối tượng Date của tin nhắn
            Date msgDate = parseDate(sdf, msg.getCreatedAt());

            // Nếu chưa có tin nhắn nào của user đó, hoặc tin nhắn hiện tại mới hơn tin cũ thì cập nhật
            if (!lastMessageMap.containsKey(otherUserId)) {
                lastMessageMap.put(otherUserId, msg);
            } else {
                Message existingMsg = lastMessageMap.get(otherUserId);
                Date existingDate = parseDate(sdf, existingMsg.getCreatedAt());
                if (msgDate != null && existingDate != null && msgDate.after(existingDate)) {
                    lastMessageMap.put(otherUserId, msg);
                }
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

    // Hàm chuyển đổi chuỗi sang Date
    private Date parseDate(SimpleDateFormat sdf, String dateString) {
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
