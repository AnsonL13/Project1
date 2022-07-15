package dungeonmania.CollectableEntities;

import dungeonmania.Dungeon;
import dungeonmania.Player;
import dungeonmania.util.Position;

public class Bomb implements CollectableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int bombRadius;
    private Dungeon dungeon;
    private Player player;

    BombState inactiveBombState;
    BombState inventoryBombState;
    BombState activeBombState;

    BombState state = inactiveBombState;

    public Bomb(String id, String type, Position position, boolean isInteractable, int bombRadius, Dungeon dungeon, Player player) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.bombRadius = bombRadius;
        
        this.dungeon = dungeon;
        this.player = player;
        inactiveBombState = new InactiveBombState(this, dungeon, player);
        inventoryBombState = new InventoryBombState(this, dungeon, player);
        activeBombState = new ActiveBombState(this, dungeon, player);
        state = inactiveBombState;
    }

    // State pattern functionality
    public void pickUp() {
        state.pickUp();
    }

	public void putDown() {
        state.putDown();
    }

    public void explode() {
        state.explode();
    }

    void setState(BombState state) {
		this.state = state;
	}

    public BombState getInactiveBombState() {
        return inactiveBombState;
    }

    public BombState getInventoryBombState() {
        return inventoryBombState;
    }

    public BombState getActiveBombState() {
        return activeBombState;
    }

    // Getters
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

    public int getBombRadius() {
        return bombRadius;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
