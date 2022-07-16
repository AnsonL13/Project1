package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.StaticEntities.ZombieToastSpawner;
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
     * Gets all the instances of ZombieToastSpawners
     */
    private List<ZombieToastSpawner> getSpawner(List<Entity> entities) {
        List<ZombieToastSpawner> allSpawners = entities.stream()
            .filter( o -> o instanceof ZombieToastSpawner)
            .map(ZombieToastSpawner.class::cast)
            .collect(Collectors.toList());
        return allSpawners;
    }

    /*
     * Spawn enemies
     */
    public List<MovingEntity> spawn(String latestId, List<Entity> entities) {
        List<ZombieToastSpawner> zombieSpawners = getSpawner(entities);
        List<MovingEntity> newEnemies = new ArrayList<MovingEntity>();  
        --nextZombieRate;
        --nextSpiderRate;

        // Check if it is time to spawn zombies.
        if (spawnZombie()) {
            nextZombieRate = zombieRate;
            // Spawn a zombie 
            for (ZombieToastSpawner spawner : zombieSpawners) {
                Position newSpawnPos = spawner.spawn(entities);
                if (newSpawnPos != null) {
                    newEnemies.add(new ZombieToast(latestId, zombieAttack, zombieHealth, newSpawnPos));
                    latestId = getNewId(latestId);
                }
            }
        }
        
        // Check if it is time to spawn the spider. 
        if (spawnSpider()) {
            nextSpiderRate = spiderRate;
            Position pos = ifSpiderSpawn();
            while(! canSpiderSpawn(pos, entities)) {
                pos = ifSpiderSpawn();
            }
            newEnemies.add(new Spider(latestId, pos, spiderAttack, spiderHealth));
        }
        return newEnemies;
    }

    /*
     * Check if position on boulder. Cannot spawn on boulder.
     * 
     */
    private boolean canSpiderSpawn(Position position, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getType().equals("boulder") && entity.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }


    private Position ifSpiderSpawn() {
        Random random = new Random();
        int i = 10;
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