package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class SwampTile implements StaticEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int movementFactor;

    public SwampTile(String id, String type, Position position, boolean isInteractable, int movementFactor) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.movementFactor = movementFactor;
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

    public int getMovementFactor() {
        return movementFactor;
    }
}
