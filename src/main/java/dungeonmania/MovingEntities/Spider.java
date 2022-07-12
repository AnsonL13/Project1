package dungeonmania.MovingEntities;

import dungeonmania.Enemy;
import dungeonmania.util.Position;

public class Spider implements MovingEntity, Enemy {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int spiderAttack;
    private int spiderHealth;

    public Spider(String id, String type, Position position, boolean isInteractable, int spiderAttack, int spiderHealth) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.spiderAttack = spiderAttack;
        this.spiderHealth = spiderHealth;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getId() {
        return id;
    }

    public final String getType() {
        return type;
    }

    public final Position getPosition() {
        return position;
    }

    public int getSpiderAttack() {
        return spiderAttack;
    }

    public int getSpiderHealth() {
        return spiderHealth;
    }
}
