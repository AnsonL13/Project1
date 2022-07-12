package dungeonmania.BuildableEntities;

import dungeonmania.util.Position;

public class Bow implements BuildableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int bowDurability;

    public Bow(String id, String type, Position position, boolean isInteractable, int bowDurability) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.bowDurability = bowDurability;
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

    public int getBowDurability() {
        return bowDurability;
    }
}
