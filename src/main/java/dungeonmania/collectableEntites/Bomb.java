package dungeonmania.collectableEntites;

import dungeonmania.util.Position;

public class Bomb extends CollectableEntites {

    private boolean isActive;

    public Bomb(boolean isCollectable, Position position, boolean isActive) {
        super(isCollectable, position);
        this.isActive = isActive;
        //TODO Auto-generated constructor stub
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    
}
