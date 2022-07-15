package dungeonmania.MovingEntities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.Battle;
import dungeonmania.Enemy;
import dungeonmania.Entity;
import dungeonmania.Item;
import dungeonmania.Player;
import dungeonmania.Round;
import dungeonmania.Weapon;
import dungeonmania.BuildableEntities.Bow;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.util.Position;

public class MovingEntity implements Entity, Enemy {
    private int isInvicible;
    private int isInvisible;
    private double health;
    private int attack;
    private String id;
    private Position position;
    private boolean inBattle;
    private String type;

    public MovingEntity (String id, int health, int attack, Position position) { 
        this.health = (double) health;
        this.attack = attack;
        this.position = position;
        this.id = id;
    }

    /** 
     * @param player
     * @param entities
     * @return boolean
     */
    public boolean move (Position player, List<Entity> entities) {
        return isBattle(player);
    }

    /** 
     * @param player
     * @return boolean
     */
    public boolean isBattle(Position player) {
        if (isInvisible != 0) return false;
        if (player.equals(position)) return true;
        return false;
    }
    
    /** 
     * @return double
     */
    public double getHealth() {
        return health;
    }
    
    /** 
     * @return int
     */
    public int getAttack() {
        return attack;
    }

    /** 
     * @param attack
     */
    public void setAttack(int attack) {
        this.attack = attack;
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
     * @return String
     */
    public String getSimpleName() {
        return null;
    }

    /** 
     * @return boolean
     */
    @Override
    public boolean isInteractable() {
        return false;
    }

    /** 
     * @return String
     */
    @Override
    public String getId() {
        return id;
    }
    
    /** 
     * @return String
     */
    @Override
    public String getType() {
        return type;
    }
    
    /** 
     * @return Position
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /** 
     * @param duration
     */
    public void setInvisible(int duration) {
        this.isInvisible = duration;
    }

    /** 
     * @param duration
     */
    public void setInvincible(int duration) {
        this.isInvicible = duration;
    }
    
    /** 
     * @return boolean
     */
    public boolean isInvicible() {
        if (isInvicible > 0) return true;
        return false;    
    }

    
    /** 
     * @return boolean
     */
    public boolean isInvisible() {
        if (isInvisible > 0) return true;
        return false;    
    }

    public void setPotions() {
        if (isInvicible > 0) {
            isInvicible--;
        } else if (isInvisible > 0) {
            isInvisible--;
        }
    }
    
    /** 
     * @param position
     */
    @Override
    public void setPosition(Position position) {
        this.position = position;        
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
        if (isInvicible() || isInvisible()) {
            items.add(player.currentPotion());
        }

        List<Round> rounds = new ArrayList<Round>();
        
        // Check if player is invincible
        if (isInvicible()) {
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
                double deltaEnemyHealth = - ((playerBow * (playerSword + playerAttack)) / 5);
    
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
