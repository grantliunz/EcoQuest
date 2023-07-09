package com.example.devs_hackathon_2023.shop.emotes;

public class Emote {
    private String title;
    private String description;
    private int cost;
    private int imagePath;

    private String type;

    public Emote(String title, String description, int cost, int imagePath, String type) {
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.imagePath = imagePath;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public int getImagePath() {
        return imagePath;
    }

    public String getType() {
        return type;
    }
}
