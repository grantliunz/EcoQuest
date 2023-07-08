package com.example.devs_hackathon_2023.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.devs_hackathon_2023.MainActivity;
import com.example.devs_hackathon_2023.R;
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
        emoteList.add(new Emote("Happy Dance", "Express your joy with a lively dance.", 50, R.drawable.happy_dance));
        emoteList.add(new Emote("Cool Wave", "Send a cool wave to greet your friends.", 30, R.drawable.cool_wave));
        emoteList.add(new Emote("To Good", "Send a cool wave to greet your friends.", 30, R.drawable.cool_wave));
        emoteList.add(new Emote("Built Diff", "Send a cool wave to greet your friends.", 30, R.drawable.cool_wave));
        emoteList.add(new Emote("Lets go", "Send a cool wave to greet your friends.", 30, R.drawable.cool_wave));

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
