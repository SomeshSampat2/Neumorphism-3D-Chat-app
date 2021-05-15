package com.covidhelpapp.a3d_chat;

import androidx.appcompat.app.AppCompatActivity;
import soup.neumorphism.NeumorphButton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {


    private NeumorphButton login,register;
    private RelativeLayout layout_main;
    private Animation animation_fadein;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Utils.blackIconStatusBar(this,R.color.light_background);

        mAuth = FirebaseAuth.getInstance();

        layout_main = findViewById(R.id.layout_main);

        login = findViewById(R.id.login_start);
        register = findViewById(R.id.register_start);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this,LoginActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartActivity.this,
                        Pair.<View, String>create(login,"log_trans"));

                startActivity(intent,options.toBundle());

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,RegisterActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartActivity.this,
                        Pair.<View, String>create(register,"reg_trans"));

                startActivity(intent,options.toBundle());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            Intent intent = new Intent(StartActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
    }
}