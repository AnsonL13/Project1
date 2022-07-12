package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class InvincibilityPotion implements CollectableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int invincibilityPotionDuration;

    public InvincibilityPotion(String id, String type, Position position, boolean isInteractable, int invincibilityPotionDuration) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.invincibilityPotionDuration = invincibilityPotionDuration;
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

    public int getInvincibilityPotionDuration() {
        return invincibilityPotionDuration;
    }
}
