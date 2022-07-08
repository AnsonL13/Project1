package dungeonmania.StaticEntity;

import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    public boolean Istriggered;

    public FloorSwitch(String id, String type, Position position) {
        super(id, type, position);
        this.setType("FloorSwitch");
        
    }
    public boolean checkIstriggered() {
        return Istriggered;
    }
    public void setIsTriggered(boolean Istriggered) {
        this.Istriggered = Istriggered;
    }
    
}
