package com.hoangquocthai.chatapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoangquocthai.chatapp.R;
import com.hoangquocthai.chatapp.adapter.GroupAdapter;
import com.hoangquocthai.chatapp.dto.GroupChat;
import com.hoangquocthai.chatapp.retrofit.ApiChat;
import com.hoangquocthai.chatapp.retrofit.RetrofitClient;
import com.hoangquocthai.chatapp.retrofit.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView txtFullName;
    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiChat apiChat;
    private LinearLayoutManager linearLayoutManager;

    private GroupAdapter groupAdapter;

    private ImageView imgGroupAdd;
    private List<GroupChat> groupChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializeView();
        loadData();
        handleClick();

    }

    private void handleClick() {
        imgGroupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        compositeDisposable.add(apiChat.getAllGroupOfUser(Server.user.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        groupChats -> {
                            if (groupChats != null) {
                                groupChatList = groupChats;

                                groupAdapter = new GroupAdapter(getApplicationContext(), groupChatList);
                                recyclerView.setAdapter(groupAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server: " ,Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initializeView() {
        txtFullName = findViewById(R.id.txtFullName);
        recyclerView = findViewById(R.id.mainUserRecyclerView);
        imgGroupAdd = findViewById(R.id.imgGroupAdd);

        txtFullName.setText(Server.user.getFullName());

        groupChatList = new ArrayList<>();
        apiChat = RetrofitClient.getInstance(Server.BASE_URL).create(ApiChat.class);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
    }
}