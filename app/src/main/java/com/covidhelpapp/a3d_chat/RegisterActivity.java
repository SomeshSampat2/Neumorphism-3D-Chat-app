package com.covidhelpapp.a3d_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import soup.neumorphism.NeumorphButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private NeumorphButton register;
    private EditText email,password;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private LinearLayout layout_main;
    private ProgressDialog pd;

    Animation animation_fade_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Utils.blackIconStatusBar(this,R.color.light_background);


        layout_main = findViewById(R.id.layout_main);

        register = findViewById(R.id.register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_txt = email.getText().toString();
                String password_txt = password.getText().toString();

                if(email_txt.isEmpty() || password_txt.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Please Enter Both Email and Passowrd ..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pd.setTitle("Please wait...");
                    pd.setMessage("Registering");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();

                    registeruser(email_txt,password_txt);
                }




            }
        });

    }

    private void registeruser(final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            pd.dismiss();


                            Toast.makeText(RegisterActivity.this, "You are registered successfully", Toast.LENGTH_SHORT).show();



                            Map<String,Object> map = new HashMap<>();

                            map.put("email",email);
                            map.put("password",password);
                            map.put("userId",mAuth.getCurrentUser().getUid());
                            map.put("username","".toLowerCase());
                            map.put("status","");
                            map.put("profileImage","default");


                            usersRef.child(mAuth.getCurrentUser().getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Info Updated Sucessfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(RegisterActivity.this,SettingsActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    pd.dismiss();
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });






                        } else {

                            pd.dismiss();

                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
}