package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Portal implements StaticEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private String colour;

    public Portal(String id, String type, Position position, boolean isInteractable, String colour) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.colour = colour;
    }

    public String getColour() {
        return colour;
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
