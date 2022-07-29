package dungeonmania;

import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.CollectableEntities.CollectableEntity;
import dungeonmania.Goals.BouldersGoal;
import dungeonmania.Goals.ComplexGoal;
import dungeonmania.Goals.EnemiesGoal;
import dungeonmania.Goals.ExitGoal;
import dungeonmania.Goals.Goal;
import dungeonmania.Goals.TreasureGoal;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.LogicalEntities.FloorSwitch;
import dungeonmania.StaticEntities.LogicalEntities.SwitchDoor;
import dungeonmania.exceptions.InvalidActionException;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.Gson;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

public class Dungeon implements Serializable {

    // All Configs stored as a hashmap
    private HashMap<String, Integer> configMap;

    // Variables for the dungeon
    private String dungeonId;
    private String dungeonName;
    private Player player;
    private EnemyFactory spawner;
    private Goal goals;
    private int latestUnusedId = 0;
    private int tickNumber;

    // Data structures for the dungeon
    private List<Entity> entities = new ArrayList<Entity>();
    private List<InteractableEntity> interactableEntities = new ArrayList<InteractableEntity>();
    private List<Battle> battles = new ArrayList<Battle>();
    private Map<String, Door> doors = new HashMap<String, Door>();
    private Map<String, SwitchDoor> switchDoors = new HashMap<String, SwitchDoor>();
    private List<FloorSwitch> logicalEntities = new ArrayList<FloorSwitch>();
    private Map<String, Portal> portals = new HashMap<String, Portal>();
    private List<Bomb> bombs = new ArrayList<Bomb>();
    private Map<String, CollectableEntity> collectableEntities = new HashMap<String, CollectableEntity>();

    public Dungeon(String dungeonName, JsonObject dungeonJson, JsonObject configJson) {
        this.dungeonId = "dungeon-0";
        this.dungeonName = dungeonName;
        // Convert JsonObject configJson into HashMap<String, Integer> configMap
        this.configMap = configHelper(configJson);
        // Initialise all entities
        generateEntities(dungeonJson, configJson);
        // Create the enemy spawner
        generateSpawner();
        // Create the goals composite pattern "tree"
        setGoals(dungeonJson);

        this.tickNumber = 0;
    }

    /**
     * /game/tick/item
     */
    public void tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        
        this.tickNumber++;
        
        // Check if item is not in the inventory
        boolean illegalItem = false;
        boolean foundItem = false;
        for (Item item : player.getInventory()) {
            if (item.getId().equals(itemUsedId)) {
                if (! (item.getType().equals("bomb") || 
                    item.getType().equals("invincibility_potion") || 
                    item.getType().equals("invisibility_potion"))) {
                    illegalItem = true;
                }
                foundItem = true;
            }
        }

        if (! illegalItem && foundItem) {
            // Player uses the item
            player.useItem(itemUsedId);
        }

        // Complete necesarry updates. 
        tickUpdates();
        
        // Check if illegal argument
        if (illegalItem) {
            throw new IllegalArgumentException("itemUsed must be one of bomb, invincibility_potion, invisibility_potion");
        }
        
