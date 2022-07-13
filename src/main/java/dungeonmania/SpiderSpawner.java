package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.enemy.Enemy;
import dungeonmania.enemy.Spider;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SpiderSpawner extends EnemyFactory {
    public SpiderSpawner(int attack, int health, int spawnRate, Position position) {
        super(attack, health, spawnRate, position);
    }
    /* 
    cannot moving upwards immediately because of the exist of boulder ???
    private boolean isSpawnSuccess(Position position, Boulder boulder) {
        Position spawnerPosition = super.getPosition();
        Position upPos = spawnerPosition.translateBy(Direction.UP);
        if (upPos.equals(boulder.getPosition())) {
            return false;
        }
        return true;
    }
    */
    // the spider do not spawn on the boulder
    private boolean canSpawn(Position position, Boulder boulder) {
        Position spawnerPosition = super.getPosition();
        if (spawnerPosition.equals(boulder.getPosition())) {
            return false;
        }
        return true;
    }

    @Override
    public Enemy spawn() {
        Random random = new Random();
        int i = 4;
        int randomPos = random.nextInt(i);
        if (super.getNextSpawn() == 1 && super.getSpawnRate() != 0) {
            super.setNextSpawn();
            
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

            for (Position position : spawnPositions) {
                if (super.canSpawn(position)) {
                    return new Spider(super.getEnemyHealth(), super.getEnemyAttack(), position);
                }
            }
            
        }
        
        if (super.getSpawnRate() != 0) {
            super.minusNextSpawn();
        }
        return null;
    }
}

