package com.example.devs_hackathon_2023;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import android.util.Pair;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

import com.example.devs_hackathon_2023.activities.ShopActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;


import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.devs_hackathon_2023.databinding.ActivityMainBinding;

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
    }

    private void replaceFragment(int layoutResId) {
        View view = getLayoutInflater().inflate(layoutResId, null);
        FrameLayout container = findViewById(R.id.fragment_container);
        container.removeAllViews();
        container.addView(view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    @Override
    protected void onResume() {
        super.onResume();

    }
}