package dungeonmania.BuildableEntities;

import dungeonmania.Weapon;

public class MidnightArmour implements Weapon{
    private String id;
    private String type;
    private boolean isInteractable;
    private int armourDefence;
    private int armourAttack;

    public MidnightArmour(String id, String type, boolean isInteractable, int armourDefence, int armourAttack) {
        this.id = id;
        this.type = type;
        this.isInteractable = isInteractable;
        this.armourDefence = armourDefence;
        this.armourAttack = armourAttack;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getId() {
        return id;
    }

    public final String getType() {
        return type;
    }

    public int getArmourDefence() {
        return armourDefence;
    }

    public int getArmourDurability() {
        return 1;
    }

    public void decreaseDurability() {

    }

    public double getAttackDamage() {
        return this.armourAttack;
    }

    public double getDefenceDamage() {
        return this.armourDefence;
    }

    public double getDurability() {
        return 1.0;
    }
    
}
