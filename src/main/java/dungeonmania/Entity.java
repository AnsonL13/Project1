package dungeonmania;

import dungeonmania.util.Position;

import java.io.Serializable;

public interface Entity extends Serializable {
    public boolean isInteractable();
    public String getId();
    public String getType();
    public Position getPosition();
    public void setPosition(Position position);
}
