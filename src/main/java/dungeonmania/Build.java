package dungeonmania;

import java.util.HashMap;
import java.util.List;

import dungeonmania.BuildableEntities.Bow;
import dungeonmania.BuildableEntities.Shield;
import dungeonmania.exceptions.InvalidActionException;

public class Build {
    private Dungeon dungeon;
    private HashMap<String, Integer> configMap;
    public Build(Dungeon dungeon, HashMap<String, Integer> configMap) {
        this.dungeon = dungeon;
        this.configMap = configMap;
    }
    
    /** 
     * @param buildable name of item to build
     * @param player
     * @param latestUnusedId
     * @throws InvalidActionException
     */
    public void build (String buildable, Player player, int latestUnusedId) 
                throws InvalidActionException {
        if (! (buildable.equals("shield") || buildable.equals("bow"))) {
            throw new IllegalArgumentException(buildable);
        }

        // Check if possible to build, throw any excpetions
        List<String> buildables = dungeon.getBuildables();
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
            Shield shield = new Shield(Integer.toString(latestUnusedId), 
                    "shield", false, configMap.get("shield_defence"), 
                    configMap.get("shield_durability"));
            player.addToInventory(shield);
            player.addToWeapons(shield);
            player.removeForShield(); 
        } else if (buildable.equals("bow")) {
            Bow bow = new Bow(Integer.toString(latestUnusedId), "bow", 
                    false, configMap.get("bow_durability"));
            player.addToInventory(bow);
            player.addToWeapons(bow);
            player.removeForBow();
        }
    }
    
}
