package dungeonmania.MovingEntities.PositionMovements;

import java.util.List;
import java.util.Random;

import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Entity;


public class RandomMovement extends Movement{
    private MovingEntity enemy;

    public RandomMovement (MovingEntity enemy) {
        this.enemy = enemy;
    }

    @Override
    public Position moveEnemy(Position player, List<Entity> entities) {
        Random rand = new Random(); //instance of random class
        int upper = 4;
        int randomdir = rand.nextInt(upper); 
        Position pos = enemy.getPosition();
        switch(randomdir) {
            case 0:
                pos = pos.translateBy(Direction.UP);
                break;
            case 1:
                pos = pos.translateBy(Direction.LEFT);
                break;
            case 2:
                pos = pos.translateBy(Direction.DOWN);
                break;
            case 3:
                pos = pos.translateBy(Direction.RIGHT);
                break;

        }

        if (canMove(pos, entities)) {
            return pos;
        }
        return null; 
    }
}
