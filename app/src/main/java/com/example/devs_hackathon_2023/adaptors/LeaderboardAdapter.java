package com.example.devs_hackathon_2023.adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.Database;
import com.example.devs_hackathon_2023.User.Player;

import java.util.List;

public class LeaderboardAdapter extends ArrayAdapter<Player> {

    private Context context;
    private int resource;

    public LeaderboardAdapter(@NonNull Context context, int resource, @NonNull List<Player> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView ranking = convertView.findViewById(R.id.leaderboardPlacing);
        TextView name = convertView.findViewById(R.id.leaderboardName);
        TextView level = convertView.findViewById(R.id.leaderboardLevel);

        Player player = getItem(position);

        // Alternate background color
        if (position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.grey, null));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white, null));
        }

        ranking.setText(String.valueOf(position + 1));
        name.setText(player.getName());
        level.setText("Level " + player.getLevel());

        return convertView;
    }
}
