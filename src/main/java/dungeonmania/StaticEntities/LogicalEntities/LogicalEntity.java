package dungeonmania.StaticEntities.LogicalEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public abstract class LogicalEntity implements Entity {
    protected String id;
    protected String type;
    protected Position position;
    protected boolean isInteractable;
    protected String logic;

    // The tick when this entity was activated. -1 if not active. 
    protected int activeTickNumber;

    // Copy of activeTickNumber
    protected int prevActiveTickNumber;
    
    // List of every entity in the dungeon.
    protected List<Entity> entities;

    /*
     * List of active floor switches that are connected to this entity. 
     */
    protected List<LogicalEntity> activeEntities = new ArrayList<LogicalEntity>();

    /*
     * Executes whatever this entity does when it is active.
     */
    public abstract void Activate();

    /*
     * Executes whatever this entity does when it is not active.
     */
    public abstract void Deactivate();

    /*
     * Updates this entity active state if necessary. 
     */
    public abstract void update(LogicalEntity logicalEntity, boolean isActive, int tickNumber, boolean usePrevActiveTickNumber);

    /*
     * Updates cardinally adjacent entities to this entity. 
     */
    public void updateNeighbours(LogicalEntity source, LogicalEntity logicalEntity, boolean isActive, int tickNumber, boolean usePrevActiveTickNumber) {
        List<LogicalEntity> logicalEntities = getCardinallyAdjacentLogicalEntities();

        // logicalEntities.stream().forEach(o -> o.update(logicalEntity, isActive, tickNumber, usePrevActiveTickNumber));
        for (LogicalEntity entity : logicalEntities) {
            
            if (entity.equals(source)) {
                continue;
            }

            else {
                entity.update(logicalEntity, isActive, tickNumber, usePrevActiveTickNumber);
            }
        }   
    }

    /*
     * Get a list of all cardinally adjacent positions to the entity. 
     */
    public List<Position> getCardinallyAdjacentPositions(int x, int y) {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x  , y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x  , y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }

    /*
     * Get all cardinally adjacent logical entities to this entity. 
     */
    public List<LogicalEntity> getCardinallyAdjacentLogicalEntities() {

        List<LogicalEntity> logicalEntities = new ArrayList<LogicalEntity>();

        // Get cardinally adjacent positions to this entity.
        List<Position> adj = getCardinallyAdjacentPositions(this.getPosition().getX(), this.getPosition().getY());

        // Get all cardinally adjacent Logical entities 
        for (Entity entity : entities) {
            for (Position pos : adj) {
                if (entity.getPosition().equals(pos) && entity instanceof LogicalEntity) {
                    logicalEntities.add((LogicalEntity) entity);
                    break;
                }
            }
        }
        return logicalEntities;
    }

    /*
     * Returns -1 if this entity is inactive.
     * Return a number that is not -1 if this entity is active. 
     */
    public int isActive() {
        return this.activeTickNumber;
    }

    // Getters and setters
    public int getActiveTickNumber() {
        return activeTickNumber;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
