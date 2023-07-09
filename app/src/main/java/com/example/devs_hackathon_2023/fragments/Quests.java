package com.example.devs_hackathon_2023.fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devs_hackathon_2023.Quest.Quest;
import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.MainPlayer;
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

        questList = MainPlayer.getQuests();
        // Set up the adapter
        questAdapter = new QuestAdapter(questList, getContext());
        recyclerView.setAdapter(questAdapter);
        Typeface customTypeface = Typeface.createFromAsset(getActivity().getAssets(), "test.ttf");
        TextView questTitle = view.findViewById(R.id.Quests);
//        questTitle.setTypeface(customTypeface);

        return view;
    }


}