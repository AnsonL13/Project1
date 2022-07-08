package dungeonmania.StaticEntity;

import dungeonmania.util.Position;

public abstract class StaticEntity {
    String Id;
    String type;
    Position position;

    public StaticEntity(String id, String type, Position position) {
        Id = id;
        this.type = type;
        this.position = position;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    
    
    
}
