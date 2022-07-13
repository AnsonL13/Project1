package dungeonmania;

import dungeonmania.util.Position;

public interface Enemy {
    public Position getPosition();
    public String getType();
    public Battle battleCalculate(Player player);

}
