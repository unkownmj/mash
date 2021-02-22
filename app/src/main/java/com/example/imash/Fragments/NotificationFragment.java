package com.example.imash.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imash.Adapter.BattleNotificationAdapter;
import com.example.imash.Adapter.NotificationAdapter;
import com.example.imash.Model.BattleNotification;
import com.example.imash.Model.Notification;
import com.example.imash.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;

    FirebaseAuth fbtlUer;


    private RecyclerView recyclerviewbr;
    private BattleNotificationAdapter bNotificationAdapter;
    private  List<BattleNotification> bNotificationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

     //   recyclerView = view.findViewById(R.id.recycler_view);
      //  recyclerView.setHasFixedSize(true);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //notificationList = new ArrayList<>();
       // notificationAdapter = new NotificationAdapter(getContext(), notificationList);
       // recyclerView.setAdapter(notificationAdapter);

        fbtlUer = FirebaseAuth.getInstance();


        recyclerviewbr = view.findViewById(R.id.bc_recyclerview);
        recyclerviewbr.setHasFixedSize(true);
        recyclerviewbr.setLayoutManager(new LinearLayoutManager(getContext()));
        bNotificationList = new ArrayList<>();
        bNotificationAdapter = new BattleNotificationAdapter(getContext(),bNotificationList);
        recyclerviewbr.setAdapter(bNotificationAdapter);

        readBtlNotification();
     //   readNotifications();

        return view;
    }

    private void  readBtlNotification(){



        FirebaseDatabase.getInstance().getReference().child("NotificationsBtl").child(fbtlUer.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 :  snapshot.getChildren()){
                    bNotificationList.add(snapshot1.getValue(BattleNotification.class));
                }
               Collections.reverse(bNotificationList);
                bNotificationAdapter.notifyDataSetChanged();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

 //   private void readNotifications() {



     //   FirebaseDatabase.getInstance().getReference().child("Notifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
          //  @Override
          //  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                //    notificationList.add(snapshot.getValue(Notification.class));
              //  }

              //  Collections.reverse(notificationList);
           //     notificationAdapter.notifyDataSetChanged();
          //  }

          //  @Override
          //  public void onCancelled(@NonNull DatabaseError databaseError) {

          //  }
     //   });

   // }
}