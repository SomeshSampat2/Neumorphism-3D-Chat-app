package com.covidhelpapp.a3d_chat;

import androidx.appcompat.app.AppCompatActivity;
import soup.neumorphism.NeumorphImageView;

import android.net.Uri;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    private NeumorphImageView profileImage;
    String profileImage_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        profileImage_txt = getIntent().getExtras().get("profileImage").toString();


        profileImage = findViewById(R.id.profile_image);

        Picasso.get().load(profileImage_txt).placeholder(R.drawable.google_logo).into(profileImage);
    }
}