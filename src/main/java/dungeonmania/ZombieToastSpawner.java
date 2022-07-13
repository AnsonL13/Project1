package dungeonmania;

import java.util.List;

import dungeonmania.enemy.Enemy;
import dungeonmania.enemy.ZombieToast;
import dungeonmania.util.Position;

public class ZombieToastSpawner {
    private Position pos;

    public ZombieToastSpawner(int spawnRate, Position position) {
        this.pos = position;
    }

    public Position spawn(List<Entity> entities) { 
        List<Position> adj = pos.getAdjacentPositions();
        for (Position pos : adj) {
            if (canSpawn(pos, entities)) {
                return pos;
            }
        }  
        return null;
    }

    public Position getPosition() {
        return pos;
    }

    public boolean canSpawn(Position pos, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity instanceof Enemy && entity.getPosition().equals(pos)) {
                return false;
            } else if (entity instanceof Enemy && entity.getPosition().equals(pos)) { //wall or door, boulder
                return false;
            }
        }
        return true;
    }
    
}
