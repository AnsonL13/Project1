package dungeonmania.enemy;

import java.util.ArrayList;

import dungeonmania.SpiderSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends Enemy {
    private boolean isClockwise;
    private boolean isBoulder;
    public Spider (int health, int attack, Position position, boolean isClockwise, boolean isBoulder) {
        super(health, attack, position);
        this.isBoulder = isBoulder;
        this.isClockwise = isClockwise;
    }

    public Spider (int health, int attack, Position position) {
        super(health, attack, position);
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public boolean isBoulder() {
        return isBoulder;
    }

    public void move(boolean isBoulder, Position position, Boulder boulder, int health) {
        // When the spider spawns, they immediately move the 1 square upwards
        // Cannot tranverse boulders, reverse direction
        // Begin 'circling' their spawn spot 
        ArrayList<Position> coordinates = new ArrayList<Position>();
        Position newPos = super.getPos();
        coordinates.add(newPos.translateBy(Direction.UP));
        coordinates.add(newPos.translateBy(Direction.RIGHT));
        coordinates.add(newPos.translateBy(Direction.DOWN));
        coordinates.add(newPos.translateBy(Direction.DOWN));
        coordinates.add(newPos.translateBy(Direction.LEFT));
        coordinates.add(newPos.translateBy(Direction.LEFT));
        coordinates.add(newPos.translateBy(Direction.UP));
        coordinates.add(newPos.translateBy(Direction.UP));
        coordinates.add(newPos.translateBy(Direction.RIGHT));
        if (super.isInBattle()) {
            if (SpiderSpawner.canSpawn(position, boulder)== true) {
                
                if (SpiderSpawner.canSpawn(position, boulder) == true) {
                    moveUpwards(position);
                    isClockwise = true;
                    circling(health, coordinates, position);

                    if ((isBoulder)) {
                        isClockwise = false;
                        reverseDirection(health, coordinates, position);
                    }
                } 
            }
        }
    }

    private void moveUpwards(Position position) {
        position = super.getPos();
        //Direction direction;
        position.translateBy(Direction.UP);
        if (canMove(position)) {
            super.setPos(position);
        }
    }

    private void circling(int health, ArrayList<Position> coordinates, Position position) {
        position = super.getPos();
        //Direction direction;
        if (health != 0) {
            if (canMove(position)) {
                for (int i = 0; i < coordinates.size(); i++) {
                    super.setPos(coordinates.get(i));
                }
            }
        }
    }

    private void reverseDirection(int health, ArrayList<Position> coordinates, Position position) {
        position = super.getPos();
        if (health != 0) {
            if (canMove(position)) {
                for (int i = coordinates.size() - 1; i >= 0; i--) {
                    super.setPos(coordinates.get(i));
                }
            }
        }
    }

    private boolean canMove(Position position) {
        return true;
    }

    public String getSimpleName() {
        return "spider";
    }
    
}

