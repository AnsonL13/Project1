package dungeonmania.collectableEntites;

import dungeonmania.util.Position;

public class Sword extends CollectableEntites{
    private int durability;
    private int damage;
    public Sword(boolean isCollectable, Position position, int durability, int damage) {
        super(isCollectable, position);
        this.durability = durability;
        this.damage = damage;
    }
    public int getDurability() {
        return durability;
    }

    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    public void consumeSword() {
        this.durability = this.durability - 1;
    }
}
