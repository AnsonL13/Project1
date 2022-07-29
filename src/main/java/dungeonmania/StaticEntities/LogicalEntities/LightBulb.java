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
        boolean active = false;

        switchstatement:
        switch (this.logic) {
            case "and":
                List<LogicalEntity> logicalEntities = getCardinallyAdjacentLogicalEntities();

                // Get the number of adjacent floorswitches
                int floorSwitchCount = 0;
                for (LogicalEntity entity : logicalEntities) {
                    if (entity instanceof FloorSwitch) {
                        floorSwitchCount++;
                    }
                }

                // If there are more than two switches adjacent, all must be activated.
                if (floorSwitchCount > 2) {
                    for (LogicalEntity entity : logicalEntities) {
                        if (entity instanceof FloorSwitch && entity.isActive() == -1) {
                            break switchstatement;
                        }
                    }
                }

                if (activeEntities.size() >= 2) {
                    active = true;
                }

                break;
            
            case "or":
                if (activeEntities.size() >= 1) {
                    active = true;
                }

                break;
            
            case "xor":
                if (activeEntities.size() == 1) {
                    active = true;
                }

                break;

            case "co_and":
                // Check if there are 2 or more active entities. 
                if (activeEntities.size() < 2) {
                    break switchstatement;
                }

                // Check if all entities were activated on the same tick.
                int targetTick = activeEntities.get(0).isActive();
                for (LogicalEntity entity : activeEntities) {
                    if (entity.isActive() != targetTick) {
                        break switchstatement;
                    }
                }

                active = true;

                break;

            default:
                // This entity has no logic statement.  
                if (activeEntities.size() >= 1) {
                    active = true;
                }
                break;
        }
        
        // This entity go activated.
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
