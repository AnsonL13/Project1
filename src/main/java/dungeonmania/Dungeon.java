package dungeonmania;

import dungeonmania.enemy.Enemy;
import dungeonmania.enemy.Mercenary;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Dungeon {

    // All dungeon info and configs are in these two variables. Stored as JsonObject.
    private JsonObject dungeonJson;
    private JsonObject configJson;
    // Example Use: dungeonJson.get("entities").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString()

    // Add any variables here when you need them.
    private String dungeonId;
    private String dungeonName;
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private List<ZombieToastSpawner> spawners;
    private EnemyFactory factory;
    private int latestUnusedId;
    List<Entity> entities = new ArrayList<Entity>();


    public Dungeon(String dungeonName, JsonObject dungeonJson, JsonObject configJson) {
        this.dungeonJson = dungeonJson;
        this.configJson = configJson;
        this.dungeonId = "dungeon-0";
        this.dungeonName = dungeonName;
        createMercenary();
        // Add more here
    }

    private void createZombieSpawner(Position pos) {
        int health = configJson.get("zombie_attack").getAsInt();
        int attack = configJson.get("zombie_health").getAsInt();
        int spawnRate = configJson.get("zombie_spawn_rate").getAsInt();
        spawners.add(new ZombieToastSpawner(spawnRate, pos));
        // add to static entities

    }

    private void createMercenary() {
        JsonArray dungeonEntities = dungeonJson.get("entities").getAsJsonArray();
        for (JsonElement entities : dungeonEntities) {
            String type = entities.getAsJsonObject().get("type").getAsString();
            if (type.equals("mercenary")) {
                int x = entities.getAsJsonObject().get("x").getAsInt();
                int y = entities.getAsJsonObject().get("y").getAsInt();
                int health = configJson.get("mercenary_attack").getAsInt();
                int attack = configJson.get("mercenary_health").getAsInt();
                Position pos = new Position(x, y);
                enemies.add(new Mercenary(0, health, attack, pos));
            }
        }
    }

    public List<EntityResponse> getEntities() {
        List<EntityResponse> entities = new ArrayList<EntityResponse>();
        for (Enemy enemy : enemies) {
            entities.add(new EntityResponse("0", enemy.getSimpleName(), enemy.getPosition(), false));
        }
        return entities;
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
       // List<ZombieToastSpawner> allSpawners = entities.stream().filter(final ZombieToastSpawner o -> o.getType().equalsIgnoreCase("ZombieToastSpawner")).collect(Collectors.toList());
        List<Entity> newEnemy = factory.spawn(latestUnusedId, getSpawner(), entities);
        entities.addAll(newEnemy);
        latestUnusedId+= newEnemy.size();
    }

    private List<ZombieToastSpawner> getSpawner() {
        List<ZombieToastSpawner> allSpawners = new ArrayList<ZombieToastSpawner>();

        for (Entity entity : entities) {
            if (entity.getType().equalsIgnoreCase("ZombieToastSpawner")) {
                allSpawners.add((ZombieToastSpawner)entity);
            }
        }
        return allSpawners;
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

    //public void 
}
