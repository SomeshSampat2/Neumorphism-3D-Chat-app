package com.covidhelpapp.a3d_chat;

import androidx.appcompat.app.AppCompatActivity;
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphImageView;

import android.os.Bundle;
import android.widget.EditText;

import com.squareup.picasso.Picasso;

public class SettingsUsersActivity extends AppCompatActivity {

    private EditText username,status;
    private NeumorphButton save;
    private NeumorphImageView profileImage;

    String username_txt,status_txt,profileImage_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_users);

        username_txt = getIntent().getExtras().get("username").toString();
       status_txt = getIntent().getExtras().get("status").toString();
        profileImage_txt = getIntent().getExtras().get("profileImage").toString();

        username = findViewById(R.id.username);
        status = findViewById(R.id.status);
        save = findViewById(R.id.save);
        profileImage = findViewById(R.id.profile_image);


        username.setText(username_txt);
        status.setText(status_txt);

        Picasso.get().load(profileImage_txt).into(profileImage);

    }
}