package dungeonmania;

import java.util.ArrayList;
import java.util.List;

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
}
