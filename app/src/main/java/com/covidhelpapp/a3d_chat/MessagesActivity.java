package com.covidhelpapp.a3d_chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import soup.neumorphism.NeumorphButton;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.covidhelpapp.a3d_chat.Adapters.MessagesAdapter;
import com.covidhelpapp.a3d_chat.Models.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    private String receiversId;
    private String receiversUsername;
    private String receiversProfileImage;
    private String receiversUserStatus;

    private EditText messageText;
    private NeumorphButton sendMessageButton;
    private CircleImageView profileImage;
    private TextView username;
    private TextView userStatus;

    private RecyclerView recyclerViewMessages;
    private MessagesAdapter messagesAdapter;
    private List<Messages> messagesList;

    private RelativeLayout top;


    DatabaseReference reference;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        receiversId = getIntent().getExtras().get("receivers_id").toString();
        receiversProfileImage = getIntent().getExtras().get("receivers_profile_image").toString();
        receiversUsername = getIntent().getExtras().get("receivers_username").toString();
        receiversUserStatus = getIntent().getExtras().get("receivers_user_status").toString();

        messageText = findViewById(R.id.message_text);
        sendMessageButton = findViewById(R.id.send_button);
        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        userStatus = findViewById(R.id.user_status);
        top = findViewById(R.id.top);

        recyclerViewMessages = findViewById(R.id.recycler_view_messages);
        recyclerViewMessages.setHasFixedSize(true);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(MessagesActivity.this));
        messagesList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(MessagesActivity.this,messagesList);
        recyclerViewMessages.setAdapter(messagesAdapter);


        getReceiverDetails();
        getMessages();
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessagesActivity.this,SettingsUsersActivity.class);

                intent.putExtra("username",username.getText().toString());
                intent.putExtra("status",userStatus.getText().toString());
                intent.putExtra("profileImage",receiversProfileImage);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MessagesActivity.this,
                        Pair.<View, String>create(username, "user_trans"),
                        Pair.<View, String>create(userStatus, "status_trans"),
                        Pair.<View, String>create(profileImage, "profile_trans"));

              startActivity(intent, options.toBundle());
            }
        });



    }



    private void getMessages() {

        FirebaseDatabase.getInstance().getReference().child("Messages").child(firebaseUser.getUid()).child(receiversId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.exists()) {

                            Messages messages = dataSnapshot.getValue(Messages.class);
                            messagesList.add(messages);
                            messagesAdapter.notifyDataSetChanged();
                            recyclerViewMessages.smoothScrollToPosition(recyclerViewMessages.getAdapter().getItemCount());
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void sendMessage() {

        String sender_message = messageText.getText().toString();

        if(TextUtils.isEmpty(sender_message))
        {
            Toast.makeText(this, "Please Enter the Message", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference messageRef =  FirebaseDatabase.getInstance().getReference().child("Messages").child(firebaseUser.getUid())
                    .child(receiversId).push();

            final String messagepushId = messageRef.getKey();

            HashMap<String,Object> map = new HashMap<>();
            map.put("from",firebaseUser.getUid());
            map.put("to",receiversId);
            map.put("messageId",messagepushId);
            map.put("message",sender_message);

            messageRef.setValue(map);

            FirebaseDatabase.getInstance().getReference().child("Messages").child(receiversId)
                    .child(firebaseUser.getUid()).child(messagepushId).setValue(map);

            messageText.setText("");

        }

    }

    private void getReceiverDetails() {
        Picasso.get().load(receiversProfileImage).into(profileImage);
        username.setText(receiversUsername);
        userStatus.setText(receiversUserStatus);
    }
}