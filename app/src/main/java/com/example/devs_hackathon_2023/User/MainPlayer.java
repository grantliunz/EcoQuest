package com.example.devs_hackathon_2023.User;

import com.example.devs_hackathon_2023.Quest.Quest;

import java.util.List;

public class MainPlayer {
    private String name;
    private String id;
    private int score;
    private Location location;
    private List<Quest> quests;

    public MainPlayer(String name, String id, int score, Location location, List<Quest> quests) {
        this.name = name;
        this.id = id;
        this.score = score;
        this.location = location;
        this.quests = quests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public void setQuests(List<Quest> quests) {
        this.quests = quests;
    }

    public void addQuest(Quest quest) {
        this.quests.add(quest);
    }
}
