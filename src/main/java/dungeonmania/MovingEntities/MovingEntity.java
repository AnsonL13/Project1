package dungeonmania.MovingEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class MovingEntity implements Entity {
    private int isInvicible;
    private int isInvisible;
    private double health;
    private int attack;
    private String id;
    private Position position;
    private boolean inBattle;
    private String type;

    public MovingEntity (String id, int health, int attack, Position position) { 
        this.health = (double) health;
        this.attack = attack;
        this.position = position;
        this.id = id;
    }

    public boolean move (Position player, List<Entity> entities) {
        return isBattle(player);
    }

    public boolean isBattle(Position player) {
        if (isInvisible != 0) return false;
        if (player.equals(position)) return true;
        return false;
    }
    
    public double getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setPos(Position position) {
        this.position = position;
    }

    public boolean isInBattle() {
        return inBattle;
    }

    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }
  
    public String getSimpleName() {
        return null;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setInvisible(int duration) {
        this.isInvisible = duration;
    }

    public void setInvincible(int duration) {
        this.isInvicible = duration;
    }

    public void calculateRound() {
        // TODO Auto-generated method stub
    }

    public boolean isInvicible() {
        if (isInvicible > 0) return true;
        return false;    
    }

    public boolean isInvisible() {
        if (isInvisible > 0) return true;
        return false;    
    }

    public void setPotions() {
        if (isInvicible > 0) {
            isInvicible--;
        } else if (isInvisible > 0) {
            isInvisible--;
        }
    }

    @Override
    public void setPosition(Position position) {
        // TODO Auto-generated method stub
        
    }


}
