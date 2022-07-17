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
    private ArrayList<Position> coordinates = new ArrayList<Position>();
    private int index = 0;

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

    private ArrayList<Position> getCoordinates() {
        ArrayList<Position> coordinates = new ArrayList<Position>();
        Position position = super.getPosition();

        coordinates.add(position.translateBy(Direction.RIGHT));
        coordinates.add(position.translateBy(Direction.DOWN));
        coordinates.add(position.translateBy(Direction.DOWN));
        coordinates.add(position.translateBy(Direction.LEFT));
        coordinates.add(position.translateBy(Direction.LEFT));
        coordinates.add(position.translateBy(Direction.UP));
        coordinates.add(position.translateBy(Direction.UP));
        coordinates.add(position.translateBy(Direction.RIGHT));
        //coordinates.add(position.translateBy(Direction.RIGHT));
    
        return coordinates;

    }

    @Override
    public boolean move(Position player, List<Entity> entities) {
        // When the spider spawns, they immediately move the 1 square upwards
        // Cannot tranverse boulders, reverse direction
        // Begin 'circling' their spawn spot 
        Position position = null;

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
    
    private int getNextIndexClockwise(Boolean isClockwise) {
        int i = index;
        System.out.println(i);
        if (isClockwise) {
            if (index >= 7) {
                index = index - 8;
            }
            index++;   
        }
        return i;
    }

    private int getNextIndexAntiClockwise(Boolean isClockwise) {
        int i = index;
        if (! isClockwise) {
            if (index <= 0) {
                index = index + 7;
            }
            index--;
        }
        return i;
    }

    private void circling(ArrayList<Position> coordinates, Position position, List<Entity> entities) {
        position = super.getPosition();
        //System.out.println("here");
        Position nextPos = getCoordinates().get(getNextIndexClockwise(true));
        if (canMove(nextPos, entities)) {
            //super.setPosition(coordinates.get(getNextIndexClockwise(true)));
            //System.out.println("here2");
            //System.out.println(nextPos);
            super.setPosition(nextPos);
        }
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
        Position nextPos = getCoordinates().get(getNextIndexAntiClockwise(false));
        if (canMove(nextPos, entities)) {
            //super.setPosition(coordinates.get(getNextIndexAntiClockwise(false)));
            super.setPosition(nextPos);
        }
        /* 
        if (canMove(position, entities)) {
            for (int i = coordinates.size() - 1; i >= 0; i--) {
                System.out.println(i);
                super.setPosition(coordinates.get(i));
            }
        }
        */
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

