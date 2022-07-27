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
import dungeonmania.CollectableEntities.SunStone;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.CollectableEntities.Treasure;
import dungeonmania.CollectableEntities.Wood;
import dungeonmania.MovingEntities.AlliedEntities;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

public class Player implements Entity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private double playerAttack;
    private double playerHealth;
    private List<Item> inventory = new ArrayList<Item>();
    private Map<String, Key> keys = new HashMap<String, Key>();
    private List<Weapon> weapons = new ArrayList<Weapon>();
    private List<Potion> potionQueue = new ArrayList<Potion>();
    private List<AlliedEntities> allies = new ArrayList<AlliedEntities>();

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

    /*
     * Add an item to the players inventory. 
     */
    public void addToInventory(Item item) {
        this.inventory.add(item);

        if (item instanceof Weapon) {
            this.weapons.add((Weapon) item);
        }

        if (item instanceof Key) {
            this.keys.put(item.getId(), (Key) item);
        }
    }

    /*
     * Add a weapon to the list of weapons
     */
    public void addToWeapons(Weapon weapon) {
        this.weapons.add(weapon);
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    /*
     * Get the current potion being used by the player
     */
    public Item currentPotion() {
        if (potionQueue.size() == 0) return null;
        return potionQueue.get(0);
    }

    /*
     * Add an enemy (observer) to the list of movingEntities
     */
    public void addToMovingEntites(MovingEntity entity) {
        this.movingEntities.add(entity);
    }

    /*
     * Add a mercenary to allied mercenary
     */
    public void addToMercenary(Mercenary mercenary){
        this.allies.add(mercenary);
    }

    /*
     * Get the player weapons for battle
     * - Decreases the durability of the weapon
     * - Removes the weapon if run out of durability
     */
    public List<Weapon> getPlayerWeapons() {
        boolean foundSword = false;
        boolean foundBow = false;
        boolean foundShield = false;

        List<Weapon> weaponsForBattle = new ArrayList<Weapon>();
        List<Weapon> removeWeapons = new ArrayList<Weapon>();
        for (Weapon weapon : weapons) {
            if (weapon.getType().equals("sword") && !foundSword) {
                // Decrease the durability of the weapon
                weapon.decreaseDurability();
                weaponsForBattle.add(weapon);
                foundSword = true;
                
                // Check if the weapon has run out of durability
                if (weapon.getDurability() == 0) {
                    removeWeapons.add(weapon);
                    inventory.remove(weapon);
                }
            }

            if (weapon.getType().equals("bow") && !foundBow) {
                // Decrease the durability of the weapon
                weapon.decreaseDurability();
                weaponsForBattle.add(weapon);
                foundBow = true;

                // Check if the weapon has run out of durability
                if (weapon.getDurability() == 0) {
                    removeWeapons.add(weapon);
                    inventory.remove(weapon);
                }
            }

            if (weapon.getType().equals("shield") && !foundShield) {
                // Decrease the durability of the weapon
                weapon.decreaseDurability();
                weaponsForBattle.add(weapon);
                foundShield = false;

                // Check if the weapon has run out of durability
                if (weapon.getDurability() == 0) {
                    removeWeapons.add(weapon);

                   // weapons.remove(weapon);
                    inventory.remove(weapon);
                }
            }
        }

        // Remove all weapons that have run out of durability
        weapons.removeAll(removeWeapons);
        return weaponsForBattle;
    }

    /*
     * Remove inventory items that make the shield
     */
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

    /*
     * Remove inventory items that make the bow
     */
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

    /*
     * Remove inventory items that make the sceptre
     */
    public void removeForSceptre() {
        // Remove 1 wood or 2 arrows,  a treasure or key, 1 sun stone
        int woodCount = 0;
        int treasurekeyCount = 0;
        int sunStoneCount = 0;

        Iterator<Item> inventoryIterator = inventory.iterator();
        Item item;
        while(inventoryIterator.hasNext()) {     
            item = inventoryIterator.next();     

            if (item instanceof Wood && (woodCount != 1)) {
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

            else if (item instanceof SunStone && (sunStoneCount != 1)) {
                inventoryIterator.remove();
                sunStoneCount++;
            }   
        }
        if (woodCount == 0) {
            removeForSceptreArrow();
        }
    }

    public void removeForSceptreArrow() {
        int arrowCount = 0;
        Iterator<Item> inventoryIterator = inventory.iterator();
        Item item;
        while(inventoryIterator.hasNext()) {     
            item = inventoryIterator.next(); 
            if (item instanceof Arrow && (arrowCount != 2)) {
                inventoryIterator.remove();
                arrowCount++;
            }    
        }
    }

    /*
     * Remove inventory items that make the midnight armour
     */
    public void removeForMidnightArmour() {
        // Remove 1 sword and 1 sun stone
        int swordCount = 0;
        int sunStoneCount = 0;

        Iterator<Item> inventoryIterator = inventory.iterator();
        Item item;
        while(inventoryIterator.hasNext()) {     
            item = inventoryIterator.next();     

            if (item instanceof Sword && (swordCount != 1)) {
                inventoryIterator.remove();
                swordCount++;
            }

            else if (item instanceof SunStone && (sunStoneCount != 1)) {
                inventoryIterator.remove();
                sunStoneCount++;
            }
        }
    }



    /*
     * Get the amount of treasure in the players inventory
     */
    public int treasureAmount() {
        int treasureCount = 0;
        for (Item item : inventory) {
            if (item.getType().equals("treasure")) {
                treasureCount++;
            }
        }

        return treasureCount;
    }

    /*
     * Decrease the durability of the sword
     * Remove sword from inventory if durability is zero
     */
    public void decreaseSwordDurability() {
        for (Weapon weapon : weapons) {
            if (weapon.getType().equals("sword")) {
                weapon.decreaseDurability();
                if (weapon.getDurability() == 0) {
                    inventory.remove(weapon);
                    weapons.remove(weapon);
                }
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

    /*
     * Start battles. Player look for enemies and notifies them of the battle. 
     * (Observer pattern)
     */
    public List<Battle> battle() {
        List<Battle> battles = new ArrayList<Battle>();
        if (isInvisible()) return battles;
        Iterator<MovingEntity> enemyIterator = movingEntities.iterator();
        MovingEntity enemy;
        while(enemyIterator.hasNext()) {
            enemy = enemyIterator.next();
            if (enemy.getPosition().getX() == position.getX() && enemy.getPosition().getY() == position.getY()) {

                // Check for allied mercenary
                if (enemy instanceof Mercenary) {
                    Mercenary mercenary = (Mercenary) enemy;
                    if (mercenary.isAllied()) {
                        continue;
                    }
                }

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

    /*
     * Check if key exists to unlock a door
     */
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
        Position prev = this.position;
        this.position = position;
        moveAllies(prev);
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
        movingEntities.stream().forEach(o -> o.move(position, entities));
    }

    /*
     * Update the potions that the player is using
     */
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

    /*
     * Update the allied mercenary
     */
    public void updateAlliedMercenary(){
        for(AlliedEntities m : allies) {
            int bribeTime = m.getBribeTime();
            if (bribeTime != 0) {
                m.setBribeTime(bribeTime - 1);
                if (bribeTime == 1){
                    m.setAllied(false);
                    allies.remove(m);
                }
            }
        }
    }


    /*
     * Notify the enemies of the players potion use. 
     */
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

    /*
     * Check if the player is invisible or not
     */
    public boolean isInvisible() {
        if (potionQueue.size() > 0) {
            if (potionQueue.get(0) instanceof InvisibilityPotion) {
                return true;
            }
        }
        return false;
    }

    /*
     * Check if the player in invincible or not
     */
    public boolean isInvincible() {
        if (potionQueue.size() > 0) {
            if (potionQueue.get(0) instanceof InvincibilityPotion) {
                return true;
            }
        }
        return false;
    }

    /*
     * Remove enemy from moving entities list
     */
    public void removeFromMovingEntities(String Id) {
        for (MovingEntity entity : movingEntities) {
            if (entity.getId().equals(Id)) {
                movingEntities.remove(entity);
                break;
            }
        }
    }

    /*
     * Remove treasure from inventory. 
     */
    public void removeTreasure(int count) {
        int counter = 0;
        Iterator<Item> inventoryIterator = inventory.iterator();
        Item item;
        while(inventoryIterator.hasNext() && counter < count) {     
            item = inventoryIterator.next();     

            if (item instanceof Treasure) {
                inventoryIterator.remove();
                counter++;
            }
        }
    }

    public void addAlly(MovingEntity ally) {
        allies.add((AlliedEntities)ally);
    }

    // TODO implement shortest if not close to player
    private void moveAllies(Position player) {
        for (AlliedEntities ally : allies) {
            ally.move(player, new ArrayList<Entity>());
        }
    }

    public double getPlayerAllyAttack() {
        return allies.stream().mapToInt(AlliedEntities::getAllyAttack).sum() + playerAttack;

    }

    public double getAllyDefence() {
        return allies.stream().mapToInt(AlliedEntities::getAllyDefence).sum();
    }
}
