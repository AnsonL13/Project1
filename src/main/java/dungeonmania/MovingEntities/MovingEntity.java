package dungeonmania.MovingEntities;

import java.util.List;

import dungeonmania.Entity;
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

    public MovingEntity (String id, double attack, double health, Position position) { 
        this.health = health;
        this.attack = attack;
        this.position = position;
        this.id = id;
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

    public void setPotionStatus(boolean setInvisible, boolean setInvincible) {
        this.isInvisible = setInvisible;
        this.isInvincible = setInvincible;
    }

    public void setPosition(Position position) {
        this.position = position;        
    }

    public void move(Position playerPos, List<Entity> entities) {
        return;
    }

}
