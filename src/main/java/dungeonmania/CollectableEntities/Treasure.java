package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Treasure implements CollectableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;

    public Treasure(String id, String type, Position position, boolean isInteractable) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
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

    public void setPosition(Position position) {
        this.position = position;
    }
}
