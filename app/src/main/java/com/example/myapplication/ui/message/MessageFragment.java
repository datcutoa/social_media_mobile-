package com.example.myapplication.ui.message;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Emtity.Message;
import com.example.myapplication.R;
import com.example.myapplication.ui.chat.ChatActivity;

import java.util.Arrays;
import java.util.List;

public class MessageFragment extends Fragment {

    public MessageFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Danh sách tin nhắn mẫu
        List<Message> messages = Arrays.asList(
                new Message(1, 1001, 1002, "Hello", true, "2024-03-22"),
                new Message(2, 1003, 1004, "How are you?", false, "2024-03-21")
        );

        MessageAdapter adapter = new MessageAdapter(messages, message -> {
            // Khi click vào 1 tin nhắn, mở trang chat
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("receiverId", message.getReceiverId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        return view;
    }

}
