package dungeonmania.enemy;

public abstract class Enemy {
    private int health;
    private int attack;
    private int x;
    private int y;
    private boolean inBattle;

    public Enemy (int health, int attack) { 
        this.health = health;
        this.attack = attack;
    }

    public void move (boolean isInvicible, boolean isInvisible, int x, int y) {
        inBattle = isBattle();
    }

    public boolean isBattle() {
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isInBattle() {
        return inBattle;
    }

    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }
  
}

