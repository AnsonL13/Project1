package dungeonmania.MovingEntities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.Battle;
import dungeonmania.Entity;
import dungeonmania.Item;
import dungeonmania.Player;
import dungeonmania.Round;
import dungeonmania.Weapon;
import dungeonmania.BuildableEntities.Bow;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.util.Position;

public class MovingEntity implements Entity {
    protected boolean isInvincible;
    protected boolean isInvisible;
    protected double health;
    protected double attack;
    protected String id;
    protected Position position;
    protected boolean inBattle;
    protected String type;

    public MovingEntity (String id, double attack, double health, Position position) { 
        this.health = health;
        this.attack = attack;
        this.position = position;
        this.id = id;
    }
    
    /** 
     * @return double
     */
    public double getHealth() {
        return health;
    }
    
    /** 
     * @return double
     */
    public double getAttack() {
        return attack;
    }
    
    /** 
     * @param health
     */
    public void setHealth(double health) {
        this.health = health;
    }
    
    /** 
     * @return boolean
     */
    public boolean isInBattle() {
        return inBattle;
    }
    
    /** 
     * @param inBattle
     */
    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    /** 
     * @return boolean
     */
    public boolean isInteractable() {
        return false;
    }

    /** 
     * @return String
     */
    public String getId() {
        return id;
    }
    
    /** 
     * @return String
     */
    public String getType() {
        return type;
    }
    
    /** 
     * @return Position
     */
    public Position getPosition() {
        return position;
    }

    /** 
     * @return isInvisible
     */
    public boolean isInvisible() {
        return this.isInvisible;
    }

    /** 
     * @return isInvincible
     */
    public boolean isInvincible() {
        return this.isInvincible;
    }

    public void setPotionStatus(boolean setInvisible, boolean setInvincible) {
        this.isInvisible = setInvisible;
        this.isInvincible = setInvincible;
    }

    public void setPosition(Position position) {
        this.position = position;        
    }

    public void move(Position playerPos, List<Entity> entities) {
        return;
    }

    /** 
     * @param player
     * @return Battle
     */
    public Battle battleCalculate(Player player) {
        double playerHealth = player.getPlayerHealth();
        double playerAttack = player.getPlayerAttack();
        double playerBow = 1;
        double playerSword = 0;
        double playerShield = 0;
        double enemyHealth = getHealth();
        double enemyAttack = getAttack();

        // Get weapons. 
        List<Weapon> weaponryUsed = player.getPlayerWeapons();

        for (Weapon weapon : weaponryUsed) {
            if (weapon instanceof Bow) {
                playerBow = 2;
            } else if (weapon instanceof Sword) {
                playerSword = weapon.getAttackDamage();
            } else if (weapon.getType().equals("shield")) {
                playerShield = weapon.getDefenceDamage();
                if (playerShield > enemyAttack) playerShield = enemyAttack;
            }
        }

        // List of items used for every round. 
        List<Item> items = new ArrayList<Item>();

        // Add all weapons being used. 
        items.addAll(weaponryUsed);

        // If a player is using a potion, add it to the list of items.
        if (isInvincible || isInvisible) {
            items.add(player.currentPotion());
        }

        List<Round> rounds = new ArrayList<Round>();
        
        // Check if player is invincible
        if (isInvincible) {
            double deltaPlayerHealth = 0;
            double deltaEnemyHealth = - getHealth();
            setHealth(0);
            rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
        }

        else {
            playerAttack = playerAttack + playerSword;
            playerAttack *= playerBow;
            while (getHealth() > 0.0 && player.getPlayerHealth() > 0.0) {
                // Find change in health
                double deltaPlayerHealth = - ((enemyAttack - playerShield) / 10);
                double deltaEnemyHealth = - (playerAttack / 5);
    
                // Update zombie health
                BigDecimal c = BigDecimal.valueOf(getHealth()).subtract(BigDecimal.valueOf(playerAttack / 5));
                setHealth(c.doubleValue());

                // Update player health
                c = BigDecimal.valueOf(player.getPlayerHealth()).subtract(BigDecimal.valueOf((enemyAttack - playerShield) / 10));
                player.setPlayerHealth(c.doubleValue());
                
                // Add round info to list
                rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
            }
        }

        Battle battle = new Battle(type, rounds, playerHealth, enemyHealth, getId());
        // Find the winner.
        if (getHealth() > 0) {
            battle.setEnemyWon(true);
        } else if (player.getPlayerHealth() > 0) {
            battle.setPlayerWon(true);
        }

        return battle;
    }
}
