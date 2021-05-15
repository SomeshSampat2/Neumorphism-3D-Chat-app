package com.covidhelpapp.a3d_chat.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.covidhelpapp.a3d_chat.MessagesActivity;
import com.covidhelpapp.a3d_chat.Models.User;
import com.covidhelpapp.a3d_chat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUser;

    public ChatsAdapter(Context mContext, List<User> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_layout,parent,false);

        return new ChatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final User user = mUser.get(position);


        holder.status.setText(user.getStatus());
        holder.username.setText(user.getUsername());
        Picasso.get().load(user.getProfileImage()).placeholder(R.drawable.default_user_image).into(holder.profileImage);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(mContext, MessagesActivity.class);
               intent.putExtra("receivers_id",user.getUserId());
               intent.putExtra("receivers_username",user.getUsername());
               intent.putExtra("receivers_profile_image",user.getProfileImage());
               intent.putExtra("receivers_user_status",user.getStatus());

               ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                       Pair.<View, String>create(holder.username,"username_trans"),
                        Pair.<View, String>create(holder.status,"status_trans"),
                       Pair.<View, String>create(holder.profileImage,"profile_trans"));

               mContext.startActivity(intent,options.toBundle());

           }
       });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profileImage;
        public TextView username;
        public TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            status = itemView.findViewById(R.id.status);
        }
    }


}
