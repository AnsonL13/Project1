package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

public class Player implements MovingEntity{
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    public int playerAttack;
    public int playerHealth;
    List<Item> inventory = new ArrayList<Item>();

    public Player(String id, String type, Position position, boolean isInteractable, int playerAttack, int playerHealth) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.playerAttack = playerAttack;
        this.playerHealth = playerHealth;
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

    public List<Item> getInventory() {
        return inventory;
    }
}
