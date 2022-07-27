package dungeonmania.CollectableEntities.BombStates;

public interface BombState {
    public void pickUp();
	public void putDown();
    public void explode();
    public void explode(boolean logic);
}
