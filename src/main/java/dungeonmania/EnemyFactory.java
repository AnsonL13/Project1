package dungeonmania;

import dungeonmania.enemy.Enemy;
import dungeonmania.util.Position;

public class EnemyFactory {
    private int enemyHealth;
    private int enemyAttack;
    private int spawnRate;
    private int nextSpawn;
    private Position position;

    public EnemyFactory (int attack, int health, int spawnRate, Position position) {
        this.enemyAttack = attack;
        this.enemyHealth = health;
        this.spawnRate = spawnRate;
        this.nextSpawn = spawnRate;
    }

    public Enemy spawn () {
        return null;
    }

    public int getEnemyHealth() {
        return enemyHealth;
    }

    public int getEnemyAttack() {
        return enemyAttack;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public int getNextSpawn() {
        return nextSpawn;
    }

    public void setNextSpawn() {
        this.nextSpawn = spawnRate;
    }

    public void minusNextSpawn() {
        --this.nextSpawn;
    }

    public Position getPosition() {
        return position;
    }

    public boolean canSpawn(Position pos) {
        return true;
    }

}
