package com.hoangquocthai.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoangquocthai.chatapp.R;
import com.hoangquocthai.chatapp.dto.UserDTO;
import com.hoangquocthai.chatapp.retrofit.ApiChat;
import com.hoangquocthai.chatapp.retrofit.RetrofitClient;
import com.hoangquocthai.chatapp.retrofit.Server;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiChat apiChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiChat  = RetrofitClient.getInstance(Server.BASE_URL).create(ApiChat.class);
        initializeView();
        edtPassword.setText("password0");
        edtUsername.setText("user0");
        handleEvent();

    }

    private void handleEvent() {
        btnLogin.setOnClickListener((view) -> {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();

            UserDTO userDTO = new UserDTO(username, password);

            compositeDisposable.add(apiChat.login(userDTO)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            user -> {
                                if (user != null) {
                                    Intent intent = new Intent(this, MainActivity.class);
                                    Server.user = user;
                                    startActivity(intent);

                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại. Vui lòng kiểm tra lại tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));


        });
    }

    private void initializeView() {

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }
}