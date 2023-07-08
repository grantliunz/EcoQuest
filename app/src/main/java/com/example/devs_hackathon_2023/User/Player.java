package com.example.devs_hackathon_2023.User;

import com.example.devs_hackathon_2023.Quest.Quest;

import java.util.List;

public class Player{
    private String name;
    private String id;
    private int score;
    private Location location;
    private List<Quest> quests;
    private int steps;

    public Player(String name, String id, Location location, List<Quest> quests, int steps) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.quests = quests;
        this.steps = steps;
        calcScore(quests);
    }

    public int getSteps() {
        return steps;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public Location getLocation() {
        return location;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    private void calcScore(List<Quest> quests){
        int score = 0;
        for (Quest quest: quests){
            if (quest.isCompleted()){
                score += quest.getValue();
            }
        }
        this.score = score;
    }

}
