package com.covidhelpapp.a3d_chat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.covidhelpapp.a3d_chat.Adapters.ChatsAdapter;
import com.covidhelpapp.a3d_chat.Models.User;
import com.covidhelpapp.a3d_chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerViewChats;
    private List<User> userList;
    private ChatsAdapter chatsAdapter;

    DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_chats, container, false);


        reference = FirebaseDatabase.getInstance().getReference().child("Friends")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FriendList");

        recyclerViewChats = view.findViewById(R.id.recyclerview_chats);
        recyclerViewChats.setHasFixedSize(true);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        chatsAdapter = new ChatsAdapter(getContext(),userList);
        recyclerViewChats.setAdapter(chatsAdapter);

        getUsersList();





        return view;
    }

    private void getUsersList() {

        final List<String> friendsids = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    friendsids.add(snapshot.getKey());
                }

                FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                        userList.clear();

                        for(DataSnapshot snapshot1 : dataSnapshot1.getChildren())
                        {
                            User user = snapshot1.getValue(User.class);

                            for(String ids : friendsids)
                            {
                                if(user.getUserId().equals(ids))
                                {
                                    userList.add(user);
                                }
                            }
                        }

                        chatsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}