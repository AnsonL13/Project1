package dungeonmania.CollectableEntities;

import dungeonmania.Dungeon;
import dungeonmania.Player;

public class InactiveBombState implements BombState {
    private Bomb bomb;
    private Dungeon dungeon;
    private Player player;

    public InactiveBombState(Bomb bomb, Dungeon dungeon, Player player) {
        this.bomb = bomb;
        this.dungeon = dungeon;
        this.player = player;
    }

    public void pickUp() {
        // Remove bomb from dungeon
        dungeon.removeEntity(bomb.getId());
        // Place into players inventory
        player.addToInventory(bomb);
        // Change the state
        bomb.setState(bomb.getInventoryBombState());
    }

	public void putDown() {
        // Cannot put the bomb down.
        return;
    }

    public void explode() {
        // Cannot explode the bomb.
        return;
    }
}
