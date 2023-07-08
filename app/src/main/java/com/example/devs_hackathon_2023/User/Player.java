package com.example.devs_hackathon_2023.User;

import com.example.devs_hackathon_2023.Quest.Quest;

import java.util.List;

public class Player extends MainPlayer{
    private int steps;
    private int distance;
    private int money;

    private List<MainPlayer> friends;

    public Player(String name, String id, int score, Location location, List<Quest> quests, int steps, int distance, int money) {
        super(name, id, score, location, quests);
        this.steps = steps;
        this.distance = distance;
        this.money = money;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<MainPlayer> getFriends() {
        return friends;
    }

    public void addFriend(MainPlayer friend) {
        this.friends.add(friend);
    }
}
