package dungeonmania.MovingEntities;

import dungeonmania.Enemy;
import dungeonmania.util.Position;

public class ZombieToast implements MovingEntity, Enemy {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int zombieAttack;
    private int zombieHealth;

    public ZombieToast(String id, String type, Position position, boolean isInteractable, int zombieAttack, int zombieHealth) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.zombieAttack = zombieAttack;
        this.zombieHealth = zombieHealth;
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

    public int getZombieAttack() {
        return zombieAttack;
    }

    public int getZombieHealth() {
        return zombieHealth;
    }
}
