package com.example.devs_hackathon_2023.User;

import android.graphics.Bitmap;

import com.example.devs_hackathon_2023.Quest.Quest;
import com.google.android.gms.maps.model.BitmapDescriptor;

import java.util.List;

public class Player{
    private String name;
    private String id;
    private int score;
    private int level;
    private Location location;
    private List<Quest> quests;
    private int steps;

    private Bitmap profilePicture;

    public Player(String name, String id, int score, Bitmap profilePicture) {
        this.name = name;
        this.id = id;
        this.score = score;
        this.profilePicture = profilePicture;
    }

    public Player(String name, String id, Location location, List<Quest> quests, int steps) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.quests = quests;
        this.steps = steps;
        score = calcScore();
        level = calcLevel();
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

    private int calcScore(){
        int score = 0;
        for (Quest quest: quests){
            if (quest.isCompleted()){
                score += quest.getValue();
            }
        }
        return score;
    }

    private int calcLevel(){
        return score / 50;
    }

    public int getLevel(){
        return level;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

}
