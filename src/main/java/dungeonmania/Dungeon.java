package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.ExitGoal;
import dungeonmania.goals.Goal;
import dungeonmania.util.Direction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

public class Dungeon {

    // All dungeon info and configs are in these two variables. Stored as JsonObject.
    private JsonObject dungeonJson;
    private JsonObject configJson;
    // Example Use: dungeonJson.get("entities").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString()

    // Add any variables here when you need them.
    private String dungeonId;
    private String dungeonName;

    // Add data structures here when you need them.
    private Map<String, Goal> goals = new HashMap<String, Goal>();

    public Dungeon(String dungeonName, JsonObject dungeonJson, JsonObject configJson) {
        this.dungeonJson = dungeonJson;
        this.configJson = configJson;
        this.dungeonId = "dungeon-0";
        this.dungeonName = dungeonName;
        setGoal();
        // Add more here
    }

    private void setGoal() {
        String goal = dungeonJson.get("goal-condition").getAsJsonObject().get("goal").getAsString();
        if (goal == "AND" || goal == "OR") {
            return;
        } else if (goal.equals("exit")) {
            goals.put("goal", new ExitGoal());
        } else if (goal.equals("treasure")) {
            goals.put("goal", new ExitGoal());
        } else if (goal.equals("boulders")) {
            goals.put("goal", new ExitGoal());
        } else if (goal.equals("enemies")) {
            goals.put("goal", new ExitGoal());
        }
    }

    public String getGoal() {
        String mapAsString = (goals.keySet().stream()
        .map(key -> key + ":" + goals.get(key).getString()))
        .collect(Collectors.joining(", ", "{", "}"));
        //System.out.println(mapAsString);
        return mapAsString;
    }

    // Getters (Add more here)
    public String getDungeonId() {
        return dungeonId;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    /**
     * /game/tick/item
     */
    public void tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {

    }

    /**
     * /game/tick/movement
     */
    public void tick(Direction movementDirection) {

    }

    /**
     * /game/build
     */
    public void build(String buildable) throws IllegalArgumentException, InvalidActionException {

    }

    /**
     * /game/interact
     */
    public void interact(String entityId) throws IllegalArgumentException, InvalidActionException {

    }
}
