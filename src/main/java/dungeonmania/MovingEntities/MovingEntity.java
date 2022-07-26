package dungeonmania.MovingEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.util.Position;

public class MovingEntity implements Entity {
    protected boolean isInvincible;
    protected boolean isInvisible;
    protected double health;
    protected double attack;
    protected String id;
    protected Position position;
    protected boolean inBattle;
    protected String type;
    protected int stuckTimer;

    public MovingEntity (String id, double attack, double health, Position position) { 
        this.health = health;
        this.attack = attack;
        this.position = position;
        this.id = id;
        this.stuckTimer = 0;
    }

    /** 
     * @return double
     */
    public double getHealth() {
        return health;
    }
    
    /** 
     * @return double
     */
    public double getAttack() {
        return attack;
    }
    
    /** 
     * @param health
     */
    public void setHealth(double health) {
        this.health = health;
    }
    
    /** 
     * @return boolean
     */
    public boolean isInBattle() {
        return inBattle;
    }
    
    /** 
     * @param inBattle
     */
    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    /** 
     * @return boolean
     */
    public boolean isInteractable() {
        return false;
    }

    /** 
     * @return String
     */
    public String getId() {
        return id;
    }
    
    /** 
     * @return String
     */
    public String getType() {
        return type;
    }
    
    /** 
     * @return Position
     */
    public Position getPosition() {
        return position;
    }

    /** 
     * @return isInvisible
     */
    public boolean isInvisible() {
        return this.isInvisible;
    }

    /** 
     * @return isInvincible
     */
    public boolean isInvincible() {
        return this.isInvincible;
    }

    /*
     * Notifies this entity if a player is using a potion or not
     */
    public void setPotionStatus(boolean setInvisible, boolean setInvincible) {
        this.isInvisible = setInvisible;
        this.isInvincible = setInvincible;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /*
     * Setting the position, accounting for swamp tiles.
     */
    public void setPosition(Position position, List<Entity> entities) {
        this.position = position;

        // Check if moving into a swamp tile
        for (Entity entity : entities) {
            if (entity instanceof SwampTile && entity.getPosition().equals(position)) {
                SwampTile swampTile = (SwampTile) entity;
                this.stuckTimer = swampTile.getMovementFactor() + 1;
            }
        }
    }

    public void move(Position playerPos, List<Entity> entities) {
        return;
    }

    public void setStuckTimer(int stuckTimer) {
        this.stuckTimer = stuckTimer;
    }

    public int getStuckTimer() {
        return stuckTimer;
    }

    public void decrementStuckTimer() {
        if (stuckTimer > 0) {
            this.stuckTimer--;
        }
    }
}
