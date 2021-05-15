package com.covidhelpapp.a3d_chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphImageView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private EditText username,status;
    private NeumorphButton save;
    private NeumorphImageView profileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private StorageReference userProfileRef;

    private ProgressDialog pd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        username = findViewById(R.id.username);
        status = findViewById(R.id.status);
        save = findViewById(R.id.save);
        profileImage = findViewById(R.id.profile_image);
        pd = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userProfileRef = FirebaseStorage.getInstance().getReference().child("profileImages");


        readUserData();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.setTitle("Please wait...");
                pd.setMessage("Updating");
                pd.setCanceledOnTouchOutside(false);
                pd.show();

                String username_txt = username.getText().toString();
                String status_txt = status.getText().toString();


                Map<String,Object> map = new HashMap<>();
                map.put("username",username_txt.toLowerCase());
                map.put("status",status_txt.toLowerCase());

                userRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully..", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {


            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {

                pd.setTitle("Please wait...");
                pd.setMessage("Updating Image");
                pd.setCanceledOnTouchOutside(false);
                pd.show();


                Uri resultUri = result.getUri();

                 final StorageReference filepath = userProfileRef.child(mAuth.getCurrentUser().getUid());


                 filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                             @Override
                             public void onSuccess(Uri uri) {

                                 String downloadUrl = uri.toString();

                                 userRef.child(mAuth.getCurrentUser().getUid()).child("profileImage").setValue(downloadUrl)
                                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void aVoid) {

                                         pd.dismiss();

                                         Toast.makeText(SettingsActivity.this, "Image Updated Successfully..", Toast.LENGTH_SHORT).show();

                                     }
                                 }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         pd.dismiss();
                                         Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                     }
                                 });

                                 }

                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 pd.dismiss();
                                 Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                             }
                         });
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         pd.dismiss();
                         Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                     }
                 });




            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {


                Exception error = result.getError();
            }
        }
    }

    private void readUserData() {


        userRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

              if(snapshot.exists())
              {
                   String retrieve_username = snapshot.child("username").getValue().toString();
                   String retrieve_status = snapshot.child("status").getValue().toString();
                  String profileImageUri = snapshot.child("profileImage").getValue().toString();


                  if(snapshot.child("profileImage").getValue() != "default")
                   {
                       Picasso.get().load(profileImageUri).placeholder(R.drawable.default_user_image).into(profileImage);

                   }

                   else
                   {
                       Picasso.get().load(R.drawable.default_user_image).into(profileImage);
                   }


                   username.setText(retrieve_username);
                   status.setText(retrieve_status);

              }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(SettingsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

         Intent intent = new Intent(SettingsActivity.this,MainActivity.class);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SettingsActivity.this,
                Pair.<View, String>create(username, "user_trans"),
                Pair.<View, String>create(status, "status_trans"),
                Pair.<View, String>create(profileImage, "profile_trans"));

         startActivity(intent,options.toBundle());
    }
}