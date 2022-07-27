package dungeonmania.CollectableEntities.BombStates;

import java.io.Serializable;

public interface BombState extends Serializable {
    public void pickUp();
	public void putDown();
    public void explode();
    public void explode(boolean logic);
}
