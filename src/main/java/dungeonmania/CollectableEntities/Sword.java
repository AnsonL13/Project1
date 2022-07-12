package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Sword implements CollectableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int swordAttack;
    private int swordDurability;

    public Sword(String id, String type, Position position, boolean isInteractable, int swordAttack, int swordDurability) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.swordAttack = swordAttack;
        this.swordDurability = swordDurability;
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

    public int getSwordAttack() {
        return swordAttack;
    }

    public int getSwordDurability() {
        return swordDurability;
    }
}
