package dungeonmania.MovingEntities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.InteractableEntity;
import dungeonmania.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class Mercenary extends MovingEntity implements InteractableEntity {
    private String type;
    private boolean isInteractable;
    private boolean isAllied;

    private int allyAttack;
    private int allyDefence;
    private int bribeAmount;
    private int bribeRadius;

//    private MovingPatterns;
//    private MovingPatterns = new RunAwayMovement;
//    private MovingPatterns = new RandomMovement;
//    private MovingPatterns = new FollowMovement;
//    private MovingPatterns = new AlliedMovement;

    public Mercenary(String id, String type, Position position, boolean isInteractable, 
        int allyAttack, int allyDefence,  int bribeAmount, int bribeRadius, int mercenaryAttack, int mercenaryHealth) {
        super(id, mercenaryAttack, mercenaryHealth, position);
        this.type = type;
        this.isInteractable = isInteractable;

        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        
    }

    public boolean move(Position player) {
        Position leftMove = super.getPosition().translateBy(Direction.LEFT);
        Position rightMove = super.getPosition().translateBy(Direction.RIGHT);
        Position upMove = super.getPosition().translateBy(Direction.UP);
        Position downMove = super.getPosition().translateBy(Direction.DOWN);

        //if any pos = player pos return and set battle

        Position leftVector = Position.calculatePositionBetween(leftMove, player);
        Position rightVector = Position.calculatePositionBetween(rightMove, player);
        Position upVector = Position.calculatePositionBetween(upMove, player);
        Position downVector = Position.calculatePositionBetween(downMove, player);

        List<Double> distance = new ArrayList<>();
        distance.add(calculateLenth(leftVector));
        distance.add(calculateLenth(rightVector));
        distance.add(calculateLenth(upVector));
        distance.add(calculateLenth(downVector));
        int minIndex = distance.indexOf(Collections.min(distance));
        
        Map<Position, Double> mapShortest = new HashMap<Position, Double>();
        LinkedHashMap<Position, Double> sortedMap = new LinkedHashMap<>();

     //   mapShortest = mapShortest.entrySet()
       //     .stream()
        //    .sorted(Entry.comparingByValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (o, c) -> o, LinkedHashMap::new));
        mapShortest.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .forEachOrdered(o -> sortedMap.put(o.getKey(), o.getValue()));
        Position smallest = sortedMap.keySet().stream().findFirst().get();
        //sort map by double value

        if (canMove(smallest)) {

        } /*else if (canMove(null)) {

        } else if (canMove(null)) {

        } else if (canMove(null)) {

        } */

        return false;
    }
  
    private Double calculateLenth(Position vector) {
        double squareX = Math.pow(vector.getX(), vector.getX());
        double squareY = Math.pow(vector.getY(), vector.getY());
        double addXY = squareX + squareY;

        return Math.sqrt(addXY);
    }

    public String getSimpleName() {
        return "mercenary";
    }

    private boolean canMove(Position pos) {
        return false;
    }


    public int getAllyAttack() {
        return allyAttack;
    }

    public int getAllyDefence() {
        return allyDefence;
    }

    public boolean isInteractable() {
        return isInteractable;
    }


    public final String getType() {
        return type;
    }


    public int getBribeAmount() {
        return bribeAmount;
    }

    public int getBribeRadius() {
        return bribeRadius;
    }

    public boolean interactActionCheck(Player player) {
        int xTopBoundary = super.getPosition().getX() + bribeRadius;
        int xBottomBoundary = super.getPosition().getX() - bribeRadius;
        int yTopBoundary = super.getPosition().getY() + bribeRadius;
        int yBottomBoundary = super.getPosition().getY() - bribeRadius;
        // Check if player is within the specified bribing radius
        if ((player.getPosition().getX() >= xBottomBoundary && player.getPosition().getX() <= xTopBoundary) &&
            (player.getPosition().getY() >= yBottomBoundary && player.getPosition().getY() <= yTopBoundary)) {
                return true;
        }

        // Check if player has enough gold
        if (player.treasureAmount() < bribeAmount) {
            return false;
        }

        return true;
    }
}

