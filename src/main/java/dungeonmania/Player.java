package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

public class Player implements MovingEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    public int playerAttack;
    public int playerHealth;
    List<Item> inventory = new ArrayList<Item>();
    List<Weapon> weapons = new ArrayList<Weapon>();
    List<Enemy> enemy = new ArrayList<Enemy>();
    List<Item> potionQueue = new ArrayList<Item>();

    public Player(String id, String type, Position position, boolean isInteractable, int playerAttack, int playerHealth) {
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

    public List<Item> getInventory() {
        return inventory;
    }

    public void addToInventory(Item item) {
        this.inventory.add(item);
    }

    public void addToWeapons(Weapon weapon) {
        this.weapons.add(weapon);
    }

    public List<Weapon> getWeapons() {
        return weapons;
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
                if (item.getType().equals("bomb")) {

                }

                else if (item.getType().equals("invincibility_potion")) {
                    
                }

                else if (item.getType().equals("invisibility_potion")) {
                    
                }
                break;
            }
        }
    }

    public void battle() {
        

    }
}
