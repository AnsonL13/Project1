package dungeonmania;

public interface Weapon extends Item {
    public double getAttackDamage();
    public double getDefenceDamage();
    public double getDurability();
    public void decreaseDurability();
    public String getType();
}
