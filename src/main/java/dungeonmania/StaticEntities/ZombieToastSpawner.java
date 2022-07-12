package dungeonmania.StaticEntities;

import dungeonmania.InteractableEntity;
import dungeonmania.Player;
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
        Position coordinate = player.getPosition();

        // Check if player cardinally adjacent to spawner
        if ((position.getX() + 1 == coordinate.getX() && position.getY() == coordinate.getY()) ||
            (position.getX() - 1 == coordinate.getX() && position.getY() == coordinate.getY()) ||
            (position.getX() == coordinate.getX() && position.getY() + 1 == coordinate.getY()) ||
            (position.getX() == coordinate.getX() && position.getY() - 1== coordinate.getY())) {
            return true;
        }

        // Check if player has a weapon in their inventory
        if (! player.getWeapons().isEmpty()) {
            return true;
        }

        return false;
    }
}
