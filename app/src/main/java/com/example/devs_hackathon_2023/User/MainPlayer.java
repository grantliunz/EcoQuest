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

    private static Emote currentPet;

    private static int steps;

    private static int money;

    private static int profilePicture;

    private static List<Player> friends;

    private static int distanceWalked;

    private static int landmarksVisited;



    public static void createPlayer(String Mname, String Mid, Location Mlocation){
        name = Mname;
        id = Mid;
        score = 0;
        location = Mlocation;
        quests = new ArrayList<>();
        steps = 0;
        money = 0;
        friends = new ArrayList<>();
        distanceWalked = 0;
        landmarksVisited = 0;
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

    public static void calculateLevel(){

    }
    public static int getSteps() {
        return steps;
    }

    public static void setSteps(int Msteps) {
        steps = Msteps;
    }

    public static int getLevel(){
        return score / XP_PER_LEVEL;
    }


    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location Mlocation) {
        location = Mlocation;
    }

    public static void setDistanceWalked(int distanceWalked) {
        MainPlayer.distanceWalked = distanceWalked;
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
        quests.add(new Quest("Touch Grass", "Visit 2 green areas on the map", "abc123", 86, 168.8421, true, R.drawable.quest1, 2));
        quests.add(new Quest("Auckland Botanic Gardens", "Discover the hidden wonders of the gardens.", "abc123", 15, 168.8421, false, R.drawable.quest2, 1, new Location(-37.0079937, 174.905168380951)));
        quests.add(new Quest("Straight A's", "Visit 3 places that start with the letter A", "abc123", 86, 168.8421, true, R.drawable.quest1, 3));
        quests.add(new Quest("Sky High", "Visit the Sky Tower", "abc123", 15, 168.8421, false, R.drawable.quest2, 1, new Location(-36.848450, 174.762192)));
        quests.add(new Quest("Historical Journey", "Connect with history at the Auckland Museum", "abc123", 50, 168.8421, false, R.drawable.quest2, 1, new Location(-36.860655, 174.777744)));
        quests.add(new Quest("Get an Education", "Visit the Owen G Glenn Building", "abc123", 50, 168.8421, false, R.drawable.quest2, 1, new Location(-36.85312875, 174.771288840132)));
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

    public static void setupScore(){
        // loop through and tally completed quests
        score = 0;
        for (Quest quest : quests) {
            if (quest.isCompleted()) {
                score += quest.getValue();
            }
        }
    }
    public static Emote getCurrentEmote(){
        return currentEmote;
    }
    public static void setEmote(Emote emote){
        currentEmote = emote;
    }

    public static void setPet(Emote pet) {currentPet = pet;}
    public static Emote getCurrentPet() {return currentPet;}

    public static int getXp(){
        return score % XP_PER_LEVEL;
    }

    public static int getDistanceWalked() {
        return distanceWalked;
    }

    public static int getLandmarksVisited() {
        return landmarksVisited;
    }

    public static int getCompleted(){
        int completed = 0;
        for (Quest quest : quests) {
            if (quest.isCompleted()) {
                completed ++;
            }
        }
        return completed;
    }

}
