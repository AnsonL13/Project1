package dungeonmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.CollectableEntities.Key;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

public class Player implements Entity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private double playerAttack;
    private double playerHealth;
    List<Item> inventory = new ArrayList<Item>();
    Map<String, Key> keys = new HashMap<String, Key>();
    List<Weapon> weapons = new ArrayList<Weapon>();
    List<Enemy> enemies = new ArrayList<Enemy>();
    List<Item> potionQueue = new ArrayList<Item>();

    public Player(String id, String type, Position position, boolean isInteractable, double playerAttack, double playerHealth) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.playerAttack = playerAttack;
        this.playerHealth = playerHealth;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getId() {
        return id;
    }

    public final String getType() {
        return type;
    }

    public final Position getPosition() {
        return position;
    }

    public double getPlayerAttack() {
        return playerAttack;
    }

    public double getPlayerHealth() {
        return playerHealth;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addToInventory(Item item) {
        this.inventory.add(item);

        if (item instanceof Weapon) {
            this.weapons.add((Weapon) item);
        }

        if (item instanceof Key) {
            this.keys.put(item.getId(), (Key) item);
        }
    }

    public void addToWeapons(Weapon weapon) {
        this.weapons.add(weapon);
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public Item currentPotion() {
        // Gets the current potion being used by the player. 
        if (potionQueue.size() < 1) return null;
        return potionQueue.get(0);
    }

    // Get the players weapons
    public List<Weapon> getPlayerWeapons() {
        boolean foundSword = false;
        boolean foundBow = false;
        boolean foundShield = false;

        List<Weapon> weaponsForBattle = new ArrayList<Weapon>();
        List<Weapon> removeWeapons = new ArrayList<Weapon>();
        for (Weapon weapon : weapons) {
            if (weapon.getType().equals("sword") && !foundSword) {
                weapon.decreaseDurability();
                weaponsForBattle.add(weapon);
                foundSword = true;

                if (weapon.getDurability() == 0) {
                    removeWeapons.add(weapon);
                    //weapons.remove(weapon);
                    inventory.remove(weapon);
                }
            }

            if (weapon.getType().equals("bow") && !foundBow) {
                weapon.decreaseDurability();
                weaponsForBattle.add(weapon);
                foundBow = true;

                if (weapon.getDurability() == 0) {
                    removeWeapons.add(weapon);

                    //weapons.remove(weapon);
                    inventory.remove(weapon);
                }
            }

            if (weapon.getType().equals("shield") && !foundShield) {
                weapon.decreaseDurability();
                weaponsForBattle.add(weapon);
                foundShield = false;

                if (weapon.getDurability() == 0) {
                    removeWeapons.add(weapon);

                   // weapons.remove(weapon);
                    inventory.remove(weapon);
                }
            }
        }
        weapons.removeAll(removeWeapons);
        return weaponsForBattle;
    }

    public void removeForShield() {
        // Remove 2 wood and a treasure or key (Removes the first treasure/key in array)
        int woodCount = 0;
        int treasurekeyCount = 0;
        
        for (Item item : inventory) {
            if (item.getType().equals("wood")) {
                if (woodCount == 2) {
                    continue;
                }
                else {
                    inventory.remove(item);
                    woodCount++;
                }
            }

            else if (item.getType().equals("treasure")) {
                if (treasurekeyCount == 1) {
                    continue;
                }
                else {
                    inventory.remove(item);
                    treasurekeyCount++;
                }
            }

            else if (item.getType().equals("key")) {
                if (treasurekeyCount == 1) {
                    continue;
                }
                else {
                    inventory.remove(item);
                    treasurekeyCount++;
                }
            }
        }
    }

    public void removeForBow() {
        // Remove 1 wood and 3 arrows
        int woodCount = 0;
        int arrowCount = 0;
        
        for (Item item : inventory) {
            if (item.getType().equals("wood")) {
                if (woodCount == 1) {
                    continue;
                }
                else {
                    inventory.remove(item);
                    woodCount++;
                }
            }

            else if (item.getType().equals("wood")) {
                if (arrowCount == 3) {
                    continue;
                }
                else {
                    inventory.remove(item);
                    arrowCount++;
                }
            }
        }
    }

    public int treasureAmount() {
        int treasureCount = 0;
        for (Item item : inventory) {
            if (item.getType().equals("treasure")) {
                treasureCount++;
            }
        }

        return treasureCount;
    }

    public void decreaseSwordDurability() {
        for (Weapon weapon : weapons) {
            if (weapon.getType().equals("sword")) {
                weapon.decreaseDurability();
                break;
            }
        }
    }

    // Use an item
    public void useItem(String itemUsedId) {
        for (Item item : inventory) {
            if (item.getId().equals(itemUsedId)) {
                if (item instanceof Bomb) {
                    // Put the bomb down
                    ((Bomb) item).putDown();
                }

                else if (item.getType().equals("invincibility_potion")) {
                    // Remove from inventory
                    // Add to potions queue
                }

                else if (item.getType().equals("invisibility_potion")) {
                    // Remove from inventory
                    // Add to potions queue
                }
                break;
            }
        }
    }

    public List<Battle> battle() {
        List<Battle> battles = new ArrayList<Battle>();
        for (Enemy enemy : enemies) {
            // Check if player and enemy is on the the same square. 
            if (enemy.getPosition().getX() == position.getX() && enemy.getPosition().getY() == position.getY()) {
                // Start the battle.
                Battle battle = enemy.battleCalculate(this);
                battles.add(battle);
                // Check if the player or enemy won
                if (battle.isPlayerWon()) {
                    // The player won, remove enemy from the list. 
                    enemies.remove(enemy);
                }

                if (battle.isEnemyWon()) {
                    // The enemy won. No more battles can occur
                    break;
                }
            }
        }
        return battles;
    }

    // Unlock a door
    public boolean unlockDoor(int KeyId) {
        boolean foundKey = false;
        for (String key : keys.keySet()) {
            // Check if the corresponding key exists.
            if (keys.get(key).getKey() == KeyId) {
                // Remove key from inventory
                removeFromInventory(key);
                keys.remove(key);
                foundKey = true;
            }
        }
        return foundKey;
    }

    public void setPlayerHealth(double playerHealth) {
        this.playerHealth = playerHealth;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void removeFromInventory(String Id) {
        for (Item item : inventory) {
            if (item.getId().equals(Id)) {
                inventory.remove(item);
                break;
            }
        }
    }
}
