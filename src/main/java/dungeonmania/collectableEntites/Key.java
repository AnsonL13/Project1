package dungeonmania.collectableEntites;

import dungeonmania.util.Position;

public class Key extends CollectableEntites {
    private Door door;

    public Key(boolean isCollectable, Position position, Door door) {
        super(isCollectable, position);
        this.door = door;
    }

    public Door getDoor() {
        return door;
    }
        
}
