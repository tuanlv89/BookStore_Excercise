package com.example.quanlysach.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysach.R;
import com.example.quanlysach.database.helper.UserDatabaseHelper;
import com.example.quanlysach.database.model.User;


public class Login extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvRegister;

    private UserDatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        db = new UserDatabaseHelper(this);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
                User user = db.getUser(username, pass);
                if(user != null) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else Toast.makeText(Login.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
            }
        });
    }

    void initView() {
        btnLogin = findViewById(R.id.button_login);
        edtUsername = findViewById(R.id.edittext_username);
        edtPassword = findViewById(R.id.edittext_password);
        tvRegister = findViewById(R.id.textview_register);
    }
}
