package com.example.imash.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.imash.Adapter.UserOpponentAdpater;
import com.example.imash.Model.UserOpponent;
import com.example.imash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class SearchOpponentFragment extends Fragment {


    private RecyclerView recyclerViewBtl;
    private List<UserOpponent> mUsers;
    private UserOpponentAdpater userAdapter;

    private SocialAutoCompleteTextView search_bar_btl;
    private ImageView serachImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_opponent, container, false);
        recyclerViewBtl = view.findViewById(R.id.recycler_view_users_btl);
        recyclerViewBtl.setHasFixedSize(true);
        recyclerViewBtl.setLayoutManager(new LinearLayoutManager(getContext()));

        //recyclerViewTags = view.findViewById(R.id.recycler_view_tags_btl);
        //recyclerViewTags.setHasFixedSize(true);
        //ecyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext()));

        //  mHashTags = new ArrayList<>();
        // mHashTagsCount = new ArrayList<>();
        // tagAdapter = new TagAdapter(getContext() , mHashTags , mHashTagsCount);
        // recyclerViewTags.setAdapter(tagAdapter);

        mUsers = new ArrayList<>();
        userAdapter = new UserOpponentAdpater(getContext() , mUsers , true);
        recyclerViewBtl.setAdapter(userAdapter);

        search_bar_btl = view.findViewById(R.id.search_bar_btl);
        serachImage = view.findViewById(R.id.search_icon);

        readUsers();
        //readTags();

        search_bar_btl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                //filter(s.toString());
            }
        });

        return view;
    }

    private void readUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (TextUtils.isEmpty(search_bar_btl.getText().toString())){
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UserOpponent user = snapshot.getValue(UserOpponent.class);
                        mUsers.add(user);
                    }

                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //  private void readTags(){
    // FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
    //    @Override
    //public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    //  mHashTags.clear();
    // mHashTagsCount.clear();

    //  for (DataSnapshot snapshot : dataSnapshot.getChildren()){
    // mHashTags.add(snapshot.getKey());
    //     mHashTagsCount.add(snapshot.getChildrenCount() + "");
    // }

    //  tagAdapter.notifyDataSetChanged();
    // }

    // @Override
    //     public void onCancelled(@NonNull DatabaseError databaseError) {

    //    }
    //  });

    //  }
    private void searchUser (String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("username").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserOpponent user = snapshot.getValue(UserOpponent.class);
                    mUsers.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // private void filter (String text) {
    //    List<String> mSearchTags = new ArrayList<>();
    //  List<String> mSearchTagsCount = new ArrayList<>();

    // for (String s : mHashTags) {
    //     if (s.toLowerCase().contains(text.toLowerCase())){
    //   mSearchTags.add(s);
    //  mSearchTagsCount.add(mHashTagsCount.get(mHashTags.indexOf(s)));
    // }
    //   }

    //  tagAdapter.filter(mSearchTags , mSearchTagsCount);
    // }

}

