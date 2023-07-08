package com.example.devs_hackathon_2023.Quest;

import com.example.devs_hackathon_2023.User.MainPlayer;

import java.util.List;

public class Quest {
    private String title;
    private String description;
    private int value;
    private int timeLimit;
    private boolean completed;
    private List<MainPlayer> scores;

    public Quest(String title, String description, int value, int timeLimit, boolean completed, List<MainPlayer> scores) {
        this.title = title;
        this.description = description;
        this.value = value;
        this.timeLimit = timeLimit;
        this.completed = completed;
        this.scores = scores;
    }

}
