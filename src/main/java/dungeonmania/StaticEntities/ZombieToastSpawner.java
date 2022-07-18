package dungeonmania.StaticEntities;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.InteractableEntity;
import dungeonmania.Player;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;

public class ZombieToastSpawner implements StaticEntity, InteractableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;

    public ZombieToastSpawner(String id, String type, Position position, boolean isInteractable) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
    }

    /*
     * Return an available position for the zombie to spawn in. 
     */
    public Position spawn(List<Entity> entities) { 
        List<Position> adj = getCardinallyAdjacentPositions(position.getX(), position.getY());
        for (Position pos : adj) {
            if (canSpawn(pos, entities)) {
                return pos;
            }
        }  
        return null;
    }

    /*
     * Check a position to see if a zombie can spawn in that position
     */
    public boolean canSpawn(Position pos, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity instanceof StaticEntity && entity.getPosition().equals(pos)) {
                return false;
            }
        }
        return true;
    }


    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getId() {
        return id;
    }

    public final String getType() {
        return type;
    }

    public final Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /*
     * Get a list of all cardinally adjacent positions to the spawner. 
     */
    public List<Position> getCardinallyAdjacentPositions(int x, int y) {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x  , y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x  , y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }

    /*
     * Check if the player can interact with the spawner. 
     */
    public boolean interactActionCheck(Player player) {
        // Check if player cardinally adjacent to spawner
        if (Position.isAdjacent(position, player.getPosition()) && ! player.getWeapons().isEmpty()) {
            return true;
        }
        return false;
    }

    /*
     * Interact with the spawner. 
     */
    public void interact(Dungeon dungeon) {
        // Destroy the zombie spawner
        dungeon.removeEntity(this.id);
        dungeon.removeInteractableEntity(this.id);

        // Decrease sword durability
        dungeon.getPlayer().decreaseSwordDurability();
    }
}

