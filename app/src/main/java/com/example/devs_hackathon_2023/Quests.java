package com.example.devs_hackathon_2023;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devs_hackathon_2023.Quest.Quest;
import com.example.devs_hackathon_2023.adaptors.QuestAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Quests #newInstance} factory method to
 * create an instance of this fragment.
 */
public class Quests extends Fragment {
    private RecyclerView recyclerView;
    private QuestAdapter questAdapter;
    private List<Quest> questList;

    public Quests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quests, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewQuest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Prepare the quest list
        questList = new ArrayList<>();
        // Add your quest items to the list
        questList.add(new Quest("Albert Park", "Discover the hidden wonders of the park.", "abc123", 86, 168.8421, false, R.drawable.quest1, 1));
        questList.add(new Quest("Auckland Botanic Gardens", "Discover the hidden wonders of the gardens.", "abc123", 15, 168.8421, false, R.drawable.quest2, 1));
        questList.add(new Quest("Straight A's", "Visit 3 places that start with the letter A", "abc123", 86, 168.8421, false, R.drawable.quest1, 3));
        questList.add(new Quest("Auckland Botanic Gardens", "Discover the hidden wonders of the gardens.", "abc123", 15, 168.8421, false, R.drawable.quest2));
        questList.add(new Quest("Albert Park", "Discover the hidden wonders of the park.", "abc123", 86, 168.8421, false, R.drawable.quest1));
        questList.add(new Quest("Auckland Botanic Gardens", "Discover the hidden wonders of the gardens.", "abc123", 15, 168.8421, false, R.drawable.quest2));
        // Add more quests as needed

        // Set up the adapter
        questAdapter = new QuestAdapter(questList, getContext());
        recyclerView.setAdapter(questAdapter);

        return view;
    }


}