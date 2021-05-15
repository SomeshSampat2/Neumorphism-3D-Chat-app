package com.covidhelpapp.a3d_chat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;

public class OpenActivity extends AppCompatActivity {

    private View img_bottom,tv_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        Utils.blackIconStatusBar(OpenActivity.this,R.color.light_background);


        img_bottom = findViewById(R.id.img_bottom);
        tv_name = findViewById(R.id.tv_name);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(OpenActivity.this,StartActivity.class);

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OpenActivity.this,
                                Pair.create(img_bottom,"img_tree"),
                                Pair.create(tv_name,"logo_text"));

                        startActivity(intent,options.toBundle());


                    }
                },1000);

            }




    }

