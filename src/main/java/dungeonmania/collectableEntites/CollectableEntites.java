package dungeonmania.collectableEntites;

import dungeonmania.util.Position;

public abstract class CollectableEntites {
    private boolean isCollectable;
    private Position position;

    public CollectableEntites(boolean isCollectable, Position position) {
        this.isCollectable = isCollectable;
        this.position = position;
    }

    public boolean isCollectable() {
        return isCollectable;
    }

    public void setCollectable(boolean isCollectable) {
        this.isCollectable = isCollectable;
    }

    public Position getPosition() {
        return position;
    }
    
}
