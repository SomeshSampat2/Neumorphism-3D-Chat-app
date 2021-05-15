package com.covidhelpapp.a3d_chat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.covidhelpapp.a3d_chat.Models.User;
import com.covidhelpapp.a3d_chat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    private FirebaseUser firebaseUser;

    public RequestAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_request_layout,parent,false);

        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);

        holder.username.setText(user.getUsername());
        holder.status.setText(user.getStatus());

        Picasso.get().load(user.getProfileImage()).placeholder(R.drawable.default_user_image).into(holder.profileImage);


        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("Friends")
                        .child(firebaseUser.getUid()).child("FriendList").child(user.getUserId())
                        .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(mContext, "You and "+user.getUsername()+" are Friends Now ..", Toast.LENGTH_LONG).show();

                       holder.itemView.setVisibility(View.GONE);

                        FirebaseDatabase.getInstance().getReference().child("Friends")
                                .child(user.getUserId()).child("FriendList").child(firebaseUser.getUid()).setValue(true);

                        FirebaseDatabase.getInstance().getReference().child("Requests").child(user.getUserId())
                                .child("Requested to").child(firebaseUser.getUid()).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("Requests").child(firebaseUser.getUid())
                                .child("Requested from").child(user.getUserId()).removeValue();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "Rejected.. ", Toast.LENGTH_SHORT).show();

                holder.itemView.setVisibility(View.GONE);


                FirebaseDatabase.getInstance().getReference().child("Requests").child(user.getUserId())
                        .child("Requested to").child(firebaseUser.getUid()).removeValue();

                FirebaseDatabase.getInstance().getReference().child("Requests").child(firebaseUser.getUid())
                        .child("Requested from").child(user.getUserId()).removeValue();


            }
        });



    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView profileImage;
        public TextView username,status;
        public ImageButton acceptBtn,rejectBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            status = itemView.findViewById(R.id.status);
            acceptBtn = itemView.findViewById(R.id.accept_request);
            rejectBtn = itemView.findViewById(R.id.reject_request);



        }
    }

}
