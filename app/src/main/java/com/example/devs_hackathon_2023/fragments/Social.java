package com.example.devs_hackathon_2023.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.Database;
import com.example.devs_hackathon_2023.activities.LeaderboardActivity;
import com.example.devs_hackathon_2023.interfaces.FriendDeleteListener;
import com.example.devs_hackathon_2023.adaptors.FriendsAdapter;


public class Social extends Fragment implements FriendDeleteListener {

    View view;
    TextView totalFriends;
    public Social() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_social, container, false);
        generateFriendsList();

        ImageView leaderboard = view.findViewById(R.id.leaderboard);
        totalFriends = view.findViewById(R.id.totalFriends);
        totalFriends.setText("Total Friends: " + Database.getPlayers().size());
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LeaderboardActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void generateFriendsList() {
        FriendsAdapter friendsAdapter = new FriendsAdapter(this.getContext(), R.layout.friend_tab, Database.getPlayers());
        friendsAdapter.setFriendDeleteListener(this);
        ListView listView = view.findViewById(R.id.friendList);
        listView.setAdapter(friendsAdapter);
    }

    @Override
    public void onFriendDelete() {
        totalFriends.setText("Total Friends: " + Database.getPlayers().size());
    }
}