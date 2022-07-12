package dungeonmania;

import dungeonmania.BuildableEntities.Bow;
import dungeonmania.BuildableEntities.Shield;
import dungeonmania.CollectableEntities.Arrow;
import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.CollectableEntities.InvincibilityPotion;
import dungeonmania.CollectableEntities.InvisibilityPotion;
import dungeonmania.CollectableEntities.Key;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.CollectableEntities.Treasure;
import dungeonmania.CollectableEntities.Wood;
import dungeonmania.Goals.BouldersGoal;
import dungeonmania.Goals.ComplexGoal;
import dungeonmania.Goals.EnemiesGoal;
import dungeonmania.Goals.ExitGoal;
import dungeonmania.Goals.Goal;
import dungeonmania.Goals.TreasureGoal;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class Dungeon {

    // All dungeon info and configs are in these two variables. Stored as JsonObject.
    private JsonObject dungeonJson;
    private JsonObject configJson;
    // Example Use: dungeonJson.get("entities").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString()
    // Configs stored as a hashmap
    private HashMap<String, Integer> configMap;

    // Add any variables here when you need them.
    private String dungeonId;
    private String dungeonName;
    private Player player;
    private EnemyFactory spawner;
    private Goal goals;
    int latestUnusedId = 0;

    // Add data structures here when you need them.
    List<Entity> entities = new ArrayList<Entity>();
    List<InteractableEntity> interactablEntities = new ArrayList<InteractableEntity>();

    public Dungeon(String dungeonName, JsonObject dungeonJson, JsonObject configJson) {
        this.dungeonJson = dungeonJson;
        this.configJson = configJson;
        this.dungeonId = "dungeon-0";
        this.dungeonName = dungeonName;
        // Convert JsonObject configJson into HashMap<String, Integer> configMap
        this.configMap = configHelper(configJson);
        // Initialise all entities
        generateEntities(dungeonJson);
        // Create the enemy spawner
        generateSpawner();
        // Create the goals composite pattern "tree"
        setGoals(dungeonJson);
    }

    // Getters (Add more here)
    public String getDungeonId() {
        return dungeonId;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public List<Item> getInventory() {
        return player.getInventory();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Goal getGoals() {
        return goals;
    }

    public List<String> getBuildables() {
        int woodCount = 0;
        int arrowCount = 0;
        int treasureCount = 0;
        int keyCount = 0;

        // Get all resource items in the players inventory
        for (Item item : player.getInventory()) {
            switch (item.getType()) {
                case "wood":
                    woodCount++;
                    break;

                case "arrow":
                    arrowCount++;
                    break;

                case "treasure":
                    treasureCount++;
                    break;

                case "key":
                    keyCount++;
                    break;

                default:
                    break;
            }
        }

        List<String> buildables = new ArrayList<>();
        // Calculate if bow can be created
        if (woodCount >= 1 && arrowCount >= 3) {
            buildables.add("bow");
        }

        // Calculate if shield can be created
        if (woodCount >= 2 && (treasureCount >= 1 || keyCount >= 1)) {
            buildables.add("shield");
        }

        return buildables;
    }

    /**
     * /game/tick/item
     */
    public void tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        // Check if item is not in the inventory
        boolean foundItem = false;
        for (Item item : player.getInventory()) {
            if (item.getId().equals(itemUsedId)) {
                if (! (item.getType().equals("bomb") || 
                    item.getType().equals("invincibility_potion") || 
                    item.getType().equals("invisibility_potion"))) {
                    throw new IllegalArgumentException("itemUsedId");
                }
                foundItem = true;
            }
        }

        if (! foundItem) {
            throw new InvalidActionException("itemUsedId");
        }

        // Player uses the item
        player.useItem(itemUsedId);
        // Enemy movement
        
        // Battles
        player.battle();
        // Spawn

        // Update enemies
    }

    /**
     * /game/tick/movement
     */
    public void tick(Direction movementDirection) {
        // Check if movement into a static entity
        
        // Move

        // Check if moved into a collectable entity

        // Check if moved into an enemy

        // Move enemies
        
        // Check if Enemy has moved into a player

        // Spawn enemies
    }

    /**
     * /game/build
     */
    public void build(String buildable) throws IllegalArgumentException, InvalidActionException {
        // Check if buildable is not a bow or shield
        if (! (buildable.equals("shield") || buildable.equals("bow"))) {
            throw new IllegalArgumentException(buildable);
        }

        // Check if possible to build, throw any excpetions
        List<String> buildables = getBuildables();
        boolean canBuild = false;
        for (String item : buildables) {
            if (item.equals(buildable)) {
                canBuild = true;
            }
        }

        if (! canBuild) {
            throw new InvalidActionException(buildable);
        }

        // Build the item
        // Add the item to the players inventory and remove items from players inventory
        if (buildable.equals("shield")) {
            Shield shield = new Shield(Integer.toString(latestUnusedId), "shield", false, configMap.get("shield_defence"), configMap.get("shield_durability"));
            player.addToInventory(shield);
            player.addToWeapons(shield);
            player.removeForShield(); 
        }

        else if (buildable.equals("bow")) {
            Bow bow = new Bow(Integer.toString(latestUnusedId), "bow", false, configMap.get("bow_durability"));
            player.addToInventory(bow);
            player.addToWeapons(bow);
            player.removeForBow();
        }
    }

    /**
     * /game/interact
     */
    public void interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        // Check for IllegalArgumentException
        boolean foundEntity = false;
        for (Entity entity : interactablEntities) {
            if (entity.getId().equals(entityId)) {
                foundEntity = true;
            }
        }

        if (! foundEntity) {
            throw new IllegalArgumentException(entityId);
        }

        // Check for InvalidActionException
        for (InteractableEntity entity : interactablEntities) {
            if (entity.getId().equals(entityId)) {
                // Check if invalidAction
                if (! entity.interactActionCheck(player)) {
                    throw new InvalidActionException(entityId);
                }

                else if (entity.getType().equals("mercenary")) {
                    // Bribe the mercenary                    
                    // Not done yet ...
                }
                
                else if (entity.getType().equals("zombie_toast_spawner")) {
                    // Destroy the zombie spawner
                    this.entities.remove(entity);
                    this.interactablEntities.remove(entity);

                    // Decrease sword durability
                    player.decreaseSwordDurability();
                }
            }
        }
    }

    // Convert config.json to Hashmap<String, Integer>
    public HashMap<String, Integer> configHelper(JsonObject configJson) {
        java.lang.reflect.Type mapType = new TypeToken<HashMap<String, Integer>>() {}.getType();
        return new Gson().fromJson(configJson, mapType);
    }

    // Generate entities from Dungeon.json
    public void generateEntities(JsonObject dungeonJson) {
        // Read from dungeon json file. Generate all entities. 
        for (JsonElement entityinfo : dungeonJson.get("entities").getAsJsonArray()) {
            int xPosition;
            int yPosition;
            switch (entityinfo.getAsJsonObject().get("type").getAsString()) {
                case "player":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    this.player = new Player(Integer.toString(latestUnusedId), "player", new Position(xPosition, yPosition), false, configMap.get("player_attack"), configMap.get("player_health"));
                    // Player in entity array shares the same instance of Player class with this.player
                    entities.add(this.player);
                    this.latestUnusedId++;
                    break;

                case "wall":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Wall wall = new Wall(Integer.toString(latestUnusedId), "wall", new Position(xPosition, yPosition), false);
                    entities.add(wall);
                    this.latestUnusedId++;
                    break;

                case "exit":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Exit exit = new Exit(Integer.toString(latestUnusedId), "exit", new Position(xPosition, yPosition), false);
                    entities.add(exit);
                    this.latestUnusedId++;
                    break;

                case "boulder":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Boulder boulder = new Boulder(Integer.toString(latestUnusedId), "boulder", new Position(xPosition, yPosition), false);
                    entities.add(boulder);
                    this.latestUnusedId++;
                    break;

                case "switch":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    FloorSwitch floorSwitch = new FloorSwitch(Integer.toString(latestUnusedId), "switch", new Position(xPosition, yPosition), false);
                    entities.add(floorSwitch);
                    this.latestUnusedId++;
                    break;
                    
                case "door":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Door door = new Door(Integer.toString(latestUnusedId), "door", new Position(xPosition, yPosition), false);
                    entities.add(door);
                    this.latestUnusedId++;
                    break;
                    
                case "portal":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Portal portal = new Portal(Integer.toString(latestUnusedId), "portal", new Position(xPosition, yPosition), false);
                    entities.add(portal);
                    this.latestUnusedId++;
                    break;

                case "zombie_toast_spawner":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(Integer.toString(latestUnusedId), "zombie_toast_spawner", new Position(xPosition, yPosition), true);
                    entities.add(zombieToastSpawner);
                    interactablEntities.add(zombieToastSpawner);
                    this.latestUnusedId++;
                    break;
                
                case "spider":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Spider spider = new Spider(Integer.toString(latestUnusedId), "spider", new Position(xPosition, yPosition), false, configMap.get("spider_attack"), configMap.get("spider_health"));
                    entities.add(spider);
                    this.latestUnusedId++;
                    break;
                
                case "zombie_toast":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    ZombieToast zombieToast = new ZombieToast(Integer.toString(latestUnusedId), "zombie_toast", new Position(xPosition, yPosition), false, configMap.get("zombie_attack"), configMap.get("zombie_health"));
                    entities.add(zombieToast);
                    this.latestUnusedId++;
                    break;

                case "mercenary":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Mercenary mercenary = new Mercenary(Integer.toString(latestUnusedId), "mercenary", new Position(xPosition, yPosition), true, 
                                                        configMap.get("ally_attack"), configMap.get("ally_defence"), 
                                                        configMap.get("bribe_amount"), configMap.get("bribe_radius"), 
                                                        configMap.get("mercenary_attack"), configMap.get("mercenary_health"));
                    entities.add(mercenary);
                    interactablEntities.add(mercenary);
                    this.latestUnusedId++;
                    break;

                case "treasure":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Treasure treasure = new Treasure(Integer.toString(latestUnusedId), "treasure", new Position(xPosition, yPosition), false);
                    entities.add(treasure);
                    this.latestUnusedId++;
                    break;
                
                case "key":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Key key = new Key(Integer.toString(latestUnusedId), "key", new Position(xPosition, yPosition), false);
                    entities.add(key);
                    this.latestUnusedId++;
                    break;

                case "invincibility_potion":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    InvincibilityPotion invincibilityPotion = new InvincibilityPotion(Integer.toString(latestUnusedId), "invincibility_potion", 
                                        new Position(xPosition, yPosition), false, configMap.get("invincibility_potion_duration"));
                    entities.add(invincibilityPotion);
                    this.latestUnusedId++;
                    break;

                case "invisibility_potion":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    InvisibilityPotion invisibilityPotion = new InvisibilityPotion(Integer.toString(latestUnusedId), "invisibility_potion", 
                                        new Position(xPosition, yPosition), false, configMap.get("invisibility_potion_duration"));
                    entities.add(invisibilityPotion);
                    this.latestUnusedId++;
                    break;

                case "wood":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Wood wood = new Wood(Integer.toString(latestUnusedId), "wood", new Position(xPosition, yPosition), false);
                    entities.add(wood);
                    this.latestUnusedId++;
                    break;

                case "arrow":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Arrow arrow = new Arrow(Integer.toString(latestUnusedId), "arrow", new Position(xPosition, yPosition), false);
                    entities.add(arrow);
                    this.latestUnusedId++;
                    break;

                case "bomb":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Bomb bomb = new Bomb(Integer.toString(latestUnusedId), "bomb", new Position(xPosition, yPosition), false, configMap.get("bomb_radius"));
                    entities.add(bomb);
                    this.latestUnusedId++;
                    break;

                case "sword":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Sword sword = new Sword(Integer.toString(latestUnusedId), "sword", new Position(xPosition, yPosition), false, configMap.get("sword_attack"), configMap.get("sword_durability"));
                    entities.add(sword);
                    this.latestUnusedId++;
                    break;

                default:
                    break;
            }
        }
    }

    // Sets this.spawner to an instance of an EnemyFactory class
    public void generateSpawner() {
        configMap.get("spider_spawn_rate");
        configMap.get("zombie_spawn_rate");
        this.spawner = new EnemyFactory(configMap.get("spider_spawn_rate"), configMap.get("zombie_spawn_rate"));
    }

    // Make the goals composite pattern
    public void setGoals(JsonObject dungeonJson) {
        JsonObject goal = dungeonJson.get("goal-condition").getAsJsonObject();
        this.goals = setGoalsHelper(goal);
    }

    // Recursive function to build the composite pattern for goals.
    public Goal setGoalsHelper(JsonObject subGoal) {
        Goal newgoal = null;
        if (subGoal.get("goal").getAsString().equals("AND") || 
            subGoal.get("goal").getAsString().equals("OR")) {

            newgoal = new ComplexGoal(subGoal.get("goal").getAsString(), false);
            newgoal.add(setGoalsHelper(subGoal.get("subgoals").getAsJsonArray().get(0).getAsJsonObject()));
            newgoal.add(setGoalsHelper(subGoal.get("subgoals").getAsJsonArray().get(1).getAsJsonObject()));
        }

        else {
            switch (subGoal.get("goal").getAsString()) {
                case "enemies":
                    newgoal = new EnemiesGoal("enemies", false, configMap.get("enemy_goal"));
                break;
                case "boulders":
                    newgoal = new BouldersGoal("boulders", false);
                break;
                case "treasure":
                    newgoal = new TreasureGoal("treasure", false, configMap.get("treasure_goal"));
                break;
                case "exit":
                    newgoal = new ExitGoal("exit", false);
                break;
            }
        }
        return newgoal;
    }
}
