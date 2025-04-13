package com.example.myapplication.data;

import android.content.Context;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Emtity.Message;
import com.example.myapplication.Emtity.User;
import java.util.List;
import java.util.concurrent.Executors;

public class DataManager {
    private static DataManager instance;
    private SocialNetworkDatabase database;

    private DataManager(Context context) {
        database = SocialNetworkDatabase.getInstance(context);
    }

    public static synchronized DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public List<Message> getMessages() {
        return database.messageDao().getAllMessages();
    }

    public List<User> getUsers() {
        return database.userDao().getAllUsers();
    }

    public User getUserById(int userId) {
        return database.userDao().getUserById(userId);
    }

    public void addMessage(Message message) {
        Executors.newSingleThreadExecutor().execute(() -> database.messageDao().insertMessage(message));
    }

    public void addUser(User user) {
        Executors.newSingleThreadExecutor().execute(() -> database.userDao().insertUser(user));
    }
}
