package com.hoangquocthai.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hoangquocthai.chatapp.R;
import com.hoangquocthai.chatapp.event.ItemClickListerner;
import com.hoangquocthai.chatapp.object.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<User> users;
    private List<User> userSelected;

    public UsersAdapter(Context context, List<User> users, List<User> userSelected){
        this.users = users;
        this.context = context;
        this.userSelected = userSelected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usser_friend_item,parent,false);
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHoder myViewHoder = (MyViewHoder) holder;
        User user = users.get(position);
        myViewHoder.txtFullName.setText(user.getFullName());
        Glide.with(context).load(user.getUserAvatar()).into(myViewHoder.imgAvatarUser);

        myViewHoder.checkBoxUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHoder.checkBoxUser.setChecked(!(myViewHoder.checkBoxUser.isChecked()));
                if(!myViewHoder.checkBoxUser.isChecked()){
                    userSelected.add(user);
                    myViewHoder.checkBoxUser.setChecked(true);
                }else{
                    userSelected.remove(user);
                    myViewHoder.checkBoxUser.setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class MyViewHoder extends RecyclerView.ViewHolder {
        TextView txtFullName;
        CircleImageView imgAvatarUser;
        CheckBox checkBoxUser;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);

            txtFullName = itemView.findViewById(R.id.txtFullName);
            imgAvatarUser = itemView.findViewById(R.id.imgAvatarUser);
            checkBoxUser = itemView.findViewById(R.id.checkBoxUser);
        }
    }
}
