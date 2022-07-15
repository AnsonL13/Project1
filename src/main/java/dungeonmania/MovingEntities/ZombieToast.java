package dungeonmania.MovingEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.PositionMovements.Movement;
import dungeonmania.MovingEntities.PositionMovements.RandomMovement;
import dungeonmania.MovingEntities.PositionMovements.RunAwayMovement;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity {

    private String type;
    private boolean isInteractable;

    private Movement movement;
    private RunAwayMovement runAwayMovement = new RunAwayMovement(this);
    private RandomMovement randomMovement = new RandomMovement(this);

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
        this.movement = randomMovement;
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
        this.movement = randomMovement;

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
     * @return boolean
     */
    @Override
    public boolean move(Position player, List<Entity> entities) {  
        Position newPos = movement.moveEnemy(player, entities);
        if (newPos != null) {
            super.setPosition(newPos);
        }  
        setPotions();
        return super.isBattle(player);
    }

    
    /** 
     * @param duration
     */
    @Override
    public void setInvincible(int duration) {
        super.setInvincible(duration);
        movement = runAwayMovement;
    }

    @Override
    public void setPotions() {
        super.setPotions();
        if (super.isInvicible() != true) movement = randomMovement;
    }
    
    /** 
     * @return String
     */
    public String getSimpleName() {
        return "zombie";
    }

}
