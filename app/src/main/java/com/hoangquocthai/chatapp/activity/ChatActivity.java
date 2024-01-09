package com.hoangquocthai.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hoangquocthai.chatapp.R;
import com.hoangquocthai.chatapp.activity.CreateGroupActivity;
import com.hoangquocthai.chatapp.adapter.MessageAdapter;
import com.hoangquocthai.chatapp.dto.GroupChat;
import com.hoangquocthai.chatapp.dto.GroupChatRequestDto;
import com.hoangquocthai.chatapp.object.Message;
import com.hoangquocthai.chatapp.object.User;
import com.hoangquocthai.chatapp.retrofit.ApiChat;
import com.hoangquocthai.chatapp.retrofit.RetrofitClient;
import com.hoangquocthai.chatapp.retrofit.Server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import tech.gusavila92.websocketclient.WebSocketClient;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewListMessage;
    private ImageView imgSendMessage;
    private List<Message> messageList;
    private LinearLayoutManager linearLayoutManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiChat apiChat;
    private EditText edtMessage;
    private MessageAdapter messageAdapter;
    private WebSocketClient webSocketClient;
    private Gson g;
    private GroupChat groupChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        createWebSocketClient();

        Intent intent = getIntent();

        Long groupId = intent.getLongExtra("groupChat",-1);
        initialView();

        compositeDisposable.add(apiChat.getGroupById(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        group -> {
                            if (group != null) {
                                groupChat = group;
                                getData(groupId);
                                handleClickSendMessage();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server: ", Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    private void handleClickSendMessage() {

        imgSendMessage.setOnClickListener((v -> {
            if(!edtMessage.getText().toString().isEmpty()){
                Message message = new Message();
                message.setContent(edtMessage.getText().toString());
                message.setIdMessage(null);
                message.setGroupChat(groupChat);
                message.setCreatedAt(new Date());
                message.setSender(Server.user);

                webSocketClient.send(g.toJson(message));
                getData(groupChat.getGroupId());
                edtMessage.setText("");

            }
        }));
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://192.168.1.5:8081/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {

            }

            @Override
            public void onTextReceived(String message) {
                messageList.add(g.fromJson(message, Message.class));
            }

            @Override
            public void onBinaryReceived(byte[] data) {

            }

            @Override
            public void onPingReceived(byte[] data) {

            }

            @Override
            public void onPongReceived(byte[] data) {

            }

            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onCloseReceived() {

            }
        };
//        webSocketClient.setConnectTimeout(10000);
//        webSocketClient.setReadTimeout(60000);
//        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }


    private void initialView() {
        apiChat = RetrofitClient.getInstance(Server.BASE_URL).create(ApiChat.class);

        imgSendMessage = findViewById(R.id.imgSendMessage);
        recyclerViewListMessage = findViewById(R.id.recyclerViewListMessage);
        edtMessage = findViewById(R.id.edtMessage);

        apiChat = RetrofitClient.getInstance(Server.BASE_URL).create(ApiChat.class);
        messageList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewListMessage.setLayoutManager(linearLayoutManager);
        recyclerViewListMessage.setHasFixedSize(true);

        g = new Gson();
    }

    private void getData(Long groupId) {
        compositeDisposable.add(apiChat.getAllMessageByGroup(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messages -> {
                            if (messages != null) {
                                messageList = messages;
                                messageAdapter = new MessageAdapter(this, messageList);
                                recyclerViewListMessage.setAdapter(messageAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server: ", Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}