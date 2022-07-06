package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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


    public Dungeon(String dungeonName, JsonObject dungeonJson, JsonObject configJson) {
        this.dungeonJson = dungeonJson;
        this.configJson = configJson;
        this.dungeonId = "dungeon-0";
        this.dungeonName = dungeonName;

        // Add more here
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
