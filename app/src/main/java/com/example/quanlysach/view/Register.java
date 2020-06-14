package com.example.quanlysach.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlysach.R;
import com.example.quanlysach.database.helper.UserDatabaseHelper;
import com.example.quanlysach.database.model.User;

public class Register extends AppCompatActivity {
    Button btnRegister;
    EditText edtUsername, edtPass;

    private UserDatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        db = new UserDatabaseHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String pass = edtPass.getText().toString();
                if(!username.trim().equals("") && !pass.trim().equals("")) {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(pass);
                    if(db.getUser(username, pass) != null) {
                        Toast.makeText(Register.this, "Tài khoản đã được sử dụng! Vui lòng chọn một tài khoản khác", Toast.LENGTH_LONG).show();
                    } else if(db.register(user)>=0) {
                        Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Register.this, Login.class);
//                        startActivity(intent);
                        finish();
                    } else Toast.makeText(Register.this, "Đăng ký không thành công", Toast.LENGTH_LONG).show();

                } else Toast.makeText(Register.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
            }
        });
    }

    void initView() {
        btnRegister = findViewById(R.id.button_login);
        edtUsername = findViewById(R.id.edittext_username);
        edtPass = findViewById(R.id.edittext_password);
    }
}
