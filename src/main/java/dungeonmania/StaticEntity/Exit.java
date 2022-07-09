package dungeonmania.StaticEntity;

import dungeonmania.util.Position;

public class Exit extends StaticEntity {

    /**
     * @param id
     * @param type
     * @param position
     */
    public Exit(String id, String type, Position position) {
        super(id, type, position);
        this.setType("Exit");      
    }
    
}
