package com.example.devs_hackathon_2023.adaptors;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devs_hackathon_2023.MainActivity;
import com.example.devs_hackathon_2023.Quest.Quest;
import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.MainPlayer;
import com.example.devs_hackathon_2023.activities.ShopActivity;
import com.example.devs_hackathon_2023.fragments.Map;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.gms.maps.model.LatLng;

import androidx.fragment.app.FragmentActivity;

import java.util.List;
public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.QuestViewHolder> {
    private List<Quest> questList;
    private Context context;

    public QuestAdapter(List<Quest> questList, Context context) {
        this.questList = questList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quest, parent, false);
        return new QuestViewHolder(view);
    }

    private int findPercentageAsInt(int a, int b){
        return (int)((float)a * 100) / b;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestViewHolder holder, int position) {

        Quest quest = questList.get(position);
        //quest.completeOneTask();  // set all tasks to 1 completion for display purposes
        //holder.questImageView.setImageResource(quest.getImage());
        if (quest.isCompleted()){
            holder.questImageView.setImageResource(R.drawable.quest2);
        } else {
            holder.questImageView.setImageResource(R.drawable.quest1);
        }
        holder.questTitleTextView.setText(quest.getTitle());
        holder.questDescriptionTextView.setText(quest.getDescription());
        holder.questRewardTextView.setText("Reward: " + String.valueOf(quest.getValue()) + " xp");
        holder.questProgressText.setText("Progress: "+ quest.getNoCompleted() + "/" + quest.getNoTasks());
        holder.questProgressBar.setProgress(findPercentageAsInt(quest.getNoCompleted(), quest.getNoTasks()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("clicked quest" + quest.getDescription());
                System.out.println(MainActivity.map.getCurrentLocation() != null);
                System.out.println(quest.locationExists());
                if (quest.locationExists() && MainActivity.map.getCurrentLocation() != null){
                    System.out.println("setting new destination");
                    // set target location
                    LatLng destinationLatLng = new LatLng(quest.getQuestLoc().getLatitude(), quest.getQuestLoc().getLongitude());
                    System.out.println("123123123");

                    MainActivity.map.setTargetLocation(destinationLatLng.latitude, destinationLatLng.longitude);
                    Toast.makeText(v.getContext(), "Destination set", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questList.size();
    }

    public class QuestViewHolder extends RecyclerView.ViewHolder {
        TextView questTitleTextView;
        TextView questDescriptionTextView;
        TextView questRewardTextView;
        TextView questProgressText;

        ImageView questImageView;

        LinearProgressIndicator questProgressBar;

        public QuestViewHolder(@NonNull View itemView) {
            super(itemView);
            questTitleTextView = itemView.findViewById(R.id.questTitleTextView);
            questImageView = itemView.findViewById(R.id.questImageView);
            questDescriptionTextView = itemView.findViewById(R.id.questDescriptionTextView);
            questRewardTextView = itemView.findViewById(R.id.questRewardTextView);
            questProgressText = itemView.findViewById(R.id.questProgressText);
            questProgressBar = itemView.findViewById(R.id.questProgressBar);
        }
    }
}

