package dungeonmania.collectableEntites;

import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntites{

    private int durability;

    public InvisibilityPotion(boolean isCollectable, Position position, int durability) {
        super(isCollectable, position);
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }
    
    public void consumeInvisibilityPotion() {
        this.durability = this.durability - 1;
    }
    
}
