package com.hoangquocthai.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoangquocthai.chatapp.R;
import com.hoangquocthai.chatapp.object.Message;
import com.hoangquocthai.chatapp.retrofit.Server;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Message> messageList;

    private Context context;

    public MessageAdapter(Context context, List<Message> messages){
        this.context = context;
        this.messageList = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciver_layout,parent,false);
        return new MessageAdapter.MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MessageAdapter.MyViewHoder myViewHoder = (MessageAdapter.MyViewHoder) holder;
        Message message = messageList.get(position);
        myViewHoder.message.setText(message.getContent());

        if(message.getSender().getUsername().equals(Server.user.getUsername())) {
            myViewHoder.parentMessageItem.setGravity(RelativeLayout.ALIGN_RIGHT);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public class MyViewHoder extends RecyclerView.ViewHolder {
        TextView message;

        RelativeLayout parentMessageItem;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            parentMessageItem = itemView.findViewById(R.id.parentMessageItem);
        }
    }
}
