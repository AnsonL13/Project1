package dungeonmania.enemy;

import dungeonmania.util.Position;

public class Mercenary extends Enemy {
    public Mercenary (int health, int attack, Position position) {
        super(health, attack, position);
    }

    public void move(boolean isInvicible, boolean isInvisible, Position player) {

    }

    public String getSimpleName() {
        return "mercenary";
    }
    
}