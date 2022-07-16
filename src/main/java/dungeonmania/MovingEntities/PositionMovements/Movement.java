package dungeonmania.MovingEntities.PositionMovements;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.util.Position;

public abstract class Movement {
    
    public Position moveEnemy(Position player, List<Entity> entities) {
        return null;
    };

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
