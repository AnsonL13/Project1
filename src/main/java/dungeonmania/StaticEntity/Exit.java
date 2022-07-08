package dungeonmania.StaticEntity;

import dungeonmania.util.Position;

public class Exit extends StaticEntity {
    protected Player player;

    public Exit(String id, String type, Position position) {
        super(id, type, position);
        this.player = null;
        this.setType("Exit");      
    }
    
}
