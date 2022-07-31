package dungeonmania.MovingEntities.PositionMovements;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.DijkstrasAlgo;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.util.Position;

public class FollowMovement extends Movement {
    private Mercenary enemy;
    private DijkstrasAlgo shortestPath;

    public FollowMovement (Mercenary enemy) {
        this.enemy = enemy;
        this.shortestPath = new DijkstrasAlgo(enemy.getPosition());
    }

    /*
     * Find the next closest square to the player. 
     */
    @Override
    public Position moveEnemy(Position player, List<Entity> entities) {
        shortestPath.generateMap(entities);
        shortestPath.DijstrasPosition();
        return shortestPath.getNextPos(player);
    }

    public Mercenary getEnemy() {
        return enemy;
    }
}
