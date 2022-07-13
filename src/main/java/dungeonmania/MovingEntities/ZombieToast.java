package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Battle;
import dungeonmania.Enemy;
import dungeonmania.Item;
import dungeonmania.Player;
import dungeonmania.Round;
import dungeonmania.Weapon;
import dungeonmania.util.Position;

public class ZombieToast implements MovingEntity, Enemy {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private double zombieAttack;
    private double zombieHealth;

    private boolean playerInvisible;
    private boolean playerInvincible;

    public ZombieToast(String id, String type, Position position, boolean isInteractable, double zombieAttack, double zombieHealth) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.zombieAttack = zombieAttack;
        this.zombieHealth = zombieHealth;

        this.playerInvisible = false;
        this.playerInvincible = false;
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

    public double getZombieAttack() {
        return zombieAttack;
    }

    public double getZombieHealth() {
        return zombieHealth;
    }

    public Battle battleCalculate(Player player) {
        double playerHealth = player.getPlayerHealth();
        double playerAttack = player.getPlayerAttack();
        double playerBow = 1;
        double playerSword = 0;
        double playerShield = 0;
        double enemyHealth = zombieHealth;
        double enemyAttack = zombieAttack;

        // Get weapons. 
        List<Weapon> weaponryUsed = player.getPlayerWeapons();

        for (Weapon weapon : weaponryUsed) {
            if (weapon.getType().equals("bow")) {
                playerBow = 2;
            }

            if (weapon.getType().equals("sword")) {
                playerSword = weapon.getAttackDamage();
            }

            if (weapon.getType().equals("shield")) {
                playerShield = weapon.getDefenceDamage();
            }
        }

        // List of items used for every round. 
        List<Item> items = new ArrayList<Item>();

        // Add all weapons being used. 
        items.addAll(weaponryUsed);

        // If a player is using a potion, add it to the list of items.
        if (playerInvisible || playerInvincible) {
            items.add(player.currentPotion());
        }

        List<Round> rounds = new ArrayList<Round>();
        
        // Check if player is invinsible
        if (playerInvincible) {
            double deltaPlayerHealth = 0;
            double deltaEnemyHealth = - this.zombieHealth;
            this.zombieHealth = 0;
            rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
        }


        else {
            while (this.zombieHealth > 0 && player.getPlayerHealth() > 0) {
                // Find change in health
                double deltaPlayerHealth = - ((enemyAttack - playerShield) / 10);
                double deltaEnemyHealth = - ((playerBow * (playerSword + playerAttack)) / 5);
    
                // Update zombie health
                this.zombieHealth = this.zombieHealth - ((playerBow * (playerSword + playerAttack)) / 5);
    
                // Update player health
                double health = player.getPlayerHealth() - ((enemyAttack - playerShield) / 10);
                player.setPlayerHealth(health);
    
                // Add round info to list
                rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
            }
        }

        Battle battle = new Battle(type, rounds, playerHealth, enemyHealth, this.id);

        // Find the winner.
        // zombie won
        if (this.zombieHealth > 0) {
            battle.setEnemyWon(true);
        }

        // Player won
        else if (player.getPlayerHealth() > 0) {
            battle.setPlayerWon(true);
        }

        return battle;
    }
}
