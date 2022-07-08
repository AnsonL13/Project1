package dungeonmania.StaticEntity;

import dungeonmania.util.Position;

public class Door extends StaticEntity {
    protected boolean isOpen;
    protected double DoorId;

    public Door(String id, String type, Position position,double DoorId) {
        super(id, type, position);
        this.DoorId = DoorId;
        this.setType("Door");
        
    }
    public boolean IsOpen() {
        return isOpen;
    }
    public void setDoor(boolean isOpen) {
        this.isOpen = isOpen;
    }
    public boolean openDoor(int keyID) {
        if (keyID == DoorId) {
            isOpen = true;
        }
        else {
            isOpen = false;
        }
        return isOpen;
    }
    
}
