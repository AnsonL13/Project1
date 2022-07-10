package dungeonmania.enemy;

import dungeonmania.util.Position;

public abstract class Enemy {
    private int health;
    private int attack;
    private Position position;
    private boolean inBattle;

    public Enemy (int health, int attack, Position position) { 
        this.health = health;
        this.attack = attack;
        this.position = position;
    }

    public void move (boolean isInvicible, boolean isInvisible, Position player) {
        inBattle = isBattle(player);
    }

    public boolean isBattle(Position player) {
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

    public Position getPos() {
        return position;
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
}

