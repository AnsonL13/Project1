package dungeonmania.MovingEntities;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    private boolean isClockwise;
    private String type;
    private boolean isInteractable;
    private boolean movedUp;

    public Spider(String id, String type, Position position, boolean isInteractable, int attack, int health) {
        super(id, health, attack, position);
        this.type = type;
        this.isInteractable = isInteractable;
        isClockwise = true;
    }

    public Spider(String id, Position position, int attack, int health) {
        super(id, health, attack, position);
        this.type = "spider";
        this.isInteractable = false;
        isClockwise = true;
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    private void moveUpwards(Position position, List<Entity> entities) {
        position = super.getPosition();
        position = position.translateBy(Direction.UP);
        if (canMove(position, entities)) {
            super.setPosition(position);
        }
    }

    @Override
    public boolean move(Position player, List<Entity> entities) {
        // When the spider spawns, they immediately move the 1 square upwards
        // Cannot tranverse boulders, reverse direction
        // Begin 'circling' their spawn spot 
        Position position = null;
        ArrayList<Position> coordinates = getCoordinates();

        if (movedUp == false) {
            movedUp = true;
            moveUpwards(position, entities);
        } else if (isClockwise = true) {
            circling(coordinates, position, entities);
        } else { 
            reverseDirection(coordinates, position, entities);
        }
        
        return false;
    }

    private ArrayList<Position> getCoordinates () {
        ArrayList<Position> coordinates = new ArrayList<Position>();
        Position position = super.getPosition();
        /* 
        coordinates.add(position.translateBy(Direction.RIGHT));
        coordinates.add(position.translateBy(Direction.RIGHT));
        coordinates.add(position.translateBy(Direction.DOWN));
        coordinates.add(position.translateBy(Direction.DOWN));
        coordinates.add(position.translateBy(Direction.LEFT));
        coordinates.add(position.translateBy(Direction.LEFT));
        coordinates.add(position.translateBy(Direction.UP));
        coordinates.add(position.translateBy(Direction.UP));
        */
        /* 
        coordinates.add(position.translateBy(Direction.DOWN));
        coordinates.add(position.translateBy(Direction.DOWN));
        coordinates.add(position.translateBy(Direction.RIGHT));
        coordinates.add(position.translateBy(Direction.RIGHT));
        coordinates.add(position.translateBy(Direction.UP));
        coordinates.add(position.translateBy(Direction.UP));
        coordinates.add(position.translateBy(Direction.LEFT));
        coordinates.add(position.translateBy(Direction.LEFT));
        */
        coordinates.add(position.translateBy(Direction.RIGHT));
        coordinates.add(position.translateBy(Direction.DOWN));
        coordinates.add(position.translateBy(Direction.DOWN));
        coordinates.add(position.translateBy(Direction.LEFT));
        coordinates.add(position.translateBy(Direction.LEFT));
        coordinates.add(position.translateBy(Direction.UP));
        coordinates.add(position.translateBy(Direction.UP));
        coordinates.add(position.translateBy(Direction.RIGHT));

        return coordinates;

    }

    int index = 0;
    private int getNextIndex(Boolean ifclockwise) {
        //index++;
        return index;
    }

    private void circling(ArrayList<Position> coordinates, Position position, List<Entity> entities) {
        position = super.getPosition();
        super.setPosition(coordinates.get(getNextIndex(true)));
        
        //Direction direction;
        /*
        if (canMove(position, entities)) {
            for (int i = 0; i < coordinates.size(); i++) {
                System.out.println(super.getPosition());
                System.out.println(coordinates.get(i));

                super.setPosition(coordinates.get(i));
            }
        }*/
        
    }

    private void reverseDirection(ArrayList<Position> coordinates, Position position,  List<Entity> entities) {
        position = super.getPosition();
        if (canMove(position, entities)) {
            for (int i = coordinates.size() - 1; i >= 0; i--) {
                System.out.println(i);
                super.setPosition(coordinates.get(i));
            }
        }
    }

    private boolean canMove(Position position, List<Entity> entities) {
        if (position == null) {
            return false;
        }
        for (Entity entity : entities) {
            if (entity instanceof MovingEntity && entity.getPosition().equals(position)) {
                return false;
            }
            else if (entity instanceof Boulder && entity.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    public String getSimpleName() {
        return "spider";
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getType() {
        return type;
    }
    
}

