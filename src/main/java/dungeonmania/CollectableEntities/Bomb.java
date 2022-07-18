package dungeonmania.CollectableEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Player;
import dungeonmania.CollectableEntities.BombStates.ActiveBombState;
import dungeonmania.CollectableEntities.BombStates.BombState;
import dungeonmania.CollectableEntities.BombStates.InactiveBombState;
import dungeonmania.CollectableEntities.BombStates.InventoryBombState;
import dungeonmania.util.Position;

public class Bomb implements CollectableEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int bombRadius;

    BombState inactiveBombState;
    BombState inventoryBombState;
    BombState activeBombState;

    BombState state;

    public Bomb(String id, String type, Position position, boolean isInteractable, int bombRadius, Dungeon dungeon, Player player) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
        this.bombRadius = bombRadius;
        
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

    public void setState(BombState state) {
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

    /*
     * Get the list of squares in the bombs range
     */
    public List<Position> getTargetSquares() {
        List<Position> squares = new ArrayList<>();
        int startingX = this.position.getX() - this.bombRadius;
        int startingY = this.position.getY() - this.bombRadius;
        int endingX = this.position.getX() + this.bombRadius;
        int endingY = this.position.getY() + this.bombRadius;
        for (int i = startingX; i <= endingX; i++) {
            for (int j = startingY; j <= endingY; j++) {
                squares.add(new Position(i, j));
            }
        }
        return squares;
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
