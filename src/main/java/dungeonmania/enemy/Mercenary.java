package dungeonmania.enemy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Mercenary extends Enemy {
//    private MovingPatterns;
//    private MovingPatterns = new RunAwayMovement;
//    private MovingPatterns = new RandomMovement;
//    private MovingPatterns = new FollowMovement;
//    private MovingPatterns = new AlliedMovement;

    private boolean isAllied;


    public Mercenary (int id, int health, int attack, Position position) {
        super(id, health, attack, position);
    }

    public boolean move(boolean isInvicible, boolean isInvisible, Position player) {
      /*  Position leftMove = super.getPosition().translateBy(Direction.LEFT);
        Position rightMove = super.getPosition().translateBy(Direction.RIGHT);
        Position upMove = super.getPosition().translateBy(Direction.UP);
        Position downMove = super.getPosition().translateBy(Direction.DOWN);

        //if any pos = player pos return and set battle

        Position leftVector = Position.calculatePositionBetween(leftMove, player);
        Position rightVector = Position.calculatePositionBetween(rightMove, player);
        Position upVector = Position.calculatePositionBetween(upMove, player);
        Position downVector = Position.calculatePositionBetween(downMove, player);

        List<Double> distance = new ArrayList<>();
        Map<Position, Double> mapShortest = null;
        distance.add(calculateLenth(leftVector));
        distance.add(calculateLenth(rightVector));
        distance.add(calculateLenth(upVector));
        distance.add(calculateLenth(downVector));
        int minIndex = distance.indexOf(Collections.min(distance));
        //sort map by double value

        if (canMove(leftMove)) {

        } else if (canMove(null)) {

        } else if (canMove(null)) {

        } else if (canMove(null)) {

        }*/

        return false;
    }
/*
    private Double calculateLenth(Position vector) {
        double squareX = Math.pow(vector.getX(), vector.getX());
        double squareY = Math.pow(vector.getY(), vector.getY());
        double addXY = squareX + squareY;

        return Math.sqrt(addXY);
    }*/

    public String getSimpleName() {
        return "mercenary";
    }

    private boolean canMove(Position pos) {
        return false;
    }
    
}