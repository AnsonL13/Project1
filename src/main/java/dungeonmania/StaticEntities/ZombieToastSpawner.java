package dungeonmania.StaticEntities;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.InteractableEntity;
import dungeonmania.Player;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;


import dungeonmania.util.Position;

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

    public Position spawn(List<Entity> entities) { 
        List<Position> adj = getCardinallyAdjacentPositions(position.getX(), position.getY());
        for (Position pos : adj) {
            if (canSpawn(pos, entities)) {
                return pos;
            }
        }  
        return null;
    }

    public boolean canSpawn(Position pos, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity instanceof StaticEntity && entity.getPosition().equals(pos)) { //wall or door, boulder
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

    public List<Position> getCardinallyAdjacentPositions(int x, int y) {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x  , y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x  , y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }

    public boolean interactActionCheck(Player player) {
        // Check if player cardinally adjacent to spawner
        System.out.println(player.getWeapons().size());
        if (Position.isAdjacent(position, player.getPosition()) && ! player.getWeapons().isEmpty()) {
            return true;
        }
        return false;
    }

    public void interact(Dungeon dungeon) {
        // Destroy the zombie spawner
        dungeon.removeEntity(this.id);
        dungeon.removeInteractableEntity(this.id);

        // Decrease sword durability
        dungeon.getPlayer().decreaseSwordDurability();
    }
}

