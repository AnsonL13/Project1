package dungeonmania.CollectableEntities;

public interface Potion extends CollectableEntity {
    public String getType();
    public int getDuration();
    public void decrementDuration();
}
