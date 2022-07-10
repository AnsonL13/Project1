package dungeonmania.collectableEntites;

import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntites{
    private int durability;

    public InvincibilityPotion(boolean isCollectable, Position position, int durability) {
        super(isCollectable, position);
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }
    
    public void consumeInvincibilityPotion() {
        this.durability = this.durability - 1;
    }
    
}
