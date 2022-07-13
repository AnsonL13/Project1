package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.InteractableEntity;
import dungeonmania.Player;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Position;
import java.util.List;


import dungeonmania.util.Position;

public class ZombieToastSpawner implements StaticEntity, InteractableEntity {
    private final String id;
    private final String type;
    private final Position position;
    private final boolean isInteractable;

    public ZombieToastSpawner(String id, String type, Position position, boolean isInteractable) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
    }

    public Position spawn(List<Entity> entities) { 
        List<Position> adj = position.getAdjacentPositions();
        for (Position pos : adj) {
            if (canSpawn(pos, entities)) {
                return pos;
            }
        }  
        return null;
    }

    public boolean canSpawn(Position pos, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity instanceof MovingEntity && entity.getPosition().equals(pos)) {
                return false;
            } else if (entity instanceof StaticEntity && entity.getPosition().equals(pos)) { //wall or door, boulder
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

    public boolean interactActionCheck(Player player) {
        // Check if player cardinally adjacent to spawner
        if (Position.isAdjacent(position, player.getPosition())) {
            return true;
        }

        // Check if player has a weapon in their inventory
        if (! player.getWeapons().isEmpty()) {
            return true;
        }

        return false;
    }
}

