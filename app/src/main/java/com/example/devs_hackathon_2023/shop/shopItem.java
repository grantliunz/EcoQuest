package com.example.devs_hackathon_2023.shop;

public class shopItem {
    private String name;
    private int price;
    private String description;
    private String image;

    public shopItem(String name, int price, String description, String image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() { return description; }

    public String getImage() { return image; }
}
