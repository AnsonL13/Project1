package dungeonmania.MovingEntities.PositionMovements;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.AlliedEntities;
import dungeonmania.util.Direction;
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
