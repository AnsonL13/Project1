package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Door implements StaticEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int key;

    public Door(String id, String type, Position position, boolean isInteractable, int key) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.key = key;
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

    public int getKey() {
        return key;
    }
}
