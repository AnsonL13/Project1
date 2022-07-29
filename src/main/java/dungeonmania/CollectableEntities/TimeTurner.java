package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class TimeTurner implements CollectableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;

    public TimeTurner(String id, String type, Position position, boolean isInteractable) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
    }

    @Override
    public boolean isInteractable() {
        return isInteractable;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;        
    }
    
}
