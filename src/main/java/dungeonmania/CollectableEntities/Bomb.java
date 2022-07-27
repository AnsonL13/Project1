package dungeonmania.CollectableEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.CollectableEntities.BombStates.ActiveBombState;
import dungeonmania.CollectableEntities.BombStates.BombState;
import dungeonmania.CollectableEntities.BombStates.InactiveBombState;
import dungeonmania.CollectableEntities.BombStates.InventoryBombState;
import dungeonmania.StaticEntities.LogicalEntities.FloorSwitch;
import dungeonmania.StaticEntities.LogicalEntities.LogicalEntity;
import dungeonmania.util.Position;

public class Bomb extends LogicalEntity implements CollectableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int bombRadius;

    BombState inactiveBombState;
    BombState inventoryBombState;
    BombState activeBombState;

    BombState state;

    public Bomb(String id, String type, Position position, boolean isInteractable, int bombRadius, Dungeon dungeon, Player player, List<Entity> entities) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.bombRadius = bombRadius;
        
        inactiveBombState = new InactiveBombState(this, dungeon, player);
        inventoryBombState = new InventoryBombState(this, dungeon, player);
        activeBombState = new ActiveBombState(this, dungeon, player);
        state = inactiveBombState;

        this.activeTickNumber = -1;
        this.logic = "none";
        this.entities = entities;
    }

    /*
     * Constructor for Milestone 3 logical entities.
     */
    public Bomb(String id, String type, Position position, boolean isInteractable, int bombRadius, Dungeon dungeon, Player player, List<Entity> entities, String logic) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.bombRadius = bombRadius;
        
        inactiveBombState = new InactiveBombState(this, dungeon, player);
        inventoryBombState = new InventoryBombState(this, dungeon, player);
        activeBombState = new ActiveBombState(this, dungeon, player);
        state = inactiveBombState;

        this.activeTickNumber = -1;
        this.logic = logic;
        this.entities = entities;
    }

    // State pattern functionality
    public void pickUp() {
        state.pickUp();
    }

	public void putDown() {
        state.putDown();
    }

    public void explode() {
        state.explode();
    }

    public void setState(BombState state) {
		this.state = state;
	}

    public BombState getInactiveBombState() {
        return inactiveBombState;
    }

    public BombState getInventoryBombState() {
        return inventoryBombState;
    }

    public BombState getActiveBombState() {
        return activeBombState;
    }

    /*
     * Get the list of squares in the bombs range
     */
    public List<Position> getTargetSquares() {
        List<Position> squares = new ArrayList<>();
        int startingX = this.position.getX() - this.bombRadius;
        int startingY = this.position.getY() - this.bombRadius;
        int endingX = this.position.getX() + this.bombRadius;
        int endingY = this.position.getY() + this.bombRadius;
        for (int i = startingX; i <= endingX; i++) {
            for (int j = startingY; j <= endingY; j++) {
                squares.add(new Position(i, j));
            }
        }
        return squares;
    }

    // Getters
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

    public int getBombRadius() {
        return bombRadius;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    
    @Override
    public void Activate() {
        // Explode the bomb
        setState(activeBombState);
        state.explode(true);
    }

    @Override
    public void Deactivate() {
        // Do nothing
        return;
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

        /*
        // This entity got deactivated. 
        else if (! active && this.activeTickNumber != -1) {
            this.activeTickNumber = -1;

            this.Deactivate();
        }
        */
        
    }
}
