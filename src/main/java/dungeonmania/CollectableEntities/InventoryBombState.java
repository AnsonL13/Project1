package dungeonmania.CollectableEntities;

import dungeonmania.Dungeon;
import dungeonmania.Player;

public class InventoryBombState implements BombState {
    private Bomb bomb;
    private Dungeon dungeon;
    private Player player;
    
    public InventoryBombState(Bomb bomb, Dungeon dungeon, Player player) {
        this.bomb = bomb;
        this.dungeon = dungeon;
        this.player = player;
    }

    public void pickUp() {
        // Cannot pick up the bomb.
        return;
    }

	public void putDown() {
        bomb.setPosition(player.getPosition());
        // Remove from players inventory
        player.removeFromInventory(bomb.getId());
        // Put in list of entities in dungeon
        dungeon.addToEntities(bomb);
        // Change the state
        bomb.setState(bomb.getActiveBombState());
    }

    public void explode() {
        // Cannot explode the bomb.
        return;
    }
}
