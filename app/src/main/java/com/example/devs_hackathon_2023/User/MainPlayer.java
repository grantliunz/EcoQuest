package com.example.devs_hackathon_2023.User;

import android.media.Image;

import com.example.devs_hackathon_2023.Quest.Quest;
import com.example.devs_hackathon_2023.R;
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

    private static int profilePicture;

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


    public static String getName() {
        return name;
    }

    public static void setName(String Mname) {
        name = Mname;
    }

    public static String getId() {
        return id;
    }

    public static int getProfilePicture() {
        return profilePicture;
    }

    public static void setProfilePicture(int resourceID) {
        profilePicture = resourceID;
    }
    public static int getScore() {
        return score;
    }

    public static void setScore(int Mscore) {
        score = Mscore;
    }

    public static int getLevel(){return score / XP_PER_LEVEL; }

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location Mlocation) {
        location = Mlocation;
    }

    public static List<Quest> getQuests() {
        return quests;
    }

    public static void setQuests(List<Quest> Mquests) {
        quests = Mquests;
    }

    public static void addQuest(Quest quest) {
        quests.add(quest);
    }

    public static void setupQuest(){
        quests = new ArrayList<>();
        // Add your quest items to the list
        quests.add(new Quest("Albert Park", "Discover the hidden wonders of the park.", "abc123", 86, 168.8421, false, R.drawable.quest1, 1, new Location(-36.850109, 174.767700)));
        quests.add(new Quest("Auckland Botanic Gardens", "Discover the hidden wonders of the gardens.", "abc123", 15, 168.8421, false, R.drawable.quest2, 1));
        quests.add(new Quest("Straight A's", "Visit 3 places that start with the letter A", "abc123", 86, 168.8421, true, R.drawable.quest1, 3));
        quests.add(new Quest("Auckland Botanic Gardens", "Discover the hidden wonders of the gardens.", "abc123", 15, 168.8421, false, R.drawable.quest2));
        quests.add(new Quest("Auckland Domain", "Discover the hidden wonders of the park.", "abc123", 86, 168.8421, false, R.drawable.quest1));
        quests.add(new Quest("Auckland Botanic Test", "Discover the hidden wonders of the gardens.", "abc123", 15, 168.8421, true, R.drawable.quest2));
    }

    public static void updateQuests(Location curLocation){
        for (Quest quest : quests){
            if (!quest.isCompleted()){
                if (quest.testQuestCompletion(curLocation)){
                    // quest is completed
                    quest.completeOneTask();
                }
            }

        }
    }

    public static List<Player> getFriends() {
        return friends;
    }

    public static void addFriend(Player friend) {
        friends.add(friend);
    }

    public static void completeQuest(int questId){
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
