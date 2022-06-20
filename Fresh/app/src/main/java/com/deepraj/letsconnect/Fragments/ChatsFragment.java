package com.deepraj.letsconnect.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.deepraj.letsconnect.Adapter.UsersAdapter;
import com.deepraj.letsconnect.Models.Users;
import com.deepraj.letsconnect.R;
import com.deepraj.letsconnect.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

    binding = FragmentChatsBinding.inflate(inflater,container,false);
    database = FirebaseDatabase.getInstance();

    UsersAdapter adapter = new UsersAdapter(list, getContext());
    binding.chatRecylerView.setAdapter(adapter);

        LinearLayoutManager layoutManager  = new LinearLayoutManager(getContext());
        binding.chatRecylerView.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());

                    //to hide the user chat that has logged inside the app
                    if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                        list.add(users);
                    }


                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}