package dungeonmania;

import java.util.List;

import com.google.gson.JsonObject;

import dungeonmania.StaticEntity.StaticEntity;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;


public class Dungeon {

    // All dungeon info and configs are in these two variables. Stored as JsonObject.
    private JsonObject dungeonJson;
    private JsonObject configJson;
    // Example Use: dungeonJson.get("entities").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString()

    // Add any variables here when you need them.
    private String dungeonId;
    private String dungeonName;

    // Add data structures here when you need them.
    private List<StaticEntity> entities;


    public Dungeon(String dungeonName, JsonObject dungeonJson, JsonObject configJson, List<StaticEntity> entities) {
        this.dungeonJson = dungeonJson;
        this.configJson = configJson;
        this.dungeonId = "dungeon-0";
        this.dungeonName = dungeonName;
        this.entities = entities;

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
     * @param item
     * this function can add Static entities to the list
     */
    public void AddStaticEntity(StaticEntity item) {
        entities.add(item);
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
