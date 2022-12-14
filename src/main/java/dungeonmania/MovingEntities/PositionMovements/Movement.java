package dungeonmania.MovingEntities.PositionMovements;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.util.Position;

import java.io.Serializable;

public abstract class Movement implements Serializable {
    
    public Position moveEnemy(Position player, List<Entity> entities) {
        return null;
    };
    
    // Checks if the enemy can move, or is blocked. 
    public boolean canMove(Position position, List<Entity> entities) {
        if (position == null) return false;
        for (Entity entity : entities) {
            if (entity instanceof Boulder && entity.getPosition().equals(position)) {
                return false;
            } else if (entity instanceof Wall && entity.getPosition().equals(position)) {
                return false;
            } else if (entity instanceof Door && entity.getPosition().equals(position)) {
                return false;
            } 
        }
        return true;
    }
}
