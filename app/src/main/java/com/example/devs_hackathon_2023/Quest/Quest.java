package com.example.devs_hackathon_2023.Quest;

import com.example.devs_hackathon_2023.User.MainPlayer;

import java.util.List;

public class Quest {
    private String title;
    private String description;
    private String id;
    private int value;
    private int time;
    private boolean completed;
    private List<MainPlayer> scores;

    public Quest(String title, String description,String id, int value, int time, boolean completed, List<MainPlayer> scores) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.value = value;
        this.time = time;
        this.completed = completed;
        this.scores = scores;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int gettime() {
        return time;
    }

    public void settime(int time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<MainPlayer> getScores() {
        return scores;
    }

    public void addScore(MainPlayer player) {
        this.scores.add(player);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
