package com.example.myapplication.data;

import com.example.myapplication.Emtity.Message;
import com.example.myapplication.Emtity.User;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private List<Message> messages;
    private List<User> users;

    private DataManager() {
        messages = new ArrayList<>();
        users= new ArrayList<>();
        // Bạn có thể khởi tạo dữ liệu mẫu nếu cần:
        messages.add(new Message(1, 1001, 1002, "Hello 1002", true, "2024-03-21 11:56:46"));
        messages.add(new Message(2, 1003, 1001, "How are you 1001?", false, "2024-03-21 11:56:46"));
        messages.add(new Message(3, 1001, 1003, "I'm good 1003!", false, "2024-03-22 11:56:46"));
        messages.add(new Message(4, 1004, 1001, "Hey, what's up?", false, "2024-03-20 11:56:46"));
        messages.add(new Message(5, 1002, 1001, "Hello earliest 1001", true, "2024-03-23 11:56:46"));

        users.add(new User(1001,"HuyUsername","123","huy@gmail.com","Huy","","","","Nam","19/10/2004",""));
        users.add(new User(1002,"ĐạtUsername","123","dat@gmail.com","Đạt","","","","Nam","19/10/2004",""));
        users.add(new User(1003,"PhucUsername","123","phuc@gmail.com","Phúc","","","","Nam","19/10/2004",""));
        users.add(new User(1004,"PhanUsername","123","phan@gmail.com","Phan","","","","Nam","19/10/2004",""));
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
    public List<User> getUsers() {
        return users;
    }
    public User getUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }


    public void addMessage(Message message) {
        messages.add(message);
    }
    public  void addUser(User user){
        users.add(user);
    }
}
