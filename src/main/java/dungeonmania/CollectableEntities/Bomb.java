package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Bomb implements CollectableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int bombRadius;

    public Bomb(String id, String type, Position position, boolean isInteractable, int bombRadius) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.bombRadius = bombRadius;
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

    public int getBombRadius() {
        return bombRadius;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
