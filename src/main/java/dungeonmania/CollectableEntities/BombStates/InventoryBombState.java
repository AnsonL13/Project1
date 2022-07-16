package dungeonmania.CollectableEntities.BombStates;

import dungeonmania.Dungeon;
import dungeonmania.Player;
import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.util.Position;

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
        // Set the new bomb position
        bomb.setPosition(new Position(player.getPosition().getX(), player.getPosition().getY()));
        // Remove from players inventory
        player.removeFromInventory(bomb.getId());
        // Put bomb in the list of entities in dungeon
        dungeon.addToEntities(bomb);
        // Change the state
        bomb.setState(bomb.getActiveBombState());
    }

    public void explode() {
        // Cannot explode the bomb.
        return;
    }
}
