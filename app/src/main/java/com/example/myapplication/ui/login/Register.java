package com.example.myapplication.ui.login;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.DAO.UserDao;
import com.example.myapplication.Emtity.User;
import com.example.myapplication.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Register extends AppCompatActivity {
    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnRegister;
    private UserDao userDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Ánh xạ các thành phần giao diện
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Khởi tạo database và DAO
        SocialNetworkDatabase db = SocialNetworkDatabase.getInstance(this);
        userDao = db.userDao();

        // Xử lý sự kiện khi nhấn nút Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            // Kiểm tra xem username đã tồn tại chưa
            int count = userDao.checkUserExists(username);
            if (count > 0) {
                runOnUiThread(() -> Toast.makeText(Register.this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show());
            } else {
                // Tạo người dùng mới
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setEmail(email);
                newUser.setPassword(password);
                userDao.insertUser(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, Login.class)); // Chuyển qua màn hình đăng nhập
                    finish();
                });
            }
        });
    }
}

