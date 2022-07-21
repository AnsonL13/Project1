package dungeonmania;

import java.util.HashMap;
import java.util.List;

import dungeonmania.BuildableEntities.Bow;
import dungeonmania.BuildableEntities.MidnightArmour;
import dungeonmania.BuildableEntities.Sceptre;
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
        if (! (buildable.equals("shield") || buildable.equals("bow")) || buildable.equals("sceptre") || buildable.equals("midnight_armour")) {
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
            // Add item to players inventory
            player.addToInventory(shield);
            // Add item to players weapons
            player.addToWeapons(shield);
            // Remove the materials that made the shield
            player.removeForShield(); 
        } else if (buildable.equals("bow")) {
            Bow bow = new Bow(Integer.toString(latestUnusedId), "bow", 
                    false, configMap.get("bow_durability"));
            // Add item to players inventory
            player.addToInventory(bow);
            // Add item to players weapons
            player.addToWeapons(bow);
            // Remove the materials that made the shield
            player.removeForBow();
        } else if (buildable.equals("sceptre")){
            Sceptre sceptre = new Sceptre(Integer.toString(latestUnusedId), "sceptre", 
                    false, configMap.get("controlTime"));
            // Add item to players inventory
            player.addToInventory(sceptre);
            // Add item to players weapons
            // player.addToWeapons(sceptre);
            // Remove the materials that made the shield
            player.removeForSceptre();
        } else if (buildable.equals("midnight_armour")) {
            MidnightArmour midnightArmour = new MidnightArmour(Integer.toString(latestUnusedId), "midnight_armour", 
                    false, configMap.get("midnight_armour_defence"), configMap.get("midnight_armour_attack"));
            // Add item to players inventory
            player.addToInventory(midnightArmour);
            // Add item to players weapons
            player.addToWeapons(midnightArmour);
            // Remove the materials that made the shield
            player.removeForMidnightArmour();
        }
    }
}
