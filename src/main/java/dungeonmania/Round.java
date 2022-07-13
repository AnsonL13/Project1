package dungeonmania;

import java.util.List;

public class Round {
    private double deltaPlayerHealth;
    private double deltaEnemyHealth;
    private List<Item> weaponryUsed;

    public Round(double deltaPlayerHealth, double deltaEnemyHealth, List<Item> weaponryUsed) {
        this.deltaPlayerHealth = deltaPlayerHealth;
        this.deltaEnemyHealth = deltaEnemyHealth;
        this.weaponryUsed = weaponryUsed;
    }

    public double getDeltaCharacterHealth(){
        return deltaPlayerHealth;
    }
    
    public double getDeltaEnemyHealth(){
        return deltaEnemyHealth;
    }

    public List<Item> getWeaponryUsed() { return weaponryUsed; }
}
