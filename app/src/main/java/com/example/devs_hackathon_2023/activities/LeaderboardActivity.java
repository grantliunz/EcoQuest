package com.example.devs_hackathon_2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.Database;
import com.example.devs_hackathon_2023.adaptors.LeaderboardAdapter;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        generateListView();
    }

    private void generateListView() {
        LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(this, R.layout.leaderboard_tab, Database.getTopPlayers());
        ListView listView = findViewById(R.id.leaderboardList);
        listView.setAdapter(leaderboardAdapter);

    }
}