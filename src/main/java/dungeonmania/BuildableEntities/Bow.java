package dungeonmania.BuildableEntities;

import dungeonmania.Weapon;

public class Bow implements BuildableEntity, Weapon {
    private String id;
    private String type;
    private boolean isInteractable;
    private int bowDurability;

    public Bow(String id, String type, boolean isInteractable, int bowDurability) {
        this.id = id;
        this.type = type;
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

    public int getBowDurability() {
        return bowDurability;
    }

    public void decreaseDurability() {
        this.bowDurability--;
    }
}
