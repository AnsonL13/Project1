package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.enemy.Spider;
import dungeonmania.util.Direction;
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

    public void setSpawnRate (int spiderRate, int zombieRate) {
        this.spiderRate = spiderRate;
        this.zombieRate = zombieRate;
        this.nextSpiderRate = spiderRate;
        this.nextZombieRate = zombieRate;
    }
/*
    public Enemy spawn (String enemy, Position pos) {
        if (enemy == null || pos == null) return null;
        
        if (enemy.equalsIgnoreCase("Zombie")) {
            return new ZombieToast(0, zombieHealth, zombieAttack, pos);
        } else if (enemy.equalsIgnoreCase("Spider")) {
            return new Spider(spiderAttack, spiderHealth, pos);
        }
        return null;
    } */

    public List<Entity> spawn (String latestId, List<ZombieToastSpawner> zombieSpawners, List<Entity> entities) {    
        List<Entity> newEnemies = new ArrayList<Entity>();  
        --nextZombieRate;
        --nextSpiderRate;

        if (spawnZombie()) {
            nextZombieRate = zombieRate;
            for (ZombieToastSpawner spawner : zombieSpawners) {
                Position newSpawnPos = spawner.spawn(entities);
                if (newSpawnPos != null) {
                    newEnemies.add(new ZombieToast(latestId, zombieAttack, zombieHealth, newSpawnPos));
                }
                latestId = getNewId(latestId);
            }
        } 
        
        if (spawnSpider()) {
            nextSpiderRate = spiderRate;
            Position pos = null;
            pos = ifSpiderSpawn();
            newEnemies.add(new Spider(latestId, pos, spiderAttack, spiderHealth));
        }
        return newEnemies;
    }

    

    // cannot moving upwards immediately because of the exist of boulder ???
    /*
    private boolean canSpawn(Position position, Boulder boulder) {
        Position spawnerPosition = super.getPosition();
        if (spawnerPosition.equals(boulder.getPosition())) {
            return false;
        }
        return true;
    }*/


    private Position ifSpiderSpawn() {
        Random random = new Random();
        int i = 15;
        int randomY = random.nextInt(i);
        int randomX = random.nextInt(i);
       /*  
        List<Position> spawnPositions = new ArrayList<Position>();
        Position spawner = super.getPosition();
        switch(randomPos) {
            case 0:
                spawnPositions.add(spawner.translateBy(Direction.UP));
                break;
            case 1:
                spawnPositions.add(spawner.translateBy(Direction.DOWN));
                break;
            case 2:
                spawnPositions.add(spawner.translateBy(Direction.LEFT));
                break;
            case 3:
                spawnPositions.add(spawner.translateBy(Direction.RIGHT));
                break;
        }
         */
        return new Position(randomX, randomY);
    }

    private boolean spawnZombie() {
        if (zombieRate > 0 && nextZombieRate == 0) return true;
        return false;
    }

    private boolean spawnSpider() {
        if (spiderRate > 0 && nextSpiderRate == 0) return true;
        return false;
    }

    private String getNewId(String latestId) {
        int increment = Integer.parseInt(latestId);
        increment++;
        return String.valueOf(increment);
    }

    

}