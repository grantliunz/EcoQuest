package com.example.devs_hackathon_2023.adaptors;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devs_hackathon_2023.Quest.Quest;
import com.example.devs_hackathon_2023.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

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
        holder.questImageView.setImageResource(quest.getImage());
        holder.questTitleTextView.setText(quest.getTitle());
        holder.questDescriptionTextView.setText(quest.getDescription());
        holder.questRewardTextView.setText("Reward: " + String.valueOf(quest.getValue()) + " xp");
        holder.questProgressText.setText("Progress: "+ quest.getNoCompleted() + "/" + quest.getNoTasks());
        holder.questProgressBar.setProgress(findPercentageAsInt(quest.getNoCompleted(), quest.getNoTasks()));
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

