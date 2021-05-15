package com.covidhelpapp.a3d_chat.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.covidhelpapp.a3d_chat.ImageActivity;
import com.covidhelpapp.a3d_chat.Models.User;
import com.covidhelpapp.a3d_chat.R;
import com.covidhelpapp.a3d_chat.SettingsActivity;
import com.covidhelpapp.a3d_chat.SettingsUsersActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUsers;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.find_friends_layout, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Friends").child(firebaseUser.getUid());


        final User user = mUsers.get(position);

        holder.username.setText(user.getUsername());
        holder.status.setText(user.getStatus());

        Picasso.get().load(user.getProfileImage()).placeholder(R.drawable.default_user_image).into(holder.profileImage);



        isRequested(user.getUserId(),holder.btnRequest);
        if(user.getUserId().equals(firebaseUser.getUid()))
        {
            holder.btnRequest.setVisibility(View.GONE);
        }
        else
        {
            holder.btnRequest.setVisibility(View.VISIBLE);
        }



        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!user.getUserId().equals(firebaseUser.getUid()))
                {


                Intent intent = new Intent(mContext, SettingsUsersActivity.class);
                intent.putExtra("username", user.getUsername());
                intent.putExtra("status", user.getStatus());
                intent.putExtra("profileImage", user.getProfileImage());

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                        Pair.<View, String>create(holder.username, "user_trans"),
                        Pair.<View, String>create(holder.status, "status_trans"),
                        Pair.<View, String>create(holder.profileImage, "profile_trans"),
                        Pair.<View, String>create(holder.btnRequest, "but_trans"));

                mContext.startActivity(intent, options.toBundle());
              }

            }
        });

        reference.child("FriendList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUserId()))
                {
                    holder.btnRequest.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btnRequest.getText().toString().equals("request"))
                {


                    FirebaseDatabase.getInstance().getReference().child("Requests")
                            .child(firebaseUser.getUid()).child("Requested to").child(user.getUserId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Requests")
                            .child(user.getUserId()).child("Requested from").child(firebaseUser.getUid()).setValue(true);

                }
                else
                {



                    FirebaseDatabase.getInstance().getReference().child("Requests")
                            .child(firebaseUser.getUid()).child("Requested to").child(user.getUserId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Requests")
                            .child(user.getUserId()).child("Requested from").child(firebaseUser.getUid()).removeValue();

                }
            }
        });

        if(user.getUserId().equals(firebaseUser.getUid()))
        {
            holder.btnRequest.setVisibility(View.GONE);
        }
        else
        {
            holder.btnRequest.setVisibility(View.VISIBLE);
        }




    }

    private void isRequested(final String userId, final Button btnRequest) {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Requests")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("Requested to").child(userId).exists() || dataSnapshot.child("Requested from").child(userId).exists()) {

                        btnRequest.setText("requested");
                        btnRequest.setBackgroundResource(R.drawable.button_background_requested);

                    }
                    else
                    {
                        btnRequest.setText("request");
                        btnRequest.setBackgroundResource(R.drawable.button_background);
                    }
                }
                else
                {
                    btnRequest.setText("request");
                    btnRequest.setBackgroundResource(R.drawable.button_background);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView username,status;
        public CircleImageView profileImage;
        public Button btnRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.username);
            status = itemView.findViewById(R.id.status);
            profileImage = itemView.findViewById(R.id.profile_image);
            btnRequest = itemView.findViewById(R.id.btn_request);



        }
    }

}
