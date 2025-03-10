package com.example.myapplication.ui;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.R;

public class PostController extends AppCompatActivity   {
    public void newPost(){
        setContentView(R.layout.item_post);
        SocialNetworkDatabase db= SocialNetworkDatabase.getInstance(this);
        User user;
        user=db.userDao().getUserById(3);
       ImageView imgAvata =findViewById(R.id.imgAvatar);
       View userName= findViewById(R.id.tvUsername);









    }

}
