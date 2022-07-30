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

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class DungeonGenerator {

    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    private String configName;
    private Dungeon dungeon;
    
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
        
        // Get the entities. 

        //Dungeon dungeon = new Dungeon("Dungeon-" + strDate, dungeonJson, configJson);
        return dungeon;
    }

    
    public static void generate(int xStart, int yStart, int xEnd, int yEnd,String name,Dungeon response) {
        Position start = new Position(xStart,yStart);
        Position end = new Position(xEnd,yEnd);
        //To do ()
    }
    public static List<Position> getNeigbours(Position position, int distance) {
        List<Position> neigbours = new ArrayList<Position>();
        neigbours.add(new Position(position.getX(),position.getY()+distance));
        neigbours.add(new Position(position.getX(),position.getY()-distance));
        neigbours.add(new Position(position.getX()+distance,position.getY()));
        neigbours.add(new Position(position.getX()-distance,position.getY()));

        List<Position> finalList = new ArrayList<Position>();
        for(Position positions : neigbours) {
            finalList.add(positions);
        }

        for(Position pos : neigbours) {
            if (pos.getX() <= 0 || pos.getX() >= 50) {
                finalList.remove(pos);
            }
            else if (pos.getY() <= 0 || pos.getX() >= 50) {
                finalList.remove(pos);
            }
        }
        return finalList;
    }

    public static Map<Position, Boolean> RandomizedPrims(Position start, Position end) {
        Map<Position, Boolean> maze = new HashMap<Position, Boolean>();
        maze.put(start, true);
        for (int i = 0;i < 50;i++) {
            for (int j = 0;j<50;j++) {
                maze.put(new Position(i,j),false);
            }
        }
        List<Position> options = new ArrayList<Position>();
        for (Position pos : getNeigbours(start,2)) {
            if (! maze.get(pos)) {
                options.add(pos);
            }  
        }

        while (options.size() > 0) {
            int num1 = ThreadLocalRandom.current().nextInt(0,options.size());
            // num  = 0,...size - 1;
            Position next = options.remove(num1);

            List<Position> neighbours = new ArrayList<Position>();

            // Adding neighbours of 'next' that are distance 2 away and are empty
            for (Position pos: getNeigbours(next, 2)) {
                if (maze.get(pos) == true) {
                    // Is empty i.e. true
                    neighbours.add(pos);
                }
            }

            if (neighbours.size() > 0) {
                int num2 = ThreadLocalRandom.current().nextInt(0,neighbours.size());
                // num  = 0,...size - 1;
                Position neighbour = neighbours.remove(num2);

                maze.put(next, true);
                // midPos is the position inbetween next and neigbhour
                Position midPos = new Position((next.getX() + neighbour.getX())/2, (next.getY() + neighbour.getY())/2);
                maze.put(midPos, true);
                maze.put(neighbour, true);
            }

            // Add all neighbours of 'next' that are distance 2 away and are walls
            for (Position pos: getNeigbours(next, 2)) {
                if ( maze.get(pos) == false) {
                    // Is a wall i.e. false
                    options.add(pos);
                }
            }
        }

        List<Position> neighbours = getNeigbours(end, 1);
        Boolean isConnected = false;

        for (Position pos: neighbours) {
            if (maze.get(pos) == true) { 
                // true i.e. empty
                isConnected = true;
            }
        }

        if (isConnected == false) {
            int num = ThreadLocalRandom.current().nextInt(0,neighbours.size());
            // num  = 0,...size - 1;
            Position neighbour = neighbours.remove(num);
            maze.put(neighbour, true);
        }

        return maze;
    }

}