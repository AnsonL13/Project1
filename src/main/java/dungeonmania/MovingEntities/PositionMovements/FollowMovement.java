package dungeonmania.MovingEntities.PositionMovements;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FollowMovement extends Movement {
    private Mercenary enemy;

    public FollowMovement (Mercenary enemy) {
        this.enemy = enemy;
    }

    @Override
    public Position moveEnemy(Position player, List<Entity> entities) {
        Position leftMove = enemy.getPosition().translateBy(Direction.LEFT);
        Position rightMove = enemy.getPosition().translateBy(Direction.RIGHT);
        Position upMove = enemy.getPosition().translateBy(Direction.UP);
        Position downMove = enemy.getPosition().translateBy(Direction.DOWN);

        if ((player.equals(leftMove) || player.equals(rightMove) || player.equals(upMove) || player.equals(downMove)) 
                && ! enemy.isAllied()) return player;

        Position leftVector = Position.calculatePositionBetween(leftMove, player);
        Position rightVector = Position.calculatePositionBetween(rightMove, player);
        Position upVector = Position.calculatePositionBetween(upMove, player);
        Position downVector = Position.calculatePositionBetween(downMove, player);

        // add all posible position to map and their distance lenght
        Map<Position, Double> mapShortest = new HashMap<Position, Double>();
        mapShortest.put(leftMove, calculateLength(leftVector));
        mapShortest.put(rightMove, calculateLength(rightVector));
        mapShortest.put(upMove, calculateLength(upVector));
        mapShortest.put(downMove, calculateLength(downVector));

        // sort map
        LinkedHashMap<Position, Double> sortedMap = new LinkedHashMap<>();
        mapShortest.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .forEachOrdered(o -> sortedMap.put(o.getKey(), o.getValue()));
        Position smallest = sortedMap.keySet().stream().findFirst().get();

        // sort map by double value and return one with smallest 
        // distance and not blocked
        if (canMove(smallest, entities)) {
            return smallest;
        } else if (canMove(getNextPos(1, sortedMap), entities)) {
            return sortedMap.keySet().stream().skip(1).findFirst().get();
        } else if (canMove(getNextPos(2, sortedMap), entities)) {
           return sortedMap.keySet().stream().skip(2).findFirst().get();
        } else if (canMove(getNextPos(3, sortedMap), entities)) {
           return sortedMap.keySet().stream().skip(3).findFirst().get();

        }
        return null;
    }
    
    private Double calculateLength(Position vector) {
        int x = Math.abs(vector.getX());
        int y = Math.abs(vector.getY());
        double squareX = Math.pow(x, x);
        double squareY = Math.pow(y, y);
        double addXY = squareX + squareY;

        return Math.sqrt(addXY);
    }

    private Position getNextPos(int i, LinkedHashMap<Position, Double> sortedMap) {
        return sortedMap.keySet().stream().skip(i).findFirst().get();
    }
}
