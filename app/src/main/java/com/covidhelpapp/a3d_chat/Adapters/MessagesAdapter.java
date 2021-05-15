package com.covidhelpapp.a3d_chat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.covidhelpapp.a3d_chat.Models.Messages;
import com.covidhelpapp.a3d_chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import soup.neumorphism.NeumorphCardView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private Context mContext;
    private List<Messages> messagesList;

    public MessagesAdapter(Context mContext, List<Messages> messagesList) {
        this.mContext = mContext;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.messages_layout,parent,false);

        return new MessagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Messages messages = messagesList.get(position);

        final String receiverId = messages.getTo().toString();
        final String senderId = messages.getFrom().toString();
        final String messageId = messages.getMessageId().toString();

        FirebaseDatabase.getInstance().getReference().child("Users").child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String profileImageId = dataSnapshot.child("profileImage").getValue().toString();

                Picasso.get().load(profileImageId).placeholder(R.drawable.default_user_image).into(holder.profileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.profileImage.setVisibility(View.GONE);
        holder.senderMessage.setVisibility(View.GONE);
        holder.receiversMessage.setVisibility(View.GONE);

        if(firebaseUser.getUid().equals(senderId))
        {
            holder.senderMessage.setText(messages.getMessage());
            holder.senderMessage.setVisibility(View.VISIBLE);
            holder.receiversMessage.setVisibility(View.GONE);
            holder.profileImage.setVisibility(View.GONE);
            holder.receiveNeumorph.setVisibility(View.GONE);
        }
        else
        {
            holder.receiversMessage.setText(messages.getMessage());
            holder.profileImage.setVisibility(View.VISIBLE);
            holder.receiversMessage.setVisibility(View.VISIBLE);
            holder.senderMessage.setVisibility(View.INVISIBLE);
            holder.sendNeumorph.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profileImage;
        public TextView senderMessage;
        public TextView receiversMessage;
        public NeumorphCardView sendNeumorph,receiveNeumorph;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            senderMessage = itemView.findViewById(R.id.sender_message);
            receiversMessage = itemView.findViewById(R.id.receiver_message);
            sendNeumorph = itemView.findViewById(R.id.send_neumorph);
            receiveNeumorph = itemView.findViewById(R.id.receive_neumorph);

        }
    }

}
