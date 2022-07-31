package dungeonmania.StaticEntities.LogicalEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.StaticEntity;
import dungeonmania.util.Position;

public class FloorSwitch extends LogicalEntity implements StaticEntity {

    private boolean hasBoulder;

    /*
     * Constructor for Milestone 2 compatibility (no logic field)
     */
    public FloorSwitch(String id, String type, Position position, boolean isInteractable, List<Entity> entities) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.entities = entities;
        this.logic = "none";
        this.activeTickNumber = -1;
        this.hasBoulder = false;
    }

    /*
     * Constructor for Milestone 3 compatibility
     */
    public FloorSwitch(String id, String type, Position position, boolean isInteractable, List<Entity> entities, String logic) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.entities = entities;
        this.logic = logic;
        this.activeTickNumber = -1;
        this.hasBoulder = false;
    }

    /*
     * Updates this entity based on whether or not cardinally adjacent entities are active or not. 
     */
    @Override
    public void update(LogicalEntity logicalEntity, boolean isActive, int tickNumber, boolean usePrevActiveTickNumber) {

        // A logical entity next to this entity has become active. 
        if (isActive) {
            // Check if entity is already in the list of switches.
            if (! activeEntities.contains(logicalEntity)) {
                // Add the entity to the list of active adjacent entities.
                activeEntities.add(logicalEntity);
            }

            // We already visited this entity. Stop the recursion. 
            else {
                return;
            }
        }

        // A logical entity next to this entity has become inactive. 
        else if (! isActive) {
            
            if (activeEntities.contains(logicalEntity)) {
                // Remove the entity from the list of active adjacent entities.
                activeEntities.remove(logicalEntity);
            }

            // We already visited this entity. Stop the recursion. 
            else {
                return;
            }
        }

        // Check if there are any changes to this entity's activity.
        // Check if there is a boulder on this switch. 
        boolean active = false;
        for (Entity entity : entities) {
            if (entity instanceof Boulder && entity.getPosition().equals(this.position)) {
                // Boulder found. This switch is definitely active. 
                active = true;
            }
        }

        // Not found boulder.  
        if (! active) {
            active = activeThroughAdjacent();
        }

        // Floorswitch got activated. 
        if (active && this.activeTickNumber == -1) {
            if (usePrevActiveTickNumber) {
                this.activeTickNumber = this.prevActiveTickNumber;
            }

            else {
                this.activeTickNumber = tickNumber;
                this.prevActiveTickNumber = tickNumber;
            }
        }

        // Floorswitch got deactivated. 
        else if (! active && this.activeTickNumber != -1) {
            this.activeTickNumber = -1;
        }

        updateNeighbours(logicalEntity, this, active, tickNumber, usePrevActiveTickNumber);
    }

    /*
     * Call this function with logicalEntity as a cardinally adjacent entity in order to update this floorswitch. 
     */
    public void updateThis(LogicalEntity logicalEntity, boolean isActive, int tickNumber, boolean usePrevActiveTickNumber) {
        // Check if there are any changes to this entity's activity.
        // Check if there is a boulder on this switch. 
        boolean active = false;
        for (Entity entity : entities) {
            if (entity instanceof Boulder && entity.getPosition().equals(this.position)) {
                // Boulder found. This switch is definitely active. 
                active = true;
            }
        }

        // Not found boulder.  
        if (! active) {
            active = activeThroughAdjacent();
        }

        // Floorswitch got activated. 
        if (active && this.activeTickNumber == -1) {
            if (usePrevActiveTickNumber) {
                this.activeTickNumber = this.prevActiveTickNumber;
            }

            else {
                this.activeTickNumber = tickNumber;
                this.prevActiveTickNumber = tickNumber;
            }
        }

        // Floorswitch got deactivated. 
        else if (! active && this.activeTickNumber != -1) {
            this.activeTickNumber = -1;
        }

        updateNeighbours(logicalEntity, this, active, tickNumber, usePrevActiveTickNumber);
    }

    /*
     * Checks if a boulder has moved on or off this floor switch
     * Completed for every switch on every tick to send updates. 
     */
    public void boulderUpdate(int tickNumber) {
        // Check if there is a boulder on this switch
        boolean foundBoulder = false;
        for (Entity entity : entities) {
            if (entity instanceof Boulder && entity.getPosition().equals(this.position)) {
                foundBoulder = true;
                this.hasBoulder = true;
                // Found boulder, and this floorswitch is inactive. Complete Changes. 
                if (this.activeTickNumber == -1) {
                    this.activeTickNumber = tickNumber;
                    updateNeighbours(this, this, true, tickNumber, false);
                }
                break;
            }
        }

        // Not found boulder and this entity is active. Complete changes if necessary. 
        if (! foundBoulder && this.activeTickNumber != -1 && this.hasBoulder) {
            this.hasBoulder = false;

            // Turn off the switch
            this.activeTickNumber = -1;
            // Tell all neighbours that this switch is now off. 
            updateNeighbours(this, this, false, tickNumber, false);
            
            List<LogicalEntity> logicalEntities = getCardinallyAdjacentLogicalEntities();

            List<LogicalEntity> newActiveEntities = new ArrayList<LogicalEntity>();

            // Get cardinally adjacent entities that are still active. 
            for (LogicalEntity entity : logicalEntities) {
                if (entity.isActive() != -1) {
                    newActiveEntities.add(entity);
                }
            }

            // Let cardinally adjacent active entities update this floor switch
            for (LogicalEntity entity : newActiveEntities) {
                this.updateThis(entity, true, tickNumber, true);
            }
        }
    }

    /*
     * Activating the floor switch does nothing
     */
    @Override
    public void Activate() {
        return;
    }

    /*
     * Deactivating the floor switch does nothing
     */
    @Override
    public void Deactivate() {
        return;
    }
}
