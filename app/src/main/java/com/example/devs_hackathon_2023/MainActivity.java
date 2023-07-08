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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

import com.example.devs_hackathon_2023.activities.ShopActivity;
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
    protected View circleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupShopButton();

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

    private void setupShopButton() {
        ImageView clickableImageView = findViewById(R.id.shopButton);
        clickableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                animateAndSwitch(v);
            }
        });
    }
    private void animateAndSwitch(View view) {
        // Get the click position
        float startX = 1285;
        float startY = 330;
        System.out.println("startX: " + startX);
        System.out.println("startY: " + startY);


        circleView = new View(MainActivity.this);

        // Create a circular view to animate
        circleView.setLayoutParams(new ViewGroup.LayoutParams(3000, 3000));
        circleView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.circle_shape));
        circleView.setX(0);
        circleView.setY(view.getHeight());
        ((ViewGroup) getWindow().getDecorView()).addView(circleView);

        // Define the animation
        float endRadius = 600;
        Animator animator = ViewAnimationUtils.createCircularReveal(circleView, (int) startX, (int) startY, 30, 4000);
        animator.setDuration(800);

        // Set an animation listener
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
                // Remove the circular view

                // Start the pop-up animation
                startPopUpAnimation();
            }
        });

        // Start the animation
        animator.start();

    }

    private void startPopUpAnimation() {
        Intent intent = new Intent(MainActivity.this, ShopActivity.class);
        System.out.println("Starting ShopActivity");
        // Start the pop-up animation
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(MainActivity.this, R.anim.blow_up, R.anim.blow_up);
        startActivity(intent, options.toBundle());

    }
}