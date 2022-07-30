package dungeonmania;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class DungeonGenerator {

    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    private String configName;
    
    public DungeonGenerator(int xStart, int yStart, int xEnd, int yEnd, String configName) {
        this.xStart = xStart;
        this.yStart = yStart;

        this.xEnd = xEnd;
        this.yEnd = yEnd;

        this.configName = configName;
    }

    public Dungeon generateDungeon() throws IllegalArgumentException {
        String configsString = null;

        // Get the file
        try {
            configsString = FileLoader.loadResourceFile("configs/" + configName + ".json");
        }
        catch(Exception IOException) {
            throw new IllegalArgumentException("Configuration not found!");
        }

        // Turn the String into a JsonObject
        JsonObject configJson = JsonParser.parseString(configsString).getAsJsonObject();

        // Create the new dungeon
        // Make the dungeon name the Date it was created
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");  
        String strDate = dateFormat.format(date);

        String dungeonsString = "{\"entities\": [{\"x\": " + xStart + ",\"y\": " + yStart + ",\"type\": \"player\"}";
        
        // Get wall Positions. 
        Map<Position, Boolean> emptyPositions = PrimsAlgorithm(new Position(this.xStart, this.yStart), new Position(this.xEnd, this.yEnd)); 

        for (Position pos : emptyPositions.keySet()) {
            if (! emptyPositions.get(pos)) {
                dungeonsString = dungeonsString + ",{\"x\": " + pos.getX() + ",\"y\": " + pos.getY() + ",\"type\": \"wall\"}";
            }
        }

        dungeonsString = dungeonsString + ",{\"x\": " + xEnd + ",\"y\": " + yEnd + ",\"type\": \"exit\"}],\"goal-condition\": {\"goal\": \"exit\"}}";

        System.out.println(dungeonsString);
        
        JsonObject dungeonJson = JsonParser.parseString(dungeonsString).getAsJsonObject();

        Dungeon dungeon = new Dungeon("Dungeon-" + strDate, dungeonJson, configJson);
        return dungeon;
    }

    /*
     * Prims algorithm to find the position of walls. 
     */
    public Map<Position, Boolean> PrimsAlgorithm(Position start, Position end) {
        Map<Position, Boolean> maze = new HashMap<Position, Boolean>();

        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (i == xStart && j == yStart) {
                    continue;
                }
                maze.put(new Position(i, j), false);
            }
        }

        maze.put(start, true);

        List<Position> options = new ArrayList<Position>();
        for (Position pos : getNeigbours(start, 2)) {
            if (! maze.get(pos)) {
                options.add(pos);
            }  
        }

        while (! options.isEmpty()) {

            Random random = new Random();
            int randomIndex = random.nextInt(0, options.size());

            Position next = options.remove(randomIndex);

            List<Position> neighbours = new ArrayList<Position>();

            for (Position pos: getNeigbours(next, 2)) {
                if (maze.get(pos)) {
                    neighbours.add(pos);
                }
            }

            if (! neighbours.isEmpty()) {
                random = new Random();
                randomIndex = random.nextInt(0, neighbours.size());

                Position neighbour = neighbours.remove(randomIndex);

                maze.replace(next, true);

                Position midPos = new Position((next.getX() + neighbour.getX()) / 2, (next.getY() + neighbour.getY()) / 2);

                maze.replace(midPos, true);

                maze.replace(neighbour, true);
            }

            for (Position pos: getNeigbours(next, 2)) {
                if (maze.get(pos) == false) {
                    options.add(pos);
                }
            }
        }

        if (maze.get(end) == false) {
            maze.replace(end, true);
            List<Position> neighbours = getNeigbours(end, 1);

            boolean isConnected = false;
            for (Position pos: neighbours) {
                if (maze.get(pos) == true) { 
                    isConnected = true;
                }
            }

            if (isConnected == false) {
                Random random = new Random();
                int randomIndex = random.nextInt(0, neighbours.size());

                Position neighbour = neighbours.remove(randomIndex);
                maze.replace(neighbour, true);
            }
        }

        return maze;
    }

    /*
     * Get the cardinally adjacent neighbours of a position at a certain distance away. 
     */
    public List<Position> getNeigbours(Position position, int distance) {
        List<Position> neigbours = new ArrayList<Position>();
        neigbours.add(new Position(position.getX(), position.getY() + distance));
        neigbours.add(new Position(position.getX(), position.getY() - distance));
        neigbours.add(new Position(position.getX() + distance, position.getY()));
        neigbours.add(new Position(position.getX() - distance, position.getY()));

        List<Position> finalList = new ArrayList<Position>();
        for(Position positions : neigbours) {
            finalList.add(positions);
        }

        for(Position pos : neigbours) {
            if (pos.getX() < this.xStart || pos.getX() > this.xEnd) {
                finalList.remove(pos);
            }

            else if (pos.getY() < this.yStart || pos.getY() > this.yEnd) {
                finalList.remove(pos);
            }
        }

        return finalList;
    }
}