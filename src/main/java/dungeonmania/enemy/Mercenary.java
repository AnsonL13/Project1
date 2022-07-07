package dungeonmania.enemy;

public class Mercenary extends Enemy {
    public Mercenary (int health, int attack) {
        super(health, attack);
    }

    public void move(boolean isInvicible, boolean isInvisible, int x, int y) {

    }

    public boolean isBattle() {
        return true;
    }
    
}