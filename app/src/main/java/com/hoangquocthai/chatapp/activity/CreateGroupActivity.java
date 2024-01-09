package com.hoangquocthai.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoangquocthai.chatapp.R;
import com.hoangquocthai.chatapp.adapter.UsersAdapter;
import com.hoangquocthai.chatapp.dto.GroupChatRequestDto;
import com.hoangquocthai.chatapp.object.User;
import com.hoangquocthai.chatapp.retrofit.ApiChat;
import com.hoangquocthai.chatapp.retrofit.RetrofitClient;
import com.hoangquocthai.chatapp.retrofit.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateGroupActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCreateGroup;
    private Button btnCreateGroup;
    private List<User> userList;
    private List<User> userSelected;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiChat apiChat;
    private UsersAdapter friendAdapter;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_activity);

        initialView();
        getDataUser();
        handleCreateGroup();

    }

    private void getDataUser() {
        compositeDisposable.add(apiChat.getAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        users -> {
                            if (users != null) {
                                userList = users;
                                //loc user khác user hiện tại
                                if (userList.size() >= 2) {
                                    for (int i = 0; i < userList.size(); i++) {
                                        if (userList.get(i).getUsername().equals(Server.user.getUsername())) {
                                            userList.remove(i);
                                            break;
                                        }
                                    }
                                }

                                friendAdapter = new UsersAdapter(getApplicationContext(), userList, userSelected);
                                recyclerViewCreateGroup.setAdapter(friendAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server: " ,Toast.LENGTH_SHORT).show();
                        }
                ));
    }


    private void handleCreateGroup() {
        btnCreateGroup.setOnClickListener((view)->{
            if(userSelected.size() == 0){
                return;
            }
            GroupChatRequestDto groupChatRequestDto = new GroupChatRequestDto();
            groupChatRequestDto.setUsername(Server.user.getUsername());
            List<String> strings = new ArrayList<>();

            for (User s : userSelected){
                strings.add(s.getUsername());
            }
            groupChatRequestDto.setUsers(strings);

            compositeDisposable.add(apiChat.createGroup(groupChatRequestDto)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            groupChat -> {
                                if (groupChat != null) {
                                    Intent intent = new Intent(CreateGroupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), "Không kết nối được với server: " ,Toast.LENGTH_SHORT).show();
                            }
                    ));
        });
    }

    private void initialView() {
        apiChat = RetrofitClient.getInstance(Server.BASE_URL).create(ApiChat.class);

        recyclerViewCreateGroup = findViewById(R.id.recyclerViewCreateGroup);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        userList = new ArrayList<>();
        userSelected = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewCreateGroup.setLayoutManager(linearLayoutManager);
        recyclerViewCreateGroup.setHasFixedSize(true);

    }
}
