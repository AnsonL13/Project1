package dungeonmania.StaticEntities.LogicalEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class SwitchDoor extends LogicalEntity {
    private int key;
    private boolean isOpen;
    private boolean openForever;

    public SwitchDoor(String id, String type, Position position, boolean isInteractable, int key, List<Entity> entities, String logic) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.key = key;
        this.isOpen = false;
        this.entities = entities;
        this.logic = logic;
        this.activeTickNumber = -1;
        this.openForever = false;
    }

    public int getKey() {
        return key;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        if (this.openForever) {
            return;
        }
        
        this.isOpen = isOpen;
    }

    public void setOpenForever(boolean openForever) {
        this.openForever = openForever;
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

            this.Activate();
        }

        // This entity got deactivated. 
        else if (! active && this.activeTickNumber != -1) {
            this.activeTickNumber = -1;

            this.Deactivate();
        }
    }

    /*
     * Unlock the door
     */
    @Override
    public void Activate() {
        this.setOpen(true);
    }

    /*
     * Lock the door
     */
    @Override
    public void Deactivate() {
        this.setOpen(false);
    }
}
