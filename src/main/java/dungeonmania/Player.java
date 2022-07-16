package dungeonmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dungeonmania.CollectableEntities.Arrow;
import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.CollectableEntities.InvincibilityPotion;
import dungeonmania.CollectableEntities.InvisibilityPotion;
import dungeonmania.CollectableEntities.Key;
import dungeonmania.CollectableEntities.Potion;
import dungeonmania.CollectableEntities.Treasure;
import dungeonmania.CollectableEntities.Wood;
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
    List<Potion> potionQueue = new ArrayList<Potion>();

    List<MovingEntity> movingEntities = new ArrayList<MovingEntity>();

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

    public List<MovingEntity> getMovingEntities() {
        return movingEntities;
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
        if (potionQueue.size() == 0) return null;
        return potionQueue.get(0);
    }

    public void addToMovingEntites(MovingEntity entity) {
        this.movingEntities.add(entity);
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

        Iterator<Item> inventoryIterator = inventory.iterator();
        Item item;
        while(inventoryIterator.hasNext()) {     
            item = inventoryIterator.next();     

            if (item instanceof Wood && (woodCount != 2)) {
                inventoryIterator.remove();
                woodCount++;
            }

            else if (item instanceof Key && (treasurekeyCount != 1)) {
                keys.remove(item.getId());
                inventoryIterator.remove();
                treasurekeyCount++;
            }

            else if (item instanceof Treasure && (treasurekeyCount != 1)) {
                inventoryIterator.remove();
                treasurekeyCount++;
            }
        }
    }

    public void removeForBow() {
        // Remove 1 wood and 3 arrows
        int woodCount = 0;
        int arrowCount = 0;

        Iterator<Item> inventoryIterator = inventory.iterator();
        Item item;
        while(inventoryIterator.hasNext()) {     
            item = inventoryIterator.next();     

            if (item instanceof Wood && (woodCount != 1)) {
                inventoryIterator.remove();
                woodCount++;
            }

            else if (item instanceof Arrow && (arrowCount != 3)) {
                inventoryIterator.remove();
                arrowCount++;
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
                    if (item instanceof Bomb) {
                        // Put the bomb down
                        ((Bomb) item).putDown();
                    }    
                }

                else if (item.getType().equals("invincibility_potion")) {
                    // Remove from inventory
                    inventory.remove(item);

                    // Add to potions queue
                    potionQueue.add((Potion) item);

                    // Changing enemy status
                    updateSpawnedEnemies();
                    
                    break;
                }

                else if (item.getType().equals("invisibility_potion")) {
                    // Remove from inventory
                    inventory.remove(item);

                    // Add to potions queue
                    potionQueue.add((Potion) item);

                    // Changing enemy status
                    updateSpawnedEnemies();

                    break;
                }
                break;
            }
        }
    }

    public List<Battle> battle() {
        List<Battle> battles = new ArrayList<Battle>();
        if (isInvisible()) return battles;
        Iterator<MovingEntity> enemyIterator = movingEntities.iterator();
        MovingEntity enemy;
        while(enemyIterator.hasNext()) {
            enemy = enemyIterator.next();
            if (enemy.getPosition().getX() == position.getX() && enemy.getPosition().getY() == position.getY()) {
                // Start the battle.
                Battle battle = Battle.battleCalculate(this, enemy);
                battles.add(battle);
                // Check if the player or enemy won
                if (battle.isPlayerWon()) {
                    // Remove from movingentities
                    enemyIterator.remove();
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
                break;
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

    public void addAllEnemies (List<MovingEntity> newMovingEntities) {
        if (newMovingEntities == null) return;
        if (newMovingEntities.size() == 0) return;
        movingEntities.addAll(newMovingEntities);
    }

    public void moveMovingEntities(List<Entity> entities) {
        movingEntities.stream().forEach(o -> o.move(new Position(this.position.getX(), this.position.getY()), entities));
    }

    public void updatePotions() {
        boolean goToNextPotion = false;
        if (potionQueue.size() > 0) {
            Potion currentPotion = potionQueue.get(0);
            currentPotion.decrementDuration();
            if (currentPotion.getDuration() == 0) {
                potionQueue.remove(currentPotion);
                goToNextPotion = true;
            }
        }

        // Go to the next potion. 
        if (goToNextPotion) {
            updateSpawnedEnemies();
        }
    }

    public void updateSpawnedEnemies() {
        if (potionQueue.size() > 0) {
            if (potionQueue.get(0) instanceof InvincibilityPotion) {
                movingEntities.stream().forEach(o -> o.setPotionStatus(false, true));
            }

            else if (potionQueue.get(0) instanceof InvisibilityPotion) {
                movingEntities.stream().forEach(o -> o.setPotionStatus(true, false));
            }
        }
    }

    public boolean isInvisible() {
        if (potionQueue.size() > 0) {
            if (potionQueue.get(0) instanceof InvisibilityPotion) {
                return true;
            }
        }
        return false;
    }

    public void removeFromMovingEntities(String Id) {
        for (MovingEntity entity : movingEntities) {
            if (entity.getId().equals(Id)) {
                movingEntities.remove(entity);
                break;
            }
        }
    }
}
