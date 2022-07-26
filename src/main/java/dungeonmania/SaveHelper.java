package dungeonmania;

import com.google.gson.reflect.TypeToken;

import dungeonmania.response.models.DungeonResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class SaveHelper {
    public static void SaveGame(String name,DungeonResponse response) {
        Gson theFile = new Gson();
        try {
            String file = readFile("data.json");
            List<Map<String, DungeonResponse>> dungeonList = theFile.fromJson(file,
                    new TypeToken<List<Map<String, DungeonResponse>>>() {
                    }.getType());
            if (dungeonList == null) {
                dungeonList = new ArrayList<Map<String, DungeonResponse>>();
            }
            Map<String, DungeonResponse> map = new HashMap<>();
            map.put(name, response);
            dungeonList.add(map);

            FileWriter writer = new FileWriter("data.json");
            theFile.toJson(dungeonList, writer);
            writer.close();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    private static String readFile(String fileName) {
        String allLines = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            allLines = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            return allLines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allLines;
    }

    public static DungeonResponse LoadGame(String name) {
        Gson gson = new Gson();
        DungeonResponse dungeon = null;

        String data = readFile("data.json");

        List<Map<String, DungeonResponse>> dungeonList = gson.fromJson(data,
                new TypeToken<List<Map<String, DungeonResponse>>>() {
                }.getType());

        for (Map<String, DungeonResponse> eachSave : dungeonList) {
            for (Map.Entry<String, DungeonResponse> entry : eachSave.entrySet()) {
                if (entry.getKey().equals(name)) {
                    dungeon = entry.getValue();
                    break;
                }
            }
        }
        if (dungeon == null) {
            throw new IllegalArgumentException("Name is not a valid name");
        }
        return dungeon;
    }

    public static List<String> showALlGame() {
        List<String> allGames = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String data = readFile("data.json");

        List<Map<String, DungeonResponse>> dungeonList = gson.fromJson(data,
                new TypeToken<List<Map<String, DungeonResponse>>>() {
                }.getType());

        for (Map<String, DungeonResponse> eachSave : dungeonList) {
            for (String key : eachSave.keySet()) {
                allGames.add(key);
            }
        }
        return allGames;
    }
}
