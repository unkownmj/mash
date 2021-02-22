package com.example.imash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imash.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BattleActivity extends AppCompatActivity {

    private Uri imageUriBtl;
    private  String imageUrlBtl;

    private ImageView closeBtl;
    private ImageView imageAddedBtl;
    private TextView postBtl;
    SocialAutoCompleteTextView descriptionBtl;





    private FirebaseUser fUser;
    private CircleImageView opponentProfileImage;
    private  TextView opponentUsername;
    private TextView  opponentFullname;

    String profileId;

  //  Intent intent = this.getIntent();
  //  String id = intent.getStringExtra("id");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);


        String data = getBaseContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId","none");

        if (data.equals("none")){
            profileId= fUser.getUid();
        }else {
            profileId = data;
        }
        //   Intent intent = this.getIntent();
        // String id = intent.getStringExtra("id");

        closeBtl = (ImageView)findViewById(R.id.close_btl);
        imageAddedBtl= (ImageView)findViewById(R.id.image_added_btl);
        postBtl  = (TextView)findViewById(R.id.post_btl);
      //  descriptionBtl = findViewById(R.id.description_btl);

        fUser= FirebaseAuth.getInstance().getCurrentUser();



        opponentProfileImage =(CircleImageView) findViewById(R.id.image_profile_op);
        opponentUsername = (TextView) findViewById(R.id.username_op);
        opponentFullname =(TextView) findViewById(R.id.fullname_op);


        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getImageurl().equals("default")){
                    opponentProfileImage.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Picasso.get().load(user.getImageurl()).into(opponentProfileImage);
                }

                opponentFullname.setText(user.getName());
                opponentUsername.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        closeBtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(BattleActivity.this,MainActivity.class));
                finish();
            }
        });

        postBtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadBattle();

                // Intent opponentInetnt = new Intent(BattleActivity.this,SearchOpponentFragment.class);
                // startActivity(opponentInetnt);

            }
        });
        CropImage.activity().start(BattleActivity.this);
    }


    private void uploadBattle(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUriBtl!= null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("PostsB").child(fUser.getUid()).child(System.currentTimeMillis() + "" + getFileExtention(imageUriBtl));
            StorageTask uploadTask = filePath.putFile(imageUriBtl);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return  filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrlBtl = downloadUri.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("PostsB").child(fUser.getUid());
                    String postID = ref.push().getKey();



                    //map.put("publisher2",p2Id);
                    //   map.put("imageurl2",url );

                   // String receiverid = profileId.toString();

                    String sender_id = fUser.getUid().toString();
                    //if (sender_id != null){
                    //    sender_id = fUser.getUid().toString();
                   // }else{


                   // }

                    HashMap<String,Object> map = new HashMap<>();
                    map.put("postid1", postID);
                    map.put("imageurl1",imageUrlBtl);
                    map.put("publisher1", sender_id);



                    ref.child(postID).setValue(map);


                    pd.dismiss();
                    startActivity(new Intent(BattleActivity.this,MainActivity.class));

                    if (task.isSuccessful()){
                       String reciver_idn = profileId.toString();
                       String sender_idn = fUser.getUid().toString();
                        sendBattleNotification(sender_idn,reciver_idn , postID);
                   } else {
                            Toast.makeText(BattleActivity.this, "uploading failed", Toast.LENGTH_SHORT).show();
                    }




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BattleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendBattleNotification(String sender_id, String reciver_id , String  post_id){

        HashMap <String,Object> map = new HashMap<>();
        map.put("sender_id",sender_id);
        map.put("post_id",post_id);
        map.put("reciver_id", reciver_id);

        FirebaseDatabase.getInstance().getReference().child("NotificationsBtl").child(reciver_id).push().setValue(map);



    }

    private String getFileExtention(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUriBtl = result.getUri();

            imageAddedBtl.setImageURI(imageUriBtl);
        }
        else {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BattleActivity.this,MainActivity.class));
            finish();
        }
    }
}