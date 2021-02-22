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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;


//  No use of this activity for you
public class BattleActivityReciver extends AppCompatActivity {


    private Uri imageUribr;
    public String imageUrlbr;

    private ImageView closebr;
    private ImageView imageAddedbr;
    private TextView postbr;
    private FirebaseUser fuserp2;


    //String recid;


    //Intent intent =this.getIntent();
 //   String postid1  = intent.getStringExtra("postid1");
   // String sender_id = intent.getStringExtra("sender_id").toString();
     // String  reciver_id = intent.getStringExtra("reciver_id").toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_reciver);

     //   fuserp2 = FirebaseAuth.getInstance().getCurrentUser();
       // String data = getBaseContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");
       // if (data.equals("none")) {
        //    recid = fuserp2.getUid();
        //} else {
         //   recid = data;
          //  getBaseContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        //}





        closebr = findViewById(R.id.close_br);
        imageAddedbr= findViewById( R.id.image_added_br);
        postbr = findViewById(R.id.post_br);


        closebr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BattleActivityReciver.this , MainActivity.class));
                finish();
            }
        });

        postbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        CropImage.activity().start(BattleActivityReciver.this);

    }

    private void upload() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUribr!= null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("PostsB").child(fuserp2.getUid()).child(System.currentTimeMillis() + "" + getFileExtention(imageUribr));
            StorageTask uploadTask = filePath.putFile(imageUribr);
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
                    imageUrlbr = downloadUri.toString();


                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("PostsB").child(fuserp2.getUid());
                    String postID = ref.push().getKey();







                    HashMap<String,Object> map = new HashMap<>();
                    // map.put("imageurl2",imageUrlbr);
                    // map.put("postid2",postid1);
                    // map.put("publisher1",sender_id);
                  //   map.put("publisher2",recid);



                    //ref.child(postid1).setValue(map);


                    pd.dismiss();
                    Intent braIntent = new Intent( BattleActivityReciver.this,MainActivity.class);
                  //  braIntent.putExtra("publisher2",recid);
                    startActivity(braIntent);
                    if (task.isSuccessful()){
                        FirebaseDatabase.getInstance().getReference("NotificationsBtl").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    }


                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BattleActivityReciver.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
        }
    }



    private String getFileExtention(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUribr = result.getUri();

            imageAddedbr.setImageURI(imageUribr);
        }
        else {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BattleActivityReciver.this,MainActivity.class));
            finish();
        }
    }
}