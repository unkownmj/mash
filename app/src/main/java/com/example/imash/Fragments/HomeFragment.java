package com.example.imash.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.imash.Adapter.PostAdapter;
import com.example.imash.Adapter.StoryAdapter;
import com.example.imash.Model.BattlePost;
import com.example.imash.Model.BattleReciverPost;
import com.example.imash.Model.Post;
import com.example.imash.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private RecyclerView recyclerViewstroy;
    private List<BattlePost> battlePostlist;
    private StoryAdapter stroryAdapter;
    ImageView icInbox;

    private List<String> followingList;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerViewPosts.setAdapter(postAdapter);



     //   recyclerViewstroy = view.findViewById(R.id.recycler_view_story);
     //   recyclerViewstroy.setHasFixedSize(true);
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.setReverseLayout(true);
        //recyclerViewstroy.setLayoutManager(linearLayoutManager);
        //battlePostlist = new ArrayList<>();
        //stroryAdapter = new StoryAdapter(getContext(),battlePostlist);
        //recyclerViewstroy.setAdapter(stroryAdapter);



        icInbox = (ImageView)view.findViewById(R.id.icInbox);

        followingList = new ArrayList<>();

        checkFollowingUsers();
        icInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBattle();
            }
        });

        return view;

    }

    private void startBattle(){
        ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchOpponentFragment()).commit();
        // Intent battleIntent = new Intent(getContext(), BattleActivity.class);
        //startActivity(battleIntent);
    }


    private void checkFollowingUsers() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPosts();
               //readBattle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readPosts() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)) {
                            postList.add(post);
                        }
                    }
               }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void  readBattle(){
        FirebaseDatabase.getInstance().getReference().child("PostsB").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  battlePostlist.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){

                  //  if (snapshot.exists()) {
                        BattlePost bPost = snapshot1.getValue(BattlePost.class);
                        //  ye bhi try krna hand  Notification fragment se dekh k kya h
                      //  for (String id : followingList){
                            battlePostlist.add(bPost);
                        //    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(id)) {

                        //    }

                     //  }
                   // }


                }
                stroryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}