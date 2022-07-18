package dungeonmania;

public interface InteractableEntity extends Entity {
    public boolean interactActionCheck(Player player);
    public void interact(Dungeon dungeon);
}
