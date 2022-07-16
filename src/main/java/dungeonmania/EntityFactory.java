package dungeonmania;

import java.util.HashMap;

import com.google.gson.JsonElement;

import dungeonmania.CollectableEntities.Arrow;
import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.CollectableEntities.InvincibilityPotion;
import dungeonmania.CollectableEntities.InvisibilityPotion;
import dungeonmania.CollectableEntities.Key;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.CollectableEntities.Treasure;
import dungeonmania.CollectableEntities.Wood;
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
import dungeonmania.util.Position;

public class EntityFactory {
    private Dungeon dungeon;
    private HashMap<String, Integer> configMap;
    public EntityFactory (Dungeon dungeon, HashMap<String, Integer> configMap) {
        this.dungeon = dungeon;
        this.configMap = configMap;
    }
    
    /** 
     * Generate all entities for dungeon class
     * @param entityinfo
     * @param latestUnusedId
     * @return MovingEntity for player
     */
    public MovingEntity createEntity(JsonElement entityinfo, int latestUnusedId) {
        int xPosition;
        int yPosition;
        int keyId;
        switch (entityinfo.getAsJsonObject().get("type").getAsString()) {
            case "player":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Player player = new Player(Integer.toString(latestUnusedId), "player", new Position(xPosition, yPosition), false, configMap.get("player_attack"), configMap.get("player_health"));
                // Player in entity array shares the same instance of Player class with this.player
                dungeon.addToEntities(player);
                dungeon.setPlayer(player);
                break;

            case "wall":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Wall wall = new Wall(Integer.toString(latestUnusedId), "wall", new Position(xPosition, yPosition), false);
                dungeon.addToEntities(wall);
                break;

            case "exit":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Exit exit = new Exit(Integer.toString(latestUnusedId), "exit", new Position(xPosition, yPosition), false);
                dungeon.addToEntities(exit);
                break;

            case "boulder":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Boulder boulder = new Boulder(Integer.toString(latestUnusedId), "boulder", new Position(xPosition, yPosition), false);
                dungeon.addToEntities(boulder);
                break;

            case "switch":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                FloorSwitch floorSwitch = new FloorSwitch(Integer.toString(latestUnusedId), "switch", new Position(xPosition, yPosition), false);
                dungeon.addToEntities(floorSwitch);
                break;
                
            case "door":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                keyId = entityinfo.getAsJsonObject().get("key").getAsInt();
                Door door = new Door(Integer.toString(latestUnusedId), "door", new Position(xPosition, yPosition), false, keyId);
                dungeon.addToEntities(door);
                dungeon.addToDoors(Integer.toString(latestUnusedId), door);
                break;
                
            case "portal":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                String colour = entityinfo.getAsJsonObject().get("colour").getAsString();
                Portal portal = new Portal(Integer.toString(latestUnusedId), "portal", new Position(xPosition, yPosition), false, colour);
                dungeon.addToEntities(portal);
                dungeon.addToPortals(Integer.toString(latestUnusedId), portal);
                break;

            case "zombie_toast_spawner":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(Integer.toString(latestUnusedId), "zombie_toast_spawner", new Position(xPosition, yPosition), true);
                dungeon.addToEntities(zombieToastSpawner);
                dungeon.addToInteractable(zombieToastSpawner);
                break;
            
            case "spider":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Spider spider = new Spider(Integer.toString(latestUnusedId), "spider", new Position(xPosition, yPosition), false, configMap.get("spider_attack"), configMap.get("spider_health"));
                dungeon.addToEntities(spider);
                return spider;
                
            
            case "zombie_toast":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                ZombieToast zombieToast = new ZombieToast(Integer.toString(latestUnusedId), "zombie_toast", new Position(xPosition, yPosition), false, configMap.get("zombie_attack"), configMap.get("zombie_health"));
                dungeon.addToEntities(zombieToast);
                return zombieToast;

            case "mercenary":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Mercenary mercenary = new Mercenary(Integer.toString(latestUnusedId), "mercenary", new Position(xPosition, yPosition), true, 
                                                    configMap.get("ally_attack"), configMap.get("ally_defence"), 
                                                    configMap.get("bribe_amount"), configMap.get("bribe_radius"), 
                                                    configMap.get("mercenary_attack"), configMap.get("mercenary_health"));
                dungeon.addToEntities(mercenary);
                dungeon.addToInteractable(mercenary);
                return mercenary;

            case "treasure":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Treasure treasure = new Treasure(Integer.toString(latestUnusedId), "treasure", new Position(xPosition, yPosition), false);
                dungeon.addToEntities(treasure);
                dungeon.addToCollectableEntities(Integer.toString(latestUnusedId), treasure);
                break;
            
            case "key":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                keyId = entityinfo.getAsJsonObject().get("key").getAsInt();
                Key key = new Key(Integer.toString(latestUnusedId), "key", new Position(xPosition, yPosition), false, keyId);
                dungeon.addToEntities(key);
                dungeon.addToCollectableEntities(Integer.toString(latestUnusedId), key);
                break;

            case "invincibility_potion":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                InvincibilityPotion invincibilityPotion = new InvincibilityPotion(Integer.toString(latestUnusedId), "invincibility_potion", 
                                    new Position(xPosition, yPosition), false, configMap.get("invincibility_potion_duration"));
                dungeon.addToEntities(invincibilityPotion);
                dungeon.addToCollectableEntities(Integer.toString(latestUnusedId), invincibilityPotion);
                break;

            case "invisibility_potion":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                InvisibilityPotion invisibilityPotion = new InvisibilityPotion(Integer.toString(latestUnusedId), "invisibility_potion", 
                                    new Position(xPosition, yPosition), false, configMap.get("invisibility_potion_duration"));
                dungeon.addToEntities(invisibilityPotion);
                dungeon.addToCollectableEntities(Integer.toString(latestUnusedId), invisibilityPotion);
                break;

            case "wood":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Wood wood = new Wood(Integer.toString(latestUnusedId), "wood", new Position(xPosition, yPosition), false);
                dungeon.addToEntities(wood);
                dungeon.addToCollectableEntities(Integer.toString(latestUnusedId), wood);
                break;

            case "arrow":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Arrow arrow = new Arrow(Integer.toString(latestUnusedId), "arrow", new Position(xPosition, yPosition), false);
                dungeon.addToEntities(arrow);
                dungeon.addToCollectableEntities(Integer.toString(latestUnusedId), arrow);
                break;

            case "bomb":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Bomb bomb = new Bomb(Integer.toString(latestUnusedId), "bomb", new Position(xPosition, yPosition), false, configMap.get("bomb_radius"), dungeon, dungeon.getPlayer());
                dungeon.addToEntities(bomb);
                dungeon.addToBombs(bomb);
                dungeon.addToCollectableEntities(Integer.toString(latestUnusedId), bomb);
                break;

            case "sword":
                xPosition = entityinfo.getAsJsonObject().get("x").getAsInt();
                yPosition = entityinfo.getAsJsonObject().get("y").getAsInt();
                Sword sword = new Sword(Integer.toString(latestUnusedId), "sword", new Position(xPosition, yPosition), false, configMap.get("sword_attack"), configMap.get("sword_durability"));
                dungeon.addToEntities(sword);
                dungeon.addToCollectableEntities(Integer.toString(latestUnusedId), sword);
                break;

            default:
                break;
        }
        return null;
    }        
}
