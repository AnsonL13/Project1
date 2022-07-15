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

        //Move movingentities
        player.moveMovingEntities(player.getPosition(), entities);
        
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

        //Move movingentities
        player.moveMovingEntities(player.getPosition(), entities);

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
        Build buildItem = new Build(this, configMap);
        buildItem.build(buildable, player, latestUnusedId);
        latestUnusedId++;
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
        EntityFactory getEntities = new EntityFactory(this, configMap);

        for (JsonElement entityinfo : dungeonJson.get("entities").getAsJsonArray()) {
            MovingEntity newEnemies = getEntities.createEntity(entityinfo, latestUnusedId);
            if (newEnemies != null) movingEntities.add(newEnemies);
            latestUnusedId++;
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

    public void addToDoors(String latestUnusedId, Door door) {
        this.doors.put(latestUnusedId, door);
    }

    public void addToPortals(String latestUnusedId, Portal portal) {
        this.portals.put(latestUnusedId, portal);
    }    
    
    public void addToInteractable(InteractableEntity entity) {
        this.interactableEntities.add(entity);
    }

    public void addToCollectableEntities(String latestUnusedId, CollectableEntity entity) {
        this.collectableEntities.put(latestUnusedId, entity);
    }

    public void addToBombs(String latestUnusedId, Bomb entity) {
        this.bombs.put(latestUnusedId, entity);
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

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Goal getGoals() {
        return goals;
    }
}
