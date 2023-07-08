package com.example.devs_hackathon_2023.Quest;

import com.example.devs_hackathon_2023.User.MainPlayer;

import java.util.List;

public class Quest {
    private String title;
    private String description;
    private String id;
    private int value;
    private double time;
    private int noCompleted;
    private boolean completed;
    private int noTasks;
    private List<MainPlayer> scores;

    private int imagePath;

    public Quest(String title, String description,String id, int value, double time, boolean completed, int imagePath) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.value = value;
        this.time = time;
        this.noCompleted = 0;
        this.noTasks = 1;
        this.imagePath = imagePath;
        this.completed = completed;
    }
    public Quest(String title, String description,String id, int value, double time, boolean completed, int imagePath, int noTasks) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.value = value;
        this.time = time;
        this.noCompleted = 0;
        this.noTasks = noTasks;
        this.imagePath = imagePath;
        this.completed = completed;
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
    public void completeOneTask(){
        noCompleted++;

        if (noCompleted >= noTasks){
            setCompleted(true);
        }
    }
    public float getProgress(){
        return (float)noCompleted / (float)noTasks;
    }
    public int getNoCompleted(){
        return noCompleted;
    }

    public int getNoTasks(){
        return noTasks;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double gettime() {
        return time;
    }

    public void settime(double time) {
        this.time = time;
    }

    public boolean isCompleted() {
        if(completed){
            this.noCompleted = this.noTasks;
        }
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

    public int getImage() {
        return imagePath;
    }
}
