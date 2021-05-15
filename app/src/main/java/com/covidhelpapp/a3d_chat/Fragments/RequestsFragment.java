package com.covidhelpapp.a3d_chat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.covidhelpapp.a3d_chat.Adapters.RequestAdapter;
import com.covidhelpapp.a3d_chat.Models.User;
import com.covidhelpapp.a3d_chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RequestsFragment extends Fragment {


    private RecyclerView recyclerViewRequests;
    private RequestAdapter requestAdapter;
    private List<User> userList;
    private TextView noReq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        recyclerViewRequests = view.findViewById(R.id.recyclerview_requests);
        noReq = view.findViewById(R.id.no_req_text);


        userList = new ArrayList<>();

        recyclerViewRequests.setHasFixedSize(true);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(getContext()));

        requestAdapter = new RequestAdapter(getContext(),userList);

        recyclerViewRequests.setAdapter(requestAdapter);

        readRequests();







        return view;
    }

    private void readRequests() {

        final List<String> requestIds = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Requested from").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                       requestIds.add(dataSnapshot.getKey());
                    }

                    FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {

                            userList.clear();

                            for (DataSnapshot dataSnapshot1:snapshot1.getChildren())
                            {
                                User user = dataSnapshot1.getValue(User.class);
                                for(String ids:requestIds)
                                {
                                    if (ids.equals(user.getUserId())){

                                        userList.add(user);
                                    }
                                }
                            }
                            requestAdapter.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}