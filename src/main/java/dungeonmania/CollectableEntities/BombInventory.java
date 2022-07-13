package dungeonmania.CollectableEntities;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class BombInventory implements BombState {
    protected Bomb bomb;
    protected Dungeon dungeon;
    Position position;

    public BombInventory(Bomb bomb,Dungeon dungeon,Position position) {
        this.bomb = bomb;
        this.dungeon = dungeon;
        this.position = position;
    }
    public void explode() { 
        
    }

    public void pickBomb() {
        
    }

    public void notPickUp() {
        
    }
}
