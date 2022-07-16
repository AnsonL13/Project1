package dungeonmania.CollectableEntities.BombStates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Player;
import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.util.Position;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.Entity;


public class ActiveBombState implements BombState {
    private Bomb bomb;
    private Dungeon dungeon;
    private Player player;

    public ActiveBombState(Bomb bomb, Dungeon dungeon, Player player) {
        this.bomb = bomb;
        this.dungeon = dungeon;
        this.player = player;
    }

    public void pickUp() {
        // Cannot pickup the bomb
        return;
    }

	public void putDown() {
        // Cannot putdown the bomb
        return;
    }

    public void explode() {
        // Check if bomb is next to an acitve switch
        boolean canExplode = false;
        List<Position> correctSquares = getCardinallyAdjacentPositions(bomb.getPosition().getX(), bomb.getPosition().getY());
        outerloop:
        for (Entity i : dungeon.getEntities()) {
            if (i.getType().equals("switch")) {
                boolean foundSwitch = false;
                // Check if the switch is cardinally adjacent to the bomb
                for (Position pos : correctSquares) {
                    if (pos.equals(i.getPosition())) {
                        foundSwitch = true;
                    }
                }

                if (!foundSwitch) {
                    continue;
                }

                // Look for a boulder on top of the switch. 
                for (Entity j : dungeon.getEntities()) {
                    if (j.getType().equals("boulder") && j.getPosition().equals(i.getPosition())) {
                        canExplode = true;
                        break outerloop;
                    }
                }
            }
        }

        if (! canExplode) {
            return;
        }

        // Explode the bomb, remove everything with its radius (Including itself)
        List<Position> targetSquares = bomb.getTargetSquares();
        for (Position targetsquare : targetSquares) {
            // Remove from players moving entity list
            Iterator<MovingEntity> movingEntityIterator = player.getMovingEntities().iterator();
            MovingEntity movingEntity;
            while(movingEntityIterator.hasNext()) {     
                movingEntity = movingEntityIterator.next();
                // check if the moving entity is in the target square
                if (movingEntity.getPosition().equals(targetsquare)) {
                    // remove the entity
                    movingEntityIterator.remove();
                }
            }

            // Remove from dungeon
            Iterator<Entity> entityIterator = dungeon.getEntities().iterator();
            Entity entity;
            while(entityIterator.hasNext()) {     
                entity = entityIterator.next();
                // check if the moving entity is in the target square
                if (entity.getPosition().equals(targetsquare)) {
                    // remove the entity
                    if (!entity.getType().equals("player")) {
                        entityIterator.remove();
                        dungeon.removeEntityFully(entity.getId());
                    }
                }
            }
        }
    }

    public List<Position> getCardinallyAdjacentPositions(int x, int y) {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x  , y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x  , y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }
}
