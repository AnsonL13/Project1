package dungeonmania.MovingEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public interface AlliedEntities {
    public void move(Position playerPos, List<Entity> entities);
    public int getAllyAttack();
    public int getAllyDefence();
    public int getBribeTime();
    public void setAllied(boolean isAllied);
    public void setBribeTime(int time);
}
