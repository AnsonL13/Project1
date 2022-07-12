package dungeonmania;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.enemy.Enemy;
import dungeonmania.enemy.Spider;
import dungeonmania.enemy.ZombieToast;
import dungeonmania.util.Position;

public class EnemyFactory {
    private int zombieAttack;
    private int zombieHealth;
    private int spiderAttack;
    private int spiderHealth;
    private int zombieRate;
    private int spiderRate;
    private int nextSpiderRate;
    private int nextZombieRate;

    public EnemyFactory (int zombieAttack, int zombieHealth, int spiderAttack, int spiderHealth) {
        this.zombieAttack = zombieAttack;
        this.zombieHealth = zombieHealth;
        this.spiderAttack = spiderAttack;
        this.spiderHealth = spiderHealth;
    }

    public Enemy spawn (String enemy, Position pos) {
        if (enemy == null || pos == null) return null;
        
        if (enemy.equalsIgnoreCase("Zombie")) {
            return new ZombieToast(zombieHealth, zombieAttack, pos);
        } else if (enemy.equalsIgnoreCase("Spider")) {
            return new Spider(spiderAttack, spiderHealth, pos);
        }
        return null;
    } 

    public List<Enemy> spawnTwo () {    
        List<Enemy> newEnemies = new ArrayList<Enemy>();  
        --nextZombieRate;
        --nextSpiderRate;

        if (nextZombieRate == 0) {
            nextZombieRate = zombieRate;
            //for (ZombieToastSpawner spawner : zombieSpawners) {
            //      newEnemies.add(new Zombie(zombieAttack, zombieHealth, pos));
            //}
        } 
        if (nextSpiderRate == 0) {
            nextSpiderRate = spiderRate;
            Position pos = null;
            //pos = spiderSpawn();
            newEnemies.add(new Spider(spiderAttack, spiderHealth, pos));
        }
        return newEnemies;
    }


}
