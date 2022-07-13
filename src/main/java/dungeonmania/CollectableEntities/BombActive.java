package dungeonmania.CollectableEntities;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class BombActive implements BombState {
    protected Bomb bomb;
    protected Dungeon dungeon;
    Position position;

    public BombActive(Bomb bomb,Dungeon dungeon,Position position) {
        this.bomb = bomb;
        this.dungeon = dungeon;
        this.position = position;
    }
    public void explode() { //destroy the things around the bomb.
        dungeon.bombDestroy(position.getX(),position.getY()+1);
        dungeon.bombDestroy(position.getX()-1,position.getY()+1);
        dungeon.bombDestroy(position.getX()+1,position.getY()+1);
        dungeon.bombDestroy(position.getX()+1,position.getY());
        dungeon.bombDestroy(position.getX()-1,position.getY()+1);
        dungeon.bombDestroy(position.getX(),position.getY()-1);
        dungeon.bombDestroy(position.getX()+1,position.getY()-1);
        dungeon.bombDestroy(position.getX()-1,position.getY()-1);
        dungeon.removeEntity(bomb);
    }

    public void pickBomb() {
        dungeon.addEntity(bomb);
    }

    public void notPickUp() {
        
    }
}
