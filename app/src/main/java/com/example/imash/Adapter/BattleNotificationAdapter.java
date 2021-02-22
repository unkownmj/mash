package com.example.imash.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imash.BattleActivityReciver;
import com.example.imash.Model.BattleNotification;
import com.example.imash.Model.User;
import com.example.imash.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BattleNotificationAdapter extends RecyclerView.Adapter<BattleNotificationAdapter.ViewHolder> {

    private Context mContext ;
    private List <BattleNotification> bNotification;

    public BattleNotificationAdapter(Context mContext, List<BattleNotification> bNotification) {
        this.mContext = mContext;
        this.bNotification = bNotification;
    }

    @NonNull
    @Override
    public BattleNotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(mContext).inflate(R.layout.battle_notification,parent,false);
        return new BattleNotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BattleNotificationAdapter.ViewHolder holder, int position) {
        final BattleNotification Notification = bNotification.get(position);
        getUser(holder.bImageProfile, holder.bUsername,holder.bFullname,Notification.getSender_id().toString());

        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                        .edit().putString("postid", Notification.getReciver_id()).apply();

                Intent intent = new Intent(mContext, BattleActivityReciver.class);
                intent.putExtra("postid1",Notification.getPost_id());
                mContext.startActivity(intent);

            }
        });

        holder.decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("NotificationsBtl").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
            }
        });

    }

    private void getUser(final ImageView imageView, TextView bUsername, final TextView textView, String sender_id) {

        FirebaseDatabase.getInstance().getReference().child("Users").child(sender_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImageurl().equals("default")) {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else
                   Picasso.get().load(user.getImageurl()).into(imageView);

                bUsername.setText(user.getUsername());
                textView.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return bNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView bImageProfile;
        public TextView bUsername;
        public  TextView bFullname;
        public Button accept_btn, decline_btn;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            bImageProfile = itemView.findViewById(R.id.image_profile_bn);
            bUsername = itemView.findViewById(R.id.username_bn);
            bFullname  = itemView.findViewById(R.id.fullname_bn);
            accept_btn = itemView.findViewById(R.id.accept_btn);
            decline_btn = itemView.findViewById(R.id.decline_btn);



        }
    }
}
