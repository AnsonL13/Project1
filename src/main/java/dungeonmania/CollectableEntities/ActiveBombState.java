package dungeonmania.CollectableEntities;

import dungeonmania.Dungeon;
import dungeonmania.Player;
import dungeonmania.util.Position;

public class ActiveBombState implements BombState {
    private Bomb bomb;
    private Dungeon dungeon;
    private Player player;

    public ActiveBombState(Bomb bomb, Dungeon dungeon, Player player) {
        this.bomb = bomb;
        this.dungeon = dungeon;
        this.player = player;
    }

    public void pickUp() {
        // Cannot pickup the bomb
        return;
    }

	public void putDown() {
        // Cannot putdown the bomb
        return;
    }

    public void explode() {
        // Explode the bomb, remove everything with its radius
        
        // Remove from Dungeon
        dungeon.removeEntity(bomb.getId());
    }
}
