package dungeonmania.StaticEntities;

import dungeonmania.InteractableEntity;
import dungeonmania.Player;
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
