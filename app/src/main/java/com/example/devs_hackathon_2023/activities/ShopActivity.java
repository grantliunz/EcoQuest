package com.example.devs_hackathon_2023.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import com.example.devs_hackathon_2023.MainActivity;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.MainPlayer;
import com.example.devs_hackathon_2023.adaptors.EmoteAdapter;
import com.example.devs_hackathon_2023.shop.emotes.Emote;
import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmoteAdapter emoteAdapter;
    private List<Emote> emoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        setupClickableButtons();

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerViewShop);

        // Set up the GridLayoutManager with span count
        int spanCount = 3; // Number of columns in horizontal layout
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        // Prepare the emote list
        emoteList = new ArrayList<>();
        // Add your emote items to the list
        emoteList.add(new Emote("LOL", "Express your amusement with a hearty laugh.", 2, R.drawable.laughing, "Emote"));
        emoteList.add(new Emote("Thumbs Up", "Show your approval with a thumbs-up gesture.", 4, R.drawable.thumbs_up, "Emote"));
        emoteList.add(new Emote("Super Snail", "Adopt a friendly cat as your pet companion.", 8, R.drawable.snail, "Pet"));
        emoteList.add(new Emote("Crying", "Express your sadness with tears.", 10, R.drawable.crying, "Emote"));
        emoteList.add(new Emote("Sleepy", "Show how tired you are with a yawn.", 12, R.drawable.sleepy, "Emote"));
        emoteList.add(new Emote("Angry", "Express your anger with a fierce look.", 15, R.drawable.angry, "Emote"));
        emoteList.add(new Emote("Cheerful", "Spread cheer and positivity with a big smile.", 18, R.drawable.cheerful, "Emote"));
        emoteList.add(new Emote("Musician", "Show your playful side with a mischievous wink.", 22, R.drawable.guitar, "Emote"));
        emoteList.add(new Emote("Sparkle", "Shine bright and spread sparkle all around.", 25, R.drawable.magic, "Emote"));
        emoteList.add(new Emote("Friendly Cat", "Adopt a friendly cat as your pet companion.", 30, R.drawable.cat, "Pet"));
        emoteList.add(new Emote("Loyal Dog", "Get a loyal dog to accompany you on your adventures.", 40, R.drawable.dog, "Pet"));

        // Add more emotes as needed

        // Set up the adapter

        emoteAdapter = new EmoteAdapter(emoteList, this);
        recyclerView.setAdapter(emoteAdapter);
    }

    private void setupClickableButtons(){
        ImageView backButton = findViewById(R.id.shopBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch back to the main screen
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
