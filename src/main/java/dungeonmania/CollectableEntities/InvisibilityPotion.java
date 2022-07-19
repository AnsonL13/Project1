package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class InvisibilityPotion implements Potion {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int invisibilityPotionPuration;


    public InvisibilityPotion(String id, String type, Position position, boolean isInteractable, int invisibilityPotionPuration) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.invisibilityPotionPuration = invisibilityPotionPuration;
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

    public int getInvisibilityPotionPuration() {
        return invisibilityPotionPuration;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getDuration() {
        return invisibilityPotionPuration;
    }

    public void decrementDuration() {
        this.invisibilityPotionPuration--;
    }
}
