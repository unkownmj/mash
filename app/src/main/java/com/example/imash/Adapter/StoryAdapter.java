package com.example.imash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imash.Model.BattlePost;
import com.example.imash.Model.BattleReciverPost;
import com.example.imash.Model.User;
import com.example.imash.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.Viewholder> {

    private Context mContext;
    private List<BattlePost> mStory;


    private FirebaseUser firebaseUser;

    public StoryAdapter(Context mContext, List<BattlePost> mStory) {
        this.mContext = mContext;
        this.mStory = mStory;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public StoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.battlepost,parent,false);
        return new StoryAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryAdapter.Viewholder holder, int position) {

        final BattlePost bpost = mStory.get(position);

        Picasso.get().load(bpost.getImageurl2()).into(holder.postImage2);
        Picasso.get().load(bpost.getImageurl1()).into(holder.postImage1);

        FirebaseDatabase.getInstance().getReference().child("Users").child(bpost.getPublisher1()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user.getImageurl().equals("default")){
                   // holder.imageProfile1.setImageResource(R.mipmap.ic_launcher);
                    holder.imageProfile1.setImageResource(R.mipmap.ic_launcher);
                }else  {
                   // Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile1);
                    Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile2);
                }
                holder.username1.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(bpost.getPublisher2()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user.getImageurl().equals("default")){
                    holder.imageProfile2.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile1);
                }
                holder.username2.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mStory.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public ImageView imageProfile1;
        public ImageView imageProfile2;
        public ImageView postImage1;
        public  ImageView postImage2;
        public ImageView like1;
        public  ImageView like2;
        public ImageView cmment;
        public ImageView save;


        public TextView username1;
        public TextView username2;



        public Viewholder(View view) {
            super(view);

            imageProfile1 = view.findViewById(R.id.image_profile1);
            imageProfile2 = view.findViewById(R.id.image_profile2);
            postImage1 = view.findViewById(R.id.post_image1);
            postImage2 = view.findViewById(R.id.post_image2);
            like1 = view.findViewById(R.id.like1);
            like2 = view.findViewById(R.id.like2);
            cmment = view.findViewById(R.id.comment_btl);
            save = view.findViewById(R.id.save_btl);
            username1 = view.findViewById(R.id.username1);
            username2 = view.findViewById(R.id.username2);

        }
    }
}
