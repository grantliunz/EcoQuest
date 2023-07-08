package com.example.devs_hackathon_2023;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.FrameLayout;


import com.example.devs_hackathon_2023.User.MainPlayer;
import com.example.devs_hackathon_2023.User.Database;
import com.example.devs_hackathon_2023.fragments.Quests;
import com.example.devs_hackathon_2023.fragments.Social;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import androidx.annotation.NonNull;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.example.devs_hackathon_2023.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private View boundedBox;



    private Map map = new Map();
    private Quests quests = new Quests();
    private Social social = new Social();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            Database.setupDatabase(getAssets().open("players.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, map).commit();
        navView.setSelectedItemId(R.id.map_button);
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.task_button) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, quests).commit();
                    return true;
                } else if (item.getItemId() == R.id.map_button) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, map).commit();
                    return true;
                } else if (item.getItemId() == R.id.social_button) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, social).commit();
                    return true;
                }
                return false;
            }
        });
        setUpMainPlayer();
        // set a target location
        map.setTargetLocation(-36.8509, 174.7719);
    }

    private void replaceFragment(int layoutResId) {
        View view = getLayoutInflater().inflate(layoutResId, null);
        FrameLayout container = findViewById(R.id.fragment_container);
        container.removeAllViews();
        container.addView(view);

    }

    private void setUpMainPlayer() {

        MainPlayer.setProfilePicture(R.drawable.aaron_icon);
        MainPlayer.setName("John Doe");
        MainPlayer.setScore(30);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }



    @Override
    protected void onResume() {
        super.onResume();

    }
}