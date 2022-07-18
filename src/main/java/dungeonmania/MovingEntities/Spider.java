package dungeonmania.MovingEntities;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    private boolean isClockwise;
    private String type;
    private boolean isInteractable;
    private boolean movedUp;
    private ArrayList<Position> coordinates;
    private int index;

    public Spider(String id, String type, Position position, boolean isInteractable, int attack, int health) {
        super(id, attack, health, position);
        this.type = type;
        this.isInteractable = isInteractable;
        this.isClockwise = true;
        this.movedUp = false;
        this.index = 0;
        setSpiderAdjacentPositions();
    }

    public Spider(String id, Position position, int attack, int health) {
        super(id, attack, health, position);
        this.type = "spider";
        this.isInteractable = false;
        this.isClockwise = true;
        this.movedUp = false;
        this.index = 0;
        setSpiderAdjacentPositions();
    }

    /*
     * Get the positions that the spider can go to in order of first to last in a list. 
     */
    public void setSpiderAdjacentPositions() {
        int x = this.position.getX();
        int y = this.position.getY();
        ArrayList<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x  , y-1));
        adjacentPositions.add(new Position(x+1, y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x+1, y+1));
        adjacentPositions.add(new Position(x  , y+1));
        adjacentPositions.add(new Position(x-1, y+1));
        adjacentPositions.add(new Position(x-1, y));
        adjacentPositions.add(new Position(x-1, y-1));
        
        this.coordinates = adjacentPositions;
    }

    /*
     * Move the spider upwards if possible
     */
    private void moveUpwards(List<Entity> entities) {
        // Check for boulder
        if (canMove(coordinates.get(0), entities)) {
            this.position = coordinates.get(0);
            this.movedUp = true;
            this.index++;
        }
    }

    /*
     * Move the spider. 
     */
    @Override
    public void move(Position player, List<Entity> entities) {
        // When the spider spawns, they immediately move the 1 square upwards
        // Cannot tranverse boulders, reverse direction
        // Begin 'circling' their spawn spot 

        if (movedUp == false) {
            moveUpwards(entities);
        } 
        
        else if (isClockwise) {
            circling(entities);
        } 

        else if (! isClockwise) { 
            reverseDirection(entities);
        }        
    }

    /*
     * Get the next index if the array that corresponds to a clockwise movement
     */
    public int getNextClockwise(int current) {
        int next = 0;
        if (current == 7) {
            next = 0;
        }

        else {
            next = current + 1;
        }
        return next;
    }

    /*
     * Get the next index if the array that corresponds to an anticlockwise movement
     */
    public int getNextAntiClockwise(int current) {
        int next = 0;
        if (current == 0) {
            next = 7;
        }

        else {
            next = current - 1;
        }
        return next;
    }

    /*
     * Clockwise movement for spider.
     */
    private void circling(List<Entity> entities) {
        int currentPosition = getNextAntiClockwise(index);
        Position nextPos = coordinates.get(index);
        if (canMove(nextPos, entities)) {
            this.position = nextPos;
            this.index = getNextClockwise(index);
        }

        else {
            // Check if can move anticlockwise
            if(canMove(coordinates.get(getNextAntiClockwise(currentPosition)), entities)) {
                // Set movement to anticlockwise
                this.isClockwise = false;
                this.index = getNextAntiClockwise(index);
                this.index = getNextAntiClockwise(index);
                nextPos = coordinates.get(index);
                this.position = nextPos;
                this.index = getNextAntiClockwise(index);
            }
        }
    }

    /*
     * Anticlockwise movement for spider.
     */
    private void reverseDirection(List<Entity> entities) {
        int currentPosition = getNextClockwise(index);
        Position nextPos = coordinates.get(index);
        if (canMove(nextPos, entities)) {
            this.position = nextPos;
            this.index = getNextAntiClockwise(index);
        }

        else {
            // Check if can move clockwise
            if(canMove(coordinates.get(getNextClockwise(currentPosition)), entities)) {
                // Set movement to clockwise
                this.isClockwise = true;
                this.index = getNextClockwise(index);
                this.index = getNextClockwise(index);
                nextPos = coordinates.get(index);
                this.position = nextPos;
                this.index = getNextClockwise(index);
            }
        }
    }

    /*
     * Checks if there is a boulder in the spiders path.
     * Return true if there is no boulder
     * Return false if there is a boulder
     */
    private boolean canMove(Position position, List<Entity> entities) {
        if (position == null) {
            return false;
        }
        for (Entity entity : entities) {
            if (entity instanceof Boulder && entity.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getType() {
        return type;
    }
}
