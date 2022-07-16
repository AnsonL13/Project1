package dungeonmania.MovingEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.MovingEntities.PositionMovements.Movement;
import dungeonmania.MovingEntities.PositionMovements.RandomMovement;
import dungeonmania.MovingEntities.PositionMovements.RunAwayMovement;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity {

    private String type;
    private boolean isInteractable;

    private Movement movement;

    /**
     * Constructor 
     * @param id
     * @param type string name of clas
     * @param position
     * @param isInteractable if player can interact
     * @param attack
     * @param health
     */
    public ZombieToast(String id, String type, Position position, boolean isInteractable, int attack, int health) {
        super(id, attack, health, position);
        this.type = type;
        this.isInteractable = isInteractable;
        this.movement = new RandomMovement(this);
    }

    /**
     * Constructor
     * @param id
     * @param attack
     * @param health
     * @param position
     */
    public ZombieToast(String id, int attack, int health, Position position) {
        super(id, attack, health, position);
        this.isInteractable = false;
        this.type = "zombie_toast";
        this.movement = new RandomMovement(this);
    }
    
    /** 
     * @return boolean
     */
    @Override
    public boolean isInteractable() {
        return isInteractable;
    }

    /** 
     * @return String
     */
    @Override
    public final String getType() {
        return type;
    }

    /** 
     * @param player
     * @param entities
     * @return void
     */
    @Override
    public void move(Position playerPos, List<Entity> entities) {
        Position newPos = null;

        // Check if player is is Invincible
        if (isInvincible) {
            changeMovement(new RunAwayMovement(this));
        }

        // Check if player is is is Invisible
        else if (isInvisible) {
            changeMovement(new RandomMovement(this));
        }

        else {
            changeMovement(new RandomMovement(this));
        }

        newPos = movement.moveEnemy(playerPos, entities);

        if (newPos != null) {
            super.setPosition(newPos);
        }
    }

    /** 
     * @param newMovement
     * @return void
     * Changes the movement strategy of the zombie.
     */
    public void changeMovement(Movement newMovement) {
        this.movement = newMovement;
    }
}
