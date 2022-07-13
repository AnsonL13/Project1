package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Battle;
import dungeonmania.Item;
import dungeonmania.Player;
import dungeonmania.Round;
import dungeonmania.Weapon;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    private String type;
    private boolean isInteractable;
    private boolean playerInvisible;
    private boolean playerInvincible;

    public Spider(String id, String type, Position position, boolean isInteractable, int attack, int health) {
        super(id, health, attack, position);
        this.type = type;
        this.isInteractable = isInteractable;
        this.playerInvisible = false;
        this.playerInvincible = false;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getType() {
        return type;
    }

    public Battle battleCalculate(Player player) {
        double playerHealth = player.getPlayerHealth();
        double playerAttack = player.getPlayerAttack();
        double playerBow = 1;
        double playerSword = 0;
        double playerShield = 0;
        double enemyHealth = super.getHealth();
        double enemyAttack = super.getAttack();

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
            double deltaEnemyHealth = - super.getHealth();
            super.setHealth(0);
            rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
        }


        else {
            while (super.getHealth() > 0 && player.getPlayerHealth() > 0) {
                // Find change in health
                double deltaPlayerHealth = - ((enemyAttack - playerShield) / 10);
                double deltaEnemyHealth = - ((playerBow * (playerSword + playerAttack)) / 5);
    
                // Update spider health
                super.setHealth(  super.getHealth() -  ((playerBow * (playerSword + playerAttack)) / 5));
    
                // Update player health
                double health = player.getPlayerHealth() - ((enemyAttack - playerShield) / 10);
                player.setPlayerHealth(health);
    
                // Add round info to list
                rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
            }
        }

        Battle battle = new Battle(type, rounds, playerHealth, enemyHealth, super.getId());

        // Find the winner.
        // Spider won
        if (super.getHealth() > 0) {
            battle.setEnemyWon(true);
        }

        // Player won
        else if (player.getPlayerHealth() > 0) {
            battle.setPlayerWon(true);
        }

        return battle;
    }
}
