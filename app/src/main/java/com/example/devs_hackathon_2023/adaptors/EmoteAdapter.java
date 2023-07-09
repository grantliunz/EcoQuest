package com.example.devs_hackathon_2023.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.MainPlayer;
import com.example.devs_hackathon_2023.shop.emotes.Emote;

import java.util.List;

public class EmoteAdapter extends RecyclerView.Adapter<EmoteAdapter.EmoteViewHolder> {
    private List<Emote> emoteList;
    private Context context;

    public EmoteAdapter(List<Emote> emoteList, Context context) {
        this.emoteList = emoteList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emote, parent, false);
        return new EmoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmoteViewHolder holder, int position) {
        Emote emote = emoteList.get(position);
        holder.emoteTitleTextView.setText(emote.getTitle());
//        holder.emoteDescriptionTextView.setText(emote.getDescription());
        holder.emoteCostTextView.setText(String.valueOf(emote.getType()));


        holder.emoteImageView.setImageResource(emote.getImagePath());
        if (emote.equals(MainPlayer.getCurrentEmote())){
            holder.emoteEquipButton.setText("Equipped");
            holder.emoteEquipButton.setEnabled(false);
        } else if (emote.getCost() > MainPlayer.getLevel()){
            holder.emoteEquipButton.setText("Level " + emote.getCost());
            holder.emoteEquipButton.setEnabled(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(emote.getType()){
                    case "Pet":
                        MainPlayer.setPet(emote);
                        Log.d("TAG", emote.getTitle());
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return emoteList.size();
    }

    public class EmoteViewHolder extends RecyclerView.ViewHolder {
        ImageView emoteImageView;
        TextView emoteTitleTextView;
//        TextView emoteDescriptionTextView;
        TextView emoteCostTextView;

        Button emoteEquipButton;

        public EmoteViewHolder(@NonNull View itemView) {
            super(itemView);
            emoteImageView = itemView.findViewById(R.id.emoteImageView);
            emoteTitleTextView = itemView.findViewById(R.id.emoteTitleTextView);
//            emoteDescriptionTextView = itemView.findViewById(R.id.emoteDescriptionTextView);
            emoteCostTextView = itemView.findViewById(R.id.emoteCostTextView);
            emoteEquipButton = itemView.findViewById(R.id.emoteBuyButton);

        }
    }
}
