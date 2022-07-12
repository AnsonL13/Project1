package dungeonmania.BuildableEntities;

import dungeonmania.util.Position;

public class Shield implements BuildableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int shieldDefence;
    private int shieldDurability;

    public Shield(String id, String type, Position position, boolean isInteractable, int shieldDefence, int shieldDurability) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.shieldDefence = shieldDefence;
        this.shieldDurability = shieldDurability;
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

    public int getShieldDefence() {
        return shieldDefence;
    }

    public int getShieldDurability() {
        return shieldDurability;
    }
}
