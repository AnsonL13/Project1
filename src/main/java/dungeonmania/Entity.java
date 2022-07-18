package dungeonmania;

import dungeonmania.util.Position;

public interface Entity {
    public boolean isInteractable();
    public String getId();
    public String getType();
    public Position getPosition();
    public void setPosition(Position position);
}
