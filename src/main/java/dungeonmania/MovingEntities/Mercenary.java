package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

public class Mercenary implements MovingEntity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    private int allyAttack;
    private int allyDefence;
    private int bribeAmount;
    private int bribeRadius;
    private int mercenaryAttack;
    private int mercenaryHealth;

    public Mercenary(String id, String type, Position position, boolean isInteractable, 
        int allyAttack, int allyDefence,  int bribeAmount, int bribeRadius, int mercenaryAttack, int mercenaryHealth) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;

        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        
        this.mercenaryAttack = mercenaryAttack;
        this.mercenaryHealth = mercenaryHealth;
    }

    public int getMercenaryAttack() {
        return mercenaryAttack;
    }

    public int getMercenaryHealth() {
        return mercenaryHealth;
    }

    public int getAllyAttack() {
        return allyAttack;
    }

    public int getAllyDefence() {
        return allyDefence;
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

    public final Position getPosition() {
        return position;
    }

    public int getBribeAmount() {
        return bribeAmount;
    }

    public int getBribeRadius() {
        return bribeRadius;
    }
}