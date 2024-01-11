package com.hoangquocthai.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hoangquocthai.chatapp.R;
import com.hoangquocthai.chatapp.activity.CreateGroupActivity;
import com.hoangquocthai.chatapp.adapter.MessageAdapter;
import com.hoangquocthai.chatapp.dto.GroupChat;
import com.hoangquocthai.chatapp.dto.GroupChatRequestDto;
import com.hoangquocthai.chatapp.dto.MessageDTO;
import com.hoangquocthai.chatapp.object.Message;
import com.hoangquocthai.chatapp.object.User;
import com.hoangquocthai.chatapp.retrofit.ApiChat;
import com.hoangquocthai.chatapp.retrofit.RetrofitClient;
import com.hoangquocthai.chatapp.retrofit.Server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import tech.gusavila92.websocketclient.WebSocketClient;


import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewListMessage;
    private CardView imgSendMessage;
    private List<Message> messageList;
    private LinearLayoutManager linearLayoutManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiChat apiChat;
    private EditText edtMessage;
    private MessageAdapter messageAdapter;
    private WebSocketClient webSocketClient;
    private Gson g;
    private GroupChat groupChat;
    private PubNub pubnub;


    @Override
    protected void onDestroy() {
        super.onDestroy();

        webSocketClient.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        createWebSocketClient();
        initialView();

        Intent intent = getIntent();

        Long groupId = intent.getLongExtra("groupChat",-1);

        getData(groupId);

        compositeDisposable.add(apiChat.getGroupById(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        group -> {
                            if (group != null) {
                                groupChat = group;
                                getData(groupId);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server: ", Toast.LENGTH_SHORT).show();
                        }
                ));


        handleClickSendMessage();
    }

    private void handleClickSendMessage() {
        imgSendMessage.setOnClickListener((v -> {
            String msg = edtMessage.getText().toString();
            if(!msg.isEmpty()){
                Message message = new Message();
                message.setContent(msg);
                message.setIdMessage(null);
                message.setGroupChat(groupChat);
                message.setCreatedAt(new Date());
                message.setSender(Server.user);

                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setMessage(message);
                messageDTO.setUser(Server.user);

                webSocketClient.send(g.toJson(messageDTO));
                getData(groupChat.getGroupId());
                edtMessage.setText("");
                publishMessage(msg);
            }
        }));
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://" + Server.URL+"/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.d("socket", "socket open");
            }

            @Override
            public void onTextReceived(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getData(groupChat.getGroupId());

                    }
                });
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

                Log.e("socket", "Socket close"

                );
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(3000);

        webSocketClient.connect();

        initPubNub();

    }

    private void initPubNub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-aaff6267-c49d-4512-859a-f398baeb822c");
        pnConfiguration.setSubscribeKey("sub-c-44251ad0-cd2a-48c2-8d91-1b0300135651");
        pnConfiguration.setSecure(true);
        pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pub, PNStatus status) {
            }

            @Override
            public void message(PubNub pub, final PNMessageResult message) {
                final String msg = message.getMessage().toString().replace("\"", "");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getData(groupChat.getGroupId());
                        } catch (Exception e) {
                            System.out.println("Error");
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void presence(PubNub pub, PNPresenceEventResult presence) {
            }
        });

        // Subscribe to the global channel
        pubnub.subscribe()
                .channels(Arrays.asList("global_channel"))
                .execute();
    }

    public void publishMessage(String animal_sound){
        // Publish message to the global chanel
        pubnub.publish()
                .message(animal_sound)
                .channel("global_channel")
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if(status.isError()) {
                            getData(groupChat.getGroupId());
                        }
                    }
                });
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
                                messageAdapter = new MessageAdapter(this, messages);
                                recyclerViewListMessage.setAdapter(messageAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server: ", Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}