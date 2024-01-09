package com.hoangquocthai.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hoangquocthai.chatapp.activity.ChatActivity;
import com.hoangquocthai.chatapp.R;
import com.hoangquocthai.chatapp.dto.GroupChat;
import com.hoangquocthai.chatapp.event.ItemClickListerner;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<GroupChat> groupChats;

    public GroupAdapter(Context context, List<GroupChat> groupChats){
        this.groupChats = groupChats;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item,parent,false);
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHoder myViewHoder = (MyViewHoder) holder;
        GroupChat groupChat = groupChats.get(position);
        myViewHoder.groupName.setText(groupChat.getGroupName());
        Glide.with(context).load(groupChat.getGroupAvatar()).into(myViewHoder.imgGroup);

        //clcik mỗi item
        myViewHoder.setItemClickListerner(new ItemClickListerner() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {

                    Intent intent = new Intent(context.getApplicationContext(), ChatActivity.class);
                    intent.putExtra("groupChat", groupChat.getGroupId());

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }


    public class MyViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView groupName;
        CircleImageView imgGroup;

        private ItemClickListerner itemClickListerner;


        public MyViewHoder(@NonNull View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.groupName);
            imgGroup = itemView.findViewById(R.id.imgGroup);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListerner(ItemClickListerner itemClickListerner) {
            this.itemClickListerner = itemClickListerner;
        }

        @Override
        public void onClick(View view) {
            itemClickListerner.onClick(view,getAdapterPosition(),false);
        }
    }
}
