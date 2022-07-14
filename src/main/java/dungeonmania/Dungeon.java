package dungeonmania;

import dungeonmania.BuildableEntities.Bow;
import dungeonmania.BuildableEntities.Shield;
import dungeonmania.CollectableEntities.Arrow;
import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.CollectableEntities.CollectableEntity;
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
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.StaticEntities.ZombieToastSpawner;
//import dungeonmania.enemy.Mercenary;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.HashMap;

import com.google.gson.Gson;
import java.util.stream.Collector;
import java.util.stream.Collectors;


import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
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
    List<InteractableEntity> interactableEntities = new ArrayList<InteractableEntity>();
    List<Battle> battles = new ArrayList<Battle>();
    Map<String, Door> doors = new HashMap<String, Door>();
    Map<String, Portal> portals = new HashMap<String, Portal>();
    Map<String, Bomb> bombs = new HashMap<String, Bomb>();
    Map<String, CollectableEntity> collectableEntities = new HashMap<String, CollectableEntity>();

    private List<Enemy> enemies = new ArrayList<Enemy>();

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

    public List<Battle> getBattles() {
        return battles;
    }

    public void addToBattles(Battle battle) {
        this.battles.add(battle);
    }

    public void addToEntities(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(String Id) {
        for (Entity entity : entities) {
            if (entity.getId().equals(Id)) {
                entities.remove(entity);
                break;
            }
        }
    }

    public Player getPlayer() {
        return player;
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
                    throw new IllegalArgumentException(itemUsedId);
                }
                foundItem = true;
            }
        }

        if (! foundItem) {
            throw new InvalidActionException(itemUsedId);
        }

        // Player uses the item
        player.useItem(itemUsedId);

        // TODO: Enemy movement
        
        // Battles
        startBattles();

        // Spawn enemies
        String nextID = Integer.toString(latestUnusedId);
        List<MovingEntity> newEnemy = spawner.spawn(nextID, getSpawner(), entities);
        entities.addAll(newEnemy);
        player.addAllEnemies(newEnemy);
        latestUnusedId+= newEnemy.size();
    }

    /**
     * /game/tick/movement
     */
    public void tick(Direction movementDirection) {
        // Check the square that the player will move into
        Position targetSquare = player.getPosition().translateBy(movementDirection);

        // Check if movement into a static entity
        boolean normalMove = moveIntoStaticEntity(movementDirection, targetSquare);

        // Normal player movement
        if (normalMove) {
            player.setPosition(targetSquare);
        }

        // Check if moved into a collectable entity
        for (String collectableEntity : collectableEntities.keySet()) {
            Position collectablePosition = collectableEntities.get(collectableEntity).getPosition();
            // Check if collectable entity is in the same square as the player. 
            if (collectablePosition.getX() == targetSquare.getX() && collectablePosition.getY() == targetSquare.getY()) {
                // Check if the item is a bomb
                if (collectableEntities.get(collectableEntity).getType().equals("bomb")) {
                    for (String bomb : bombs.keySet()) {
                        if (bombs.get(bomb).getId().equals(collectableEntities.get(collectableEntity).getId())) {
                            // Change the state of the bomb
                            bombs.get(bomb).pickUp();
                            break;
                        }
                    }
                }
                
                else {
                    // Collect the item
                    player.addToInventory(collectableEntities.get(collectableEntity));
                    // Remove from list of entities
                    entities.remove(collectableEntities.get(collectableEntity));
                    collectableEntities.remove(collectableEntity);
                    break;
                }
            }
        }

        // Check if moved into an enemy (Battle)
        startBattles();

        // Move enemies should be done in player
        
        // Check if Enemy has moved into a player (Battle)
        startBattles();
        
        // Spawn enemies
        String nextID = Integer.toString(latestUnusedId);
        List<MovingEntity> newEnemy = spawner.spawn(nextID, getSpawner(), entities);
        entities.addAll(newEnemy);
        player.addAllEnemies(newEnemy);
        latestUnusedId+= newEnemy.size();
    }

    private List<ZombieToastSpawner> getSpawner() {
        List<ZombieToastSpawner> allSpawners = entities.stream()
            .filter( o -> o instanceof ZombieToastSpawner)
            .map(ZombieToastSpawner.class::cast)
            .collect(Collectors.toList());
        return allSpawners;
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
        } else if (buildable.equals("bow")) {
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
        for (Entity entity : interactableEntities) {
            if (entity.getId().equals(entityId)) {
                foundEntity = true;
            }
        }

        if (! foundEntity) {
            throw new IllegalArgumentException(entityId);
        }

        // Check for InvalidActionException
        for (InteractableEntity entity : interactableEntities) {
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
                    this.interactableEntities.remove(entity);

                    // Decrease sword durability
                    player.decreaseSwordDurability();
                    break;
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
        List<MovingEntity> movingEntities = new ArrayList<MovingEntity>();

        for (JsonElement entityinfo : dungeonJson.get("entities").getAsJsonArray()) {
            int xPosition;
            int yPosition;
            int keyId;
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
                    keyId = entityinfo.getAsJsonObject().get("key").getAsInt();
                    Door door = new Door(Integer.toString(latestUnusedId), "door", new Position(xPosition, yPosition), false, keyId);
                    entities.add(door);
                    doors.put(Integer.toString(latestUnusedId), door);
                    this.latestUnusedId++;
                    break;
                    
                case "portal":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    String colour = entityinfo.getAsJsonObject().get("colour").getAsString();
                    Portal portal = new Portal(Integer.toString(latestUnusedId), "portal", new Position(xPosition, yPosition), false, colour);
                    entities.add(portal);
                    portals.put(Integer.toString(latestUnusedId), portal);
                    this.latestUnusedId++;
                    break;

                case "zombie_toast_spawner":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(Integer.toString(latestUnusedId), "zombie_toast_spawner", new Position(xPosition, yPosition), true);
                    entities.add(zombieToastSpawner);
                    interactableEntities.add(zombieToastSpawner);
                    this.latestUnusedId++;
                    break;
                
                case "spider":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Spider spider = new Spider(Integer.toString(latestUnusedId), "spider", new Position(xPosition, yPosition), false, configMap.get("spider_attack"), configMap.get("spider_health"));
                    entities.add(spider);
                    movingEntities.add(spider);
                    this.latestUnusedId++;
                    break;
                
                case "zombie_toast":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    ZombieToast zombieToast = new ZombieToast(Integer.toString(latestUnusedId), "zombie_toast", new Position(xPosition, yPosition), false, configMap.get("zombie_attack"), configMap.get("zombie_health"));
                    entities.add(zombieToast);
                    movingEntities.add(zombieToast);
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
                    interactableEntities.add(mercenary);
                    movingEntities.add(mercenary);
                    this.latestUnusedId++;
                    break;

                case "treasure":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Treasure treasure = new Treasure(Integer.toString(latestUnusedId), "treasure", new Position(xPosition, yPosition), false);
                    entities.add(treasure);
                    collectableEntities.put(Integer.toString(latestUnusedId), treasure);
                    this.latestUnusedId++;
                    break;
                
                case "key":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    keyId = entityinfo.getAsJsonObject().get("key").getAsInt();
                    Key key = new Key(Integer.toString(latestUnusedId), "key", new Position(xPosition, yPosition), false, keyId);
                    entities.add(key);
                    collectableEntities.put(Integer.toString(latestUnusedId), key);
                    this.latestUnusedId++;
                    break;

                case "invincibility_potion":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    InvincibilityPotion invincibilityPotion = new InvincibilityPotion(Integer.toString(latestUnusedId), "invincibility_potion", 
                                        new Position(xPosition, yPosition), false, configMap.get("invincibility_potion_duration"));
                    entities.add(invincibilityPotion);
                    collectableEntities.put(Integer.toString(latestUnusedId), invincibilityPotion);
                    this.latestUnusedId++;
                    break;

                case "invisibility_potion":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    InvisibilityPotion invisibilityPotion = new InvisibilityPotion(Integer.toString(latestUnusedId), "invisibility_potion", 
                                        new Position(xPosition, yPosition), false, configMap.get("invisibility_potion_duration"));
                    entities.add(invisibilityPotion);
                    collectableEntities.put(Integer.toString(latestUnusedId), invisibilityPotion);
                    this.latestUnusedId++;
                    break;

                case "wood":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Wood wood = new Wood(Integer.toString(latestUnusedId), "wood", new Position(xPosition, yPosition), false);
                    entities.add(wood);
                    collectableEntities.put(Integer.toString(latestUnusedId), wood);
                    this.latestUnusedId++;
                    break;

                case "arrow":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Arrow arrow = new Arrow(Integer.toString(latestUnusedId), "arrow", new Position(xPosition, yPosition), false);
                    entities.add(arrow);
                    collectableEntities.put(Integer.toString(latestUnusedId), arrow);
                    this.latestUnusedId++;
                    break;

                case "bomb":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Bomb bomb = new Bomb(Integer.toString(latestUnusedId), "bomb", new Position(xPosition, yPosition), false, configMap.get("bomb_radius"), this, player);
                    entities.add(bomb);
                    bombs.put(Integer.toString(latestUnusedId), bomb);
                    collectableEntities.put(Integer.toString(latestUnusedId), bomb);
                    this.latestUnusedId++;
                    break;

                case "sword":
                    xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                    yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                    Sword sword = new Sword(Integer.toString(latestUnusedId), "sword", new Position(xPosition, yPosition), false, configMap.get("sword_attack"), configMap.get("sword_durability"));
                    entities.add(sword);
                    collectableEntities.put(Integer.toString(latestUnusedId), sword);
                    this.latestUnusedId++;
                    break;

                default:
                    break;
            }
        }
        player.addAllEnemies(movingEntities);
    }

    public void startBattles() {
        List<Battle> newBattles = player.battle();
        // Add all new battles to the list of battles.
        this.battles.addAll(newBattles);

        // 
        for (Battle battle : newBattles) {
            if (battle.isEnemyWon()) {
                entities.remove(player);
                break;
            }
    
            else if (battle.isPlayerWon()) {
                String id = battle.getEnemyId();
                removeEntity(id);
            }
    
            else {
                // Both the enemy and player died
                entities.remove(player);
                String id = battle.getEnemyId();
                removeEntity(id);
            }
        }
    }

    // Checks if players movement will be a static entity and handles it. 
    public boolean moveIntoStaticEntity(Direction movementDirection, Position targetSquare) {
        boolean normalMove = true;
        for (Entity entity : entities) {
            // Check if the entity is in the position the player is going to move into.
            if (entity.getPosition().getX() == targetSquare.getX() && entity.getPosition().getY() == targetSquare.getY()) {
                switch (entity.getType()) {
                    case "wall":
                        // Player does not move because there is a wall in front.
                        normalMove = false;
                        break;
    
                    case "boulder":
                        normalMove = moveIntoBoulder(movementDirection, targetSquare, entity);
                        break;
    
                    case "door":
                        normalMove = moveIntoDoor(entity);
                        break;
    
                    case "portal":
                        Position portalExitSquare = moveIntoPortal(entity, movementDirection);
                        player.setPosition(portalExitSquare);
                        normalMove = false;
                        break;
    
                    default:
                        break;
                }
            }
        }
        return normalMove;
    }

    public boolean moveIntoBoulder(Direction movementDirection, Position targetSquare, Entity entity) {
        // Check if the boulder can move
        Position nextTargetSquare = targetSquare.translateBy(movementDirection);
        for (Entity nextEntity : entities) {

            if (nextEntity.getType().equals("boulder") && 
                nextEntity.getPosition().getX() == nextTargetSquare.getX() && 
                nextEntity.getPosition().getY() == nextTargetSquare.getY()) {
                // Player cannot push the boulder
                return false;
            }

            else if (nextEntity.getType().equals("wall") && 
                    nextEntity.getPosition().getX() == nextTargetSquare.getX() && 
                    nextEntity.getPosition().getY() == nextTargetSquare.getY()) {
                // Player cannot push the boulder
                return false;
            }
        }

        // Move the boulder
        entity.setPosition(nextTargetSquare);
        return true;
    }

    public boolean moveIntoDoor(Entity entity) {
        // Check if the door is already open
        if (doors.get(entity.getId()).isOpen()) {
            return true;
        }

        // Check if the player can unlock the door
        else if (player.unlockDoor(doors.get(entity.getId()).getKey())) {
            // Player unlocked door
            doors.get(entity.getId()).setOpen(true);
            return true;
        }

        // Player cannot open the door
        else {
            return false;
        }
    }

    public Position moveIntoPortal(Entity entity, Direction movementDirection) {
        boolean foundFinalSquare = false;
            Position portalExitSquare = player.getPosition();
            Portal tempPortal = null;

            // Find the portal in the portals Hashmap.
            for (String portal : portals.keySet()) {
                if (entity.getId().equals(portals.get(portal).getId())) {
                    tempPortal = portals.get(portal);
                }
            }

            while (! foundFinalSquare) {
                portalExitSquare = portalExitSquare(tempPortal, movementDirection);

                foundFinalSquare = true;

                // Check if there is another portal on the portalExitSquare
                for (String portal : portals.keySet()) {
                    int xPostion = portals.get(portal).getPosition().getX();
                    int yPosition = portals.get(portal).getPosition().getY();
                    if (xPostion == portalExitSquare.getX() && yPosition == portalExitSquare.getY()) {
                        tempPortal = portals.get(portal);
                        foundFinalSquare = false;
                    }
                }
            }

            // Check if the portalExitSquare contains a wall, boulder or door.
            boolean normalMove = true;
            for (Entity i : entities) {
                // Check if the entity is in the position the player is going to move into.
                if (i.getPosition().getX() == portalExitSquare.getX() && entity.getPosition().getY() == portalExitSquare.getY()) {
                    switch (i.getType()) {
                        case "wall":
                            // Player does not move because there is a wall in front.
                            normalMove = false;
                            break;
        
                        case "boulder":
                            normalMove = moveIntoBoulder(movementDirection, portalExitSquare, entity);
                            break;
        
                        case "door":
                            normalMove = moveIntoDoor(entity);
                            break;
        
                        default:
                            break;
                    }
                }
            }

            if (! normalMove) {
                return player.getPosition();
            }
        return portalExitSquare;
    }

    // Given one portal and movementDirection, find the square the player will move to. 
    public Position portalExitSquare(Portal portal, Direction movementDirection) {
        // Get the corresponding portal
        for (String otherPortal : portals.keySet()) {
            // if (!Same ID && Same colour)
            if (!otherPortal.equals(portal.getId()) && portals.get(otherPortal).getColour().equals(portal.getColour())) {
                // Get the position
                return portals.get(otherPortal).getPosition().translateBy(movementDirection);
            }
        }
        return null;
    }

    // Sets this.spawner to an instance of an EnemyFactory class
    public void generateSpawner() {
        this.spawner = new EnemyFactory(configMap.get("zombie_attack"), configMap.get("zombie_health"), configMap.get("spider_attack"), configMap.get("spider_health"));
        spawner.setSpawnRate(configMap.get("spider_spawn_rate"), configMap.get("zombie_spawn_rate"));
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
                    newgoal = new EnemiesGoal("enemies", false, configMap.get("enemy_goal"), this);
                break;
                case "boulders":
                    newgoal = new BouldersGoal("boulders", false, this);
                break;
                case "treasure":
                    newgoal = new TreasureGoal("treasure", false, configMap.get("treasure_goal"), this);
                break;
                case "exit":
                    newgoal = new ExitGoal("exit", false, this);
                break;
            }
        }
        return newgoal;
    }

    //public void 
}
