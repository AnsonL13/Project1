package dungeonmania.StaticEntities.LogicalEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class Wire extends LogicalEntity {
    
    public Wire(String id, String type, Position position, boolean isInteractable, List<Entity> entities) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.entities = entities;
        this.logic = "none";
        this.activeTickNumber = -1;
    }

    /*
     * Update the activity of this wire if necessary. 
     */
    @Override
    public void update(LogicalEntity logicalEntity, boolean isActive, int tickNumber, boolean usePrevActiveTickNumber) {
        
        // A logical entity next to this wire has become active. 
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

        // A logical entity next to this wire has become inactive. 
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

        // Check if this entity is not active
        if (this.activeTickNumber == -1 && activeEntities.size() >= 0) {
            // Update the activeTickNumber
            if (usePrevActiveTickNumber) {
                this.activeTickNumber = this.prevActiveTickNumber;
            }

            else {
                this.activeTickNumber = tickNumber;
                this.prevActiveTickNumber = tickNumber;
            }
        }

        // Check if this entity is active
        else if (this.activeTickNumber != -1 && activeEntities.size() == 0) {
            // Update the activeTickNumber
            this.activeTickNumber = -1;
        }

        // Tell all neighbours of the change. 
        updateNeighbours(logicalEntity, this, isActive, tickNumber, usePrevActiveTickNumber);
    }

    /*
     * Activating the wire does nothing. 
     */
    @Override
    public void Activate() {
        return;
    }

    /*
     * Deactivating the wire does nothing.
     */
    @Override    
    public void Deactivate() {
        return;
    }
}