        // Check if item not inside players inventory
        if (! foundItem) {
            throw new InvalidActionException(itemUsedId);
        }
    }

    /**
     * /game/tick/movement
     */
    public void tick(Direction movementDirection) {

        this.tickNumber++;

        // Check the square that the player will move into
        Position targetSquare = player.getPosition().translateBy(movementDirection);

        // Check if movement into a static entity
        boolean normalMove = moveIntoStaticEntity(movementDirection, targetSquare);

        // Normal player movement
        if (normalMove) {
            player.setPosition(targetSquare);
        }

        // Check if the player moved into a collectable entity. 
        Iterator<Entry<String, CollectableEntity>> collectableIterator = collectableEntities.entrySet().iterator();
        Entry<String, CollectableEntity> collectable;
        while(collectableIterator.hasNext()) {   
            collectable = collectableIterator.next();     
            Position collectablePosition = collectable.getValue().getPosition();
            // Check if collectable entity is in the same square as the player. 
            if (collectablePosition.getX() == targetSquare.getX() && collectablePosition.getY() == targetSquare.getY()) {

                // Check if the item is a bomb
                if (collectable.getValue().getType().equals("bomb")) {
                    for (Bomb bomb : bombs) {
                        if (bomb.getId().equals(collectable.getValue().getId())) {
                            // Change the state of the bomb
                            bomb.pickUp();
                            collectableIterator.remove();
                            break;
                        }
                    }
                }
                
                else {
                    // Collect the item
                    player.addToInventory(collectable.getValue());
                    // Remove from list of entities
                    entities.remove(collectable.getValue());
                    collectableIterator.remove();
                    break;
                }
            }
        }

        // Check if moved into an enemy (Battle)
        startBattles();

        // Complete necesarry updates. 
        tickUpdates();
    }

    private void tickUpdates() {
        // Explode any bombs
        bombs.stream().forEach(o -> o.explode());

        //Move movingentities
        player.moveMovingEntities(entities);
        
        // Check if Enemy has moved into a player (Battle)
        startBattles();
        
        // Spawn enemies
        String nextID = Integer.toString(latestUnusedId);
        List<MovingEntity> newEnemy = spawner.spawn(nextID, entities);
        entities.addAll(newEnemy);
        player.addAllEnemies(newEnemy);
        latestUnusedId+= newEnemy.size();
        
        // Update spawned enemy potion status. 
        player.updateSpawnedEnemies();

        player.updateAlliedMercenary();
        // Update player potions
        player.updatePotions();

        // Update floor switches.
        for (FloorSwitch entity : logicalEntities) {
            entity.boulderUpdate(tickNumber);
        }
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

    /*
     * Get possible items that player can build in a list of strings. 
     */
    public List<String> getBuildables() {
        int woodCount = 0;
        int arrowCount = 0;
        int treasureCount = 0;
        int keyCount = 0;
        int sunStoneCount = 0;
        int swordCount = 0;

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

                case "sun_stone":
                    sunStoneCount++;
                    break;
                
                case "sword":
                    swordCount++;
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

        // Calculate if sceptre can be created
        if ((woodCount >= 1 || arrowCount >= 2) && (keyCount >= 1 || treasureCount >= 1) && sunStoneCount >= 1) {
            buildables.add("sceptre");
        }

        // Calculate if midnight armour can be created
        
        for (Entity entity : entities) {
            if (entity.getType().equals("zombie_toast")) {
                return buildables;
            }
        }
        if (swordCount >= 1 && sunStoneCount >= 1) {
            buildables.add("midnight_armour");
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

                // Interact with the interactable entity.
                entity.interact(this);
                break;
            }
        }
    }

    /**
     * /game/rewind
     */
    public void rewind(int ticks) throws IllegalArgumentException {
        if (ticks <= 0 || ticks > tickNumber) throw new IllegalArgumentException("Invalid tick.");
             
    }

    // Convert config.json to Hashmap<String, Integer>
    public HashMap<String, Integer> configHelper(JsonObject configJson) {
        java.lang.reflect.Type mapType = new TypeToken<HashMap<String, Integer>>() {}.getType();
        return new Gson().fromJson(configJson, mapType);
    }

    // Generate entities from Dungeon.json
    public void generateEntities(JsonObject dungeonJson, JsonObject configJson) {
        // Read from dungeon json file. Generate all entities. 
        List<MovingEntity> movingEntities = new ArrayList<MovingEntity>();
        EntityFactory getEntities = new EntityFactory(this, configJson, configMap);

        for (JsonElement entityinfo : dungeonJson.get("entities").getAsJsonArray()) {
            MovingEntity newEnemies = getEntities.createEntity(entityinfo, latestUnusedId);
            if (newEnemies != null) movingEntities.add(newEnemies);
            latestUnusedId++;
        }     
        player.addAllEnemies(movingEntities);
    }

    /*
     * Begin battles. 
     */
    public void startBattles() {
        List<Battle> newBattles = player.battle();
        // Add all new battles to the list of battles.
        this.battles.addAll(newBattles);
        
        /*
         * Remove entities that lost the battle from the dungeon. 
         */
        for (Battle battle : newBattles) {
            // Enemy won
            if (battle.isEnemyWon()) {
                entities.remove(player);
                break;
            }
            
            // Player won
            else if (battle.isPlayerWon()) {
                String id = battle.getEnemyId();
                removeEntity(id);
                for (InteractableEntity entity : interactableEntities) {
                    if (entity.getId().equals(id)) {
                        interactableEntities.remove(entity);
                        break;
                    }
                }
            }
    
            else {
                // Both the enemy and player died
                entities.remove(player);
                String id = battle.getEnemyId();
                removeEntity(id);
            }
        }
    }

    // Checks if players movement will be into a static entity and handles it. 
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
                    
                    case "switch_door":
                        normalMove = moveIntoSwitchDoor(entity);
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

    /*
     * Check if player can move a boulder. 
     */
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

    /*
     * Check if player can unlock a door
     */
    public boolean moveIntoDoor(Entity entity) {
        // Check if the door is already open
        if (doors.get(entity.getId()).isOpen()) {
            return true;
        }
        // Check if player have sun stone
        for (Item item : player.getInventory()){
            if (item.getType().equals("sun_stone")) {
                return true;
            }
        }
         
        // Check if the player can unlock the door
        if (player.unlockDoor(doors.get(entity.getId()).getKey())) {
            // Player unlocked door
            doors.get(entity.getId()).setOpen(true);
            return true;
        }

        // Player cannot open the door
        else {
            return false;
        }
    }

    /*
     * Check if player can unlock a switch door
     */
    public boolean moveIntoSwitchDoor(Entity entity) {
        // Check if the door is already open
        if (switchDoors.get(entity.getId()).isOpen()) {
            return true;
        }

        // Check if the player can unlock the door
        else if (player.unlockDoor(switchDoors.get(entity.getId()).getKey())) {
            // Player unlocked door
            switchDoors.get(entity.getId()).setOpen(true);
            switchDoors.get(entity.getId()).setOpenForever(true);
            return true;
        }

        // Player cannot open the door
        else {
            return false;
        }
    }

    /*
     * Check the square which a portal will teleport the player to. 
     */
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
            newgoal = new ComplexGoal(subGoal.get("goal").getAsString());
            newgoal.add(setGoalsHelper(subGoal.get("subgoals").getAsJsonArray().get(0).getAsJsonObject()));
            newgoal.add(setGoalsHelper(subGoal.get("subgoals").getAsJsonArray().get(1).getAsJsonObject()));
        }
        else {
            switch (subGoal.get("goal").getAsString()) {
                case "enemies":
                    newgoal = new EnemiesGoal("enemies", configMap.get("enemy_goal"), this);
                break;
                case "boulders":
                    newgoal = new BouldersGoal("boulders", this);
                break;
                case "treasure":
                    newgoal = new TreasureGoal("treasure", configMap.get("treasure_goal"), this);
                break;
                case "exit":
                    newgoal = new ExitGoal("exit", this);
                break;
            }
        }
        return newgoal;
    }


    // Getters
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

    public void addToBombs(Bomb entity) {
        this.bombs.add(entity);
    }

    public void removeFromBombs(Bomb entity) {
        this.bombs.remove(entity);
    }

    public void addToLogicalEntities(FloorSwitch entity) {
        this.logicalEntities.add(entity);
    }

    public void addToSwitchDoors(String latestUnusedId, SwitchDoor door) {
        this.switchDoors.put(latestUnusedId, door);
    }

    /*
     * Remove from dungeon list of entities. 
     */
    public void removeEntity(String Id) {
        for (Entity entity : entities) {
            if (entity.getId().equals(Id)) {
                entities.remove(entity);
                break;
            }
        }
    }

    /*
     * Remove from dungeon list interactable entities
     */
    public void removeInteractableEntity(String Id) {
        for (InteractableEntity entity : interactableEntities) {
            if (entity.getId().equals(Id)) {
                interactableEntities.remove(entity);
                break;
            }
        }
    }

    /*
     * Removes entity from all data structres except collectable entities,
     * for bomb explosion.
     */
    public void removeEntityFully(String Id) {
        for (Entity entity : entities) {
            if (entity.getId().equals(Id)) {
                if (entity instanceof InteractableEntity) {
                    interactableEntities.remove(entity);
                }

                if (entity instanceof Door) {
                    doors.remove(Id);
                }

                if (entity instanceof Portal) {
                    portals.remove(Id);
                }

                if (entity instanceof Bomb) {
                    bombs.remove(entity);
                }
                break;
            }
        }
    }

    /*
     * Remove a collectable entity
     */
    public void removeCollectable(String Id) {
        for (String collectableEntity : collectableEntities.keySet()) {
            if (collectableEntity.equals(Id)) {
                collectableEntities.remove(collectableEntity);
            }
            break;
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
