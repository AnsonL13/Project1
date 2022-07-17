package dungeonmania.MovingEntities.PositionMovements;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RunAwayMovement extends Movement {
    private MovingEntity enemy;

    public RunAwayMovement (MovingEntity enemy) {
        this.enemy = enemy;
    }

    @Override
    public Position moveEnemy(Position player, List<Entity> entities) {
        Position pos = enemy.getPosition();
        if (player.getX() > enemy.getPosition().getX()) {
            pos = pos.translateBy(Direction.LEFT);
        } else {
            pos = pos.translateBy(Direction.RIGHT);
        }
        /*     if (canMove(pos)) {
            super.setPos(pos);
            return;
        } else if (player.getY() > super.getPosition().getY()) {
            pos.translateBy(Direction.UP);
        } else {
            pos.translateBy(Direction.DOWN);
        }

        if (canMove(pos)) {
            super.setPos(pos);
        }*/
        if (canMove(pos, entities)) {
            return pos;
        }
        return null;    
    }    
}
