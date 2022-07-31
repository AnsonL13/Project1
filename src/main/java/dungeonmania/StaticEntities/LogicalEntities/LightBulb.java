package dungeonmania.StaticEntities.LogicalEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class LightBulb extends LogicalEntity {

    public LightBulb(String id, String type, Position position, boolean isInteractable, List<Entity> entities, String logic) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.entities = entities;
        this.logic = logic;
        this.activeTickNumber = -1;
    }

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

        // Check if this entity is active. 
        boolean active = activeThroughAdjacent();

        // This entity got activated.
        if (active && this.activeTickNumber == -1) {
            if (usePrevActiveTickNumber) {
                this.activeTickNumber = this.prevActiveTickNumber;
            }

            else {
                this.activeTickNumber = tickNumber;
                this.prevActiveTickNumber = tickNumber;
            }

            // Activate this light bulb
            this.Activate();
        }

        // This entity got deactivated. 
        else if (! active && this.activeTickNumber != -1) {
            this.activeTickNumber = -1;

            // Deactivate this light bulb
            this.Deactivate();
        }
    }


    /*
     * Turn on light bulb
     */
    @Override
    public void Activate() {
        this.type = "light_bulb_on";
    }

    /*
     * Turn off light bulb
     */
    @Override
    public void Deactivate() {
        this.type = "light_bulb_off";
    }
}
