package dungeonmania.StaticEntity;

import dungeonmania.util.Position;

public class Wall extends StaticEntity {

    public Wall(String id, String type, Position position) {
        super(id, type, position);
        this.setType("Wall");
    }
    
}
