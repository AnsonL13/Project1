package dungeonmania;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.BuildableEntities.Bow;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.MovingEntities.MovingEntity;

public class Battle {
    private String enemy;
    private double initialPlayerHealth;
    private double initialEnemyHealth;
    private List<Round> rounds;
    private boolean playerWon = false;
    private boolean enemyWon = false;
    private String enemyId;
    
    public Battle() {
        this.initialPlayerHealth = 0;
        this.initialEnemyHealth = 0;
        this.enemy = "";
        this.rounds = new ArrayList<Round>();
    }

    public Battle(String enemy, List<Round> rounds, double initialPlayerHealth, double initialEnemyHealth, String enemyId) {
        this.initialPlayerHealth = initialPlayerHealth;
        this.initialEnemyHealth = initialEnemyHealth;
        this.enemy = enemy;
        this.rounds = rounds;
        this.enemyId = enemyId;
    }

    public final String getEnemy(){
        return enemy;
    }

    public final double getInitialPlayerHealth(){
        return initialPlayerHealth;
    }

    public final double getInitialEnemyHealth(){
        return initialEnemyHealth;
    }

    public final List<Round> getRounds(){
        return rounds;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(boolean playerWon) {
        this.playerWon = playerWon;
    }

    public boolean isEnemyWon() {
        return enemyWon;
    }

    public void setEnemyWon(boolean enemyWon) {
        this.enemyWon = enemyWon;
    }

    public String getEnemyId() {
        return enemyId;
    }

        /** 
     * @param player
     * @return Battle
     */
    public static Battle battleCalculate(Player player, MovingEntity enemy) {
        double playerHealth = player.getPlayerHealth();
        double playerAttack = player.getPlayerAllyAttack();
        double playerBow = 1;
        double playerSword = 0;
        double playerShield = 0;
        double enemyHealth = enemy.getHealth();
        double enemyAttack = enemy.getAttack();

        // Get weapons. 
        List<Weapon> weaponryUsed = player.getPlayerWeapons();

        for (Weapon weapon : weaponryUsed) {
            if (weapon instanceof Bow) {
                playerBow = 2;
            } else if (weapon instanceof Sword) {
                playerSword = weapon.getAttackDamage();
            } else if (weapon.getType().equals("shield")) {
                playerShield = weapon.getDefenceDamage();
            }
        }

        // List of items used for every round. 
        List<Item> items = new ArrayList<Item>();

        // Add all weapons being used. 
        items.addAll(weaponryUsed);

        // If a player is using a potion, add it to the list of items.
        if (player.isInvincible() || player.isInvisible()) {
            items.add(player.currentPotion());
        }

        List<Round> rounds = new ArrayList<Round>();
        
        // Check if player is invincible
        if (enemy.isInvincible()) {
            double deltaPlayerHealth = 0;
            double deltaEnemyHealth = - enemy.getHealth();
            enemy.setHealth(0);
            rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
        }

        else {
            playerAttack = playerAttack + playerSword;
            playerAttack *= playerBow;
            // when defence is bigger than enemy attack
            double playerDefence = playerShield + player.getAllyDefence();
            if (playerDefence > enemyAttack) playerShield = enemyAttack;
            // calculate rounds
            while (enemy.getHealth() > 0.0 && player.getPlayerHealth() > 0.0) {
                // Find change in health
                double deltaPlayerHealth = - ((enemyAttack - playerDefence) / 10);
                double deltaEnemyHealth = - (playerAttack / 5);
    
                // Update zombie health
                BigDecimal c = BigDecimal.valueOf(enemy.getHealth()).subtract(BigDecimal.valueOf(playerAttack / 5));
                enemy.setHealth(c.doubleValue());

                // Update player health
                c = BigDecimal.valueOf(player.getPlayerHealth()).subtract(BigDecimal.valueOf((enemyAttack - playerDefence) / 10));
                player.setPlayerHealth(c.doubleValue());
                
                // Add round info to list
                rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
            }
        }

        Battle battle = new Battle(enemy.getType(), rounds, playerHealth, enemyHealth, enemy.getId());
        // Find the winner.
        if (enemy.getHealth() > 0) {
            battle.setEnemyWon(true);
        } else if (player.getPlayerHealth() > 0) {
            battle.setPlayerWon(true);
        }

        return battle;
    }
}
