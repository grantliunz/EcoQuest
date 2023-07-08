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
    private int money;
    private List<MainPlayer> friends;

    public Player(String name, String id, int score, Location location, List<Quest> quests, int steps, int money, List<MainPlayer> friends) {
        this.name = name;
        this.id = id;
        this.score = score;
        this.location = location;
        this.quests = quests;
        this.steps = steps;
        this.money = money;
        this.friends = friends;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
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
