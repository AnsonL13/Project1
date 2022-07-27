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

    /*
     * Pickup the bomb from the dungeon
     */
    public void pickUp() {
        // Cannot pick up the bomb.
        return;
    }

    /*
     * Put down the bomb on the dungeon map
     */
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

    /*
     * Destroy all entities with range of the bomb
     */
    public void explode() {
        // Cannot explode the bomb.
        return;
    }

    /*
     * Destroy all entities with range of the bomb
     */
    public void explode(boolean logic) {
        // Cannot explode the bomb.
        return;
    }
}
