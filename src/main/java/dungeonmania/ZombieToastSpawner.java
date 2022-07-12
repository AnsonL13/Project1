package dungeonmania;

import java.util.List;

import dungeonmania.enemy.Enemy;
import dungeonmania.enemy.ZombieToast;
import dungeonmania.util.Position;

public class ZombieToastSpawner {
    private int spawnRate;
    private Position pos;
    private int nextSpawn;

    public ZombieToastSpawner(int spawnRate, Position position) {
        this.spawnRate = spawnRate;
        this.pos = position;
        this.nextSpawn = spawnRate;
    }

    public Position spawn() {
        if (getNextSpawn() == 1 && getSpawnRate() != 0) {
            setNextSpawn();
            List<Position> adj = pos.getAdjacentPositions();

            for (Position pos : adj) {
                if (canSpawn(pos)) {
                    return pos;
                }
            }
        } else if (getSpawnRate() != 0) {
            minusNextSpawn();
        }
        return null;
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
        return pos;
    }

    public boolean canSpawn(Position pos) {
        return true;
    }
    
}
