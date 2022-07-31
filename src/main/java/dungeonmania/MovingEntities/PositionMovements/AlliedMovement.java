package dungeonmania.MovingEntities.PositionMovements;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class AlliedMovement extends Movement {
    public AlliedMovement () {}

    /*
     * Goes to player
     */
    @Override
    public Position moveEnemy(Position player, List<Entity> entities) {
        return player;
    }

}
