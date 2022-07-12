package dungeonmania.enemy;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class Enemy implements Entity {
    private boolean isInvicible = false;
    private boolean isInvisible = false;
    private int health;
    private int attack;
    private int id;
    private Position position;
    private boolean inBattle;

    public Enemy (int id, int health, int attack, Position position) { 
        this.health = health;
        this.attack = attack;
        this.position = position;
        this.id = id;
    }

    public void move (boolean isInvicible, boolean isInvisible, Position player) {
        inBattle = isBattle(player);
    }

    public boolean isBattle(Position player) {
        if (isInvisible) return false;
        if (player.equals(position)) return true;
        return false;
    }
    
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Position getPosition() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setInvisible() {
        this.isInvisible = true;
    }

    public void setInvincible() {
        this.isInvicible = true;
    }

    public void calculateRound() {
        // TODO Auto-generated method stub
    }

    public boolean isInvicible() {
        return isInvicible;
    }

    public boolean isInvisible() {
        return isInvisible;
    }


}

