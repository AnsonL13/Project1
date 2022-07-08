package dungeonmania.StaticEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity {

    public Boulder(String id, String type, Position position) {
        super(id, type, position);
        this.setType("Boulder");
    }

    public boolean canMove(Direction direction) {
        Position newDirection = super.getPosition().translateBy(direction.getOffset());
        List<String> block = new ArrayList<>(Arrays.asList("Boulder","Wall","Door")); //maybe add more
        if (super.getPosition().equals(newDirection) && block.contains(super.getType())) {
            return false;
        }
        return true;
    }
    
}
