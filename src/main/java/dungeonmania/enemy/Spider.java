package dungeonmania.enemy;

import dungeonmania.SpiderSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends Enemy {
    public Spider (int health, int attack, Position position) {
        super(health, attack, position);
    }

    public void move(boolean isBoulder, boolean isInvicible, boolean isInvisible, Position position, Boulder boulder, int health) {
        // When the spider spawns, they immediately move the 1 square upwards
        // Cannot tranverse boulders, reverse direction
        // Begin 'circling' their spawn spot 
        position = super.getPos();
        if (super.isInBattle()) {
            if (SpiderSpawner.isSpawnSuccess(position, boulder)== true) {
                moveUpwards();
            
                if (isBoulder) {
                    reverseDirection(health);
                } else {
                    circling(health);
                }
            }
        }
    }

    private void moveUpwards() {
        Position position = super.getPos();
        //Direction direction;
        position.translateBy(Direction.UP);
        if (canMove(position)) {
            super.setPos(position);
        }
    }

    private void circling(int health) {
        Position position = super.getPos();
        //Direction direction;
        if (health != 0) {
            position.translateBy(Direction.RIGHT);
            position.translateBy(Direction.DOWN);
            position.translateBy(Direction.DOWN);
            position.translateBy(Direction.LEFT);
            position.translateBy(Direction.LEFT);
            position.translateBy(Direction.UP);
            position.translateBy(Direction.UP);
            position.translateBy(Direction.RIGHT);
        }
        if (canMove(position)) {
            super.setPos(position);
        }
    }

    private void reverseDirection(int health) {
        Position position = super.getPos();
        if (health != 0) {
            
        }
    }

    private boolean canMove(Position position) {
        return true;
    }

    public void isBattle() {

    }

    public String getSimpleName() {
        return "spider";
    }
    
}

