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
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;
    private UserDao userDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        SocialNetworkDatabase db = SocialNetworkDatabase.getInstance(this);
        userDao = db.userDao();

        // Xử lý khi nhấn nút Đăng nhập
        btnLogin.setOnClickListener(v -> loginUser());

        // Xử lý khi nhấn nút Đăng ký (Chuyển đến Register)
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            User user = userDao.login(username, password);
            runOnUiThread(() -> {
                if (user != null) {
                    getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            .edit()
                            .putInt("userId", user.getId())
                            .apply();
                    Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc Login
                } else {
                    Toast.makeText(Login.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

