package com.example.myapplication.ui.post;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.R;

public class PostController extends AppCompatActivity   {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);
    }

//    public void newPost(){
//        setContentView(R.layout.item_post);
//        SocialNetworkDatabase db= SocialNetworkDatabase.getInstance(this);
//        User user;
//        user=db.userDao().getUserById(3);
//        ImageView imgAvata = findViewById(R.id.imgUserAvatar);
//        TextView userName= findViewById(R.id.txtUserName);
//    }

}
