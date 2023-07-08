package com.example.devs_hackathon_2023.User;

import com.example.devs_hackathon_2023.Quest.Quest;
import com.example.devs_hackathon_2023.shop.emotes.Emote;

import java.util.ArrayList;
import java.util.List;



public abstract class MainPlayer {
    private static final int XP_PER_LEVEL = 100;
    private static String name;
    private static String id;
    private static int score;
    private static Location location;
    private static List<Quest> quests;

    private static Emote currentEmote;

    private static int steps;

    private static int money;

    private static List<Player> friends;


    public static void createPlayer(String Mname, String Mid, Location Mlocation){
        name = Mname;
        id = Mid;
        score = 0;
        location = Mlocation;
        quests = new ArrayList<>();
        steps = 0;
        money = 0;
        friends = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String Mname) {
        name = Mname;
    }

    public String getId() {
        return id;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int Mscore) {
        score = Mscore;
    }

    public static int getLevel(){return score / XP_PER_LEVEL; }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location Mlocation) {
        location = Mlocation;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public void setQuests(List<Quest> Mquests) {
        quests = Mquests;
    }

    public void addQuest(Quest quest) {
        quests.add(quest);
    }

    public static List<Player> getFriends() {
        return friends;
    }

    public void addFriend(Player friend) {
        friends.add(friend);
    }

    public void completeQuest(int questId){
        Quest quest = quests.stream().filter(q -> q.getId().equals(questId)).findFirst().get();
        quest.setCompleted(true);
        score += quest.getValue();
    }
    public static Emote getCurrentEmote(){
        return currentEmote;
    }
    public static void setEmote(Emote emote){
        currentEmote = emote;
    }
}
