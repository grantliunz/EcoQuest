package com.example.devs_hackathon_2023.User;

import com.example.devs_hackathon_2023.Quest.Quest;
import com.example.devs_hackathon_2023.R;

import org.json.JSONArray;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Database {

    private static ArrayList<Player> players;
    private static List<Integer> imageList = new ArrayList<Integer>(Arrays.asList(R.drawable.bill_icon, R.drawable.connor_icon, R.drawable.dhruv_icon, R.drawable.elon_icon, R.drawable.grant_icon, R.drawable.jordan_icon, R.drawable.leander_icon, R.drawable.zucc_icon, R.drawable.chatgpt, R.drawable.le, R.drawable.raymond));


    //Setup the database by reading JSON file with Players
    public static void setupDatabase(InputStream inputStream) {
        players = new ArrayList<Player>();
        try {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                String name = jsonArray.getJSONObject(i).getString("name");
                String id = jsonArray.getJSONObject(i).getString("id");
                int steps = jsonArray.getJSONObject(i).getInt("steps");
                Location location = new Location(jsonArray.getJSONObject(i).getJSONObject("location").getDouble("latitude"), jsonArray.getJSONObject(i).getJSONObject("location").getDouble("longitude"));
//                    JSONArray jsonQuests = new JSONArray(jsonArray.getJSONObject(i).getJSONArray("quests"));
                JSONArray jsonQuests = jsonArray.getJSONObject(i).getJSONArray("quests");
                ArrayList<Quest> quests = new ArrayList<Quest>();
                for (int j = 0; j < jsonQuests.length(); j++) {
                    String questName = jsonQuests.getJSONObject(j).getString("name");
                    String questId = jsonQuests.getJSONObject(j).getString("id");
                    String questDescription = jsonQuests.getJSONObject(j).getString("description");
                    double questTime = jsonQuests.getJSONObject(j).getDouble("time");
                    Boolean questCompleted = jsonQuests.getJSONObject(j).getBoolean("completed");
                    int questPoints = jsonQuests.getJSONObject(j).getInt("points");
                    quests.add(new Quest(questName, questDescription, questId, questPoints, questTime, questCompleted, 123));
                }
                players.add(new Player(name, id, location, quests, steps, imageList.get(i % 10)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static void removePlayer(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId().equals(player.getId())) {
                players.remove(i);
                return;
            }
        }
    }

    public static List<Player> getTopPlayers() {
        List<Player> topPlayers = players;
        topPlayers.sort((p1, p2) -> p2.getScore() - p1.getScore());
        return topPlayers.subList(0, 10);
    }
}
