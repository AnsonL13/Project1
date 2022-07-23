package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public abstract class AlliedEntities implements Entity {
    public void move() {}
    public boolean isAllied() {
        return false;
    }
    @Override
    public boolean isInteractable() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Position getPosition() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setPosition(Position position) {
        // TODO Auto-generated method stub
        
    }

}
