package dungeonmania.enemy;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.SpiderSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;

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

    public void move(boolean isBoulder, Boulder boulder, int health, List<Entity> entities) {
        // When the spider spawns, they immediately move the 1 square upwards
        // Cannot tranverse boulders, reverse direction
        // Begin 'circling' their spawn spot 
        ArrayList<Position> coordinates = new ArrayList<Position>();
        Position position = null;
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

    private void moveUpwards(Position position) {
        position = super.getPos();
        //Direction direction;
        position.translateBy(Direction.UP);
        if (canMove(position, entities)) {
            super.setPos(position);
        }
    }

    private void circling(int health, ArrayList<Position> coordinates, Position position) {
        position = super.getPos();
        //Direction direction;
        if (health != 0) {
            if (canMove(position, entities)) {
                for (int i = 0; i < coordinates.size(); i++) {
                    super.setPos(coordinates.get(i));
                }
            }
        }
    }

    private void reverseDirection(int health, ArrayList<Position> coordinates, Position position) {
        position = super.getPos();
        if (health != 0) {
            if (canMove(position, entities)) {
                for (int i = coordinates.size() - 1; i >= 0; i--) {
                    super.setPos(coordinates.get(i));
                }
            }
        }
    }

    private boolean canMove(Position position, List<Entity> entities) {
        if (position == null) {
            return false;
        }
        for (Entity entity : entities) {
            if (entity instanceof MovingEntity && entity.getPosition().equals(position)) {
                return false;
            }
            else if (entity instanceof Boulder && entity.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    public String getSimpleName() {
        return "spider";
    }
    
}

