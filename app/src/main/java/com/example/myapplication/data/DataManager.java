package com.example.myapplication.data;

import com.example.myapplication.Emtity.Message;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private List<Message> messages;

    private DataManager() {
        messages = new ArrayList<>();
        // Bạn có thể khởi tạo dữ liệu mẫu nếu cần:
        messages.add(new Message(1, 1001, 1002, "Hello 1002", true, "2024-03-21 11:56:46"));
        messages.add(new Message(2, 1003, 1001, "How are you 1001?", false, "2024-03-21 11:56:46"));
        messages.add(new Message(3, 1001, 1003, "I'm good 1003!", false, "2024-03-22 11:56:46"));
        messages.add(new Message(4, 1004, 1001, "Hey, what's up?", false, "2024-03-20 11:56:46"));
        messages.add(new Message(5, 1002, 1001, "Hello earliest 1001", true, "2024-03-23 11:56:46"));
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}
