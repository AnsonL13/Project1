package dungeonmania.BuildableEntities;

import dungeonmania.Weapon;

public class Shield implements BuildableEntity, Weapon {
    private String id;
    private String type;
    private boolean isInteractable;
    private int shieldDefence;
    private int shieldDurability;

    public Shield(String id, String type, boolean isInteractable, int shieldDefence, int shieldDurability) {
        this.id = id;
        this.type = type;
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

    public int getShieldDefence() {
        return shieldDefence;
    }

    public int getShieldDurability() {
        return shieldDurability;
    }

    public void decreaseDurability() {
        this.shieldDurability--;
    }
}
