package dungeonmania;

import dungeonmania.util.Position;

public interface Entity {
    public boolean isInteractable();
    public int getId();
    public String getType();
    public Position getPosition();
}

