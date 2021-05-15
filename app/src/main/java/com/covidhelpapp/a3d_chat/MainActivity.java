package com.covidhelpapp.a3d_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import soup.neumorphism.NeumorphImageView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.covidhelpapp.a3d_chat.Fragments.ChatsFragment;
import com.covidhelpapp.a3d_chat.Fragments.RequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    NeumorphImageView toolMenu;
    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    boolean doubleBackToExitPressedOnce = false;
    TextView mainText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolMenu = findViewById(R.id.tool_menu);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        mainText = findViewById(R.id.mainText);




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.chats_menu:
                        fragment = new ChatsFragment();
                        break;

                    case R.id.request_menu:
                        fragment = new RequestsFragment();
                        break;


                }

                if(fragment == null)
                {
                    fragment = new ChatsFragment();

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragment).commit();




                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new ChatsFragment()).commit();


        toolMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),toolMenu,Gravity.END);

                popupMenu.getMenuInflater().inflate(R.menu.tool_menu,popupMenu.getMenu());


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){

                            case R.id.settings:
                                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                                        Pair.<View, String>create(mainText,"text_trans"));

                                startActivity(intent,options.toBundle());
                                break;

                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(MainActivity.this, "Logged Out Successfully..", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(MainActivity.this,OpenActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
                                finish();
                                break;


                            case R.id.find_friends:
                                Intent intent2 = new Intent(MainActivity.this,FindFriendsActivity.class);
                                startActivity(intent2);
                                break;



                        }

                        return true;
                    }
                });

                popupMenu.show();




            }
        });
    }


}