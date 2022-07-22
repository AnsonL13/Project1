package dungeonmania.MovingEntities;

import dungeonmania.util.Position;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.InteractableEntity;
import dungeonmania.Player;
import dungeonmania.MovingEntities.PositionMovements.FollowMovement;
import dungeonmania.MovingEntities.PositionMovements.Movement;
import dungeonmania.MovingEntities.PositionMovements.RandomMovement;
import dungeonmania.MovingEntities.PositionMovements.RunAwayMovement;

import java.util.List;

public class Mercenary extends MovingEntity implements InteractableEntity {
    private String type;
    private boolean isInteractable;
    private boolean isAllied;

    private int allyAttack;
    private int allyDefence;
    private int bribeAmount;
    private int bribeRadius;

    private Movement movement;

    /**
     * Constructor
     * @param id
     * @param type string name
     * @param position position in game
     * @param isInteractable if player can interact
     * @param allyAttack
     * @param allyDefence
     * @param bribeAmount
     * @param bribeRadius
     * @param mercenaryAttack
     * @param mercenaryHealth
     */
    public Mercenary(String id, String type, Position position, boolean isInteractable, 
            int allyAttack, int allyDefence,  int bribeAmount, int bribeRadius, 
            int mercenaryAttack, int mercenaryHealth) {
        super(id, mercenaryAttack, mercenaryHealth, position);
        this.type = type;
        this.isInteractable = isInteractable;
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.movement = new FollowMovement(this);
        this.isAllied = false;
    }

    /**
     * Constructor
     * @param id
     * @param position
     * @param allyAttack
     * @param allyDefence
     * @param bribeAmount
     * @param bribeRadius
     * @param mercenaryAttack
     * @param mercenaryHealth
     */
    public Mercenary(String id, Position position, int allyAttack,
            int allyDefence,  int bribeAmount, int bribeRadius, 
            int mercenaryAttack, int mercenaryHealth) {
        super(id, mercenaryAttack, mercenaryHealth, position);
        this.type = "mercenary";
        this.isInteractable = false;
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.movement = new FollowMovement(this);
        this.isAllied = false;
    }
    
    /** 
     * @param player
     * @param entities
     * @return boolean
     */
    @Override
    public void move(Position playerPos, List<Entity> entities) {
        decrementStuckTimer();
        if (this.stuckTimer > 0) return;

        Position newPos = null;

        // Check if mercenary is allied
        if (isAllied) {
            this.movement = new FollowMovement(this);
        }

        else {
            // Check if player is Invincible
            if (isInvincible) {
                changeMovement(new RunAwayMovement(this));
            }

            // Check if player is Invisible
            else if (isInvisible) {
                changeMovement(new RandomMovement(this));
            }

            else {
                changeMovement(new FollowMovement(this));
            }
        }

        newPos = movement.moveEnemy(playerPos, entities);

        if (newPos != null) {
            super.setPosition(newPos, entities);
        }
    }

    /** 
     * @param newMovement
     * @return void
     * Changes the movement strategy of the zombie.
     */
    public void changeMovement(Movement newMovement) {
        this.movement = newMovement;
    }
    
    /** 
     * @return int
     */
    public int getAllyAttack() {
        return allyAttack;
    }

    /** 
     * @return int
     */
    public int getAllyDefence() {
        return allyDefence;
    }
    
    /** 
     * @return boolean
     */
    @Override
    public boolean isInteractable() {
        return isInteractable;
    }
    
    /** 
     * @return String
     */
    @Override
    public final String getType() {
        return type;
    }

    /** 
     * @return int
     */
    public int getBribeAmount() {
        return bribeAmount;
    }
    
    /** 
     * @return int
     */
    public int getBribeRadius() {
        return bribeRadius;
    }
    
    /** 
     * @param player
     * @return boolean
     */
    @Override
    public boolean interactActionCheck(Player player) {
        int xTopBoundary = super.getPosition().getX() + bribeRadius;
        int xBottomBoundary = super.getPosition().getX() - bribeRadius;
        int yTopBoundary = super.getPosition().getY() + bribeRadius;
        int yBottomBoundary = super.getPosition().getY() - bribeRadius;
        // Check if player is within the specified bribing radius
        if ((player.getPosition().getX() >= xBottomBoundary && player.getPosition().getX() <= xTopBoundary) &&
            (player.getPosition().getY() >= yBottomBoundary && player.getPosition().getY() <= yTopBoundary)) {
                // Check if player has enough gold
                if (player.treasureAmount() >= bribeAmount) {
                    // Player can interact with the mercenary. 
                    return true;
                }
        }

        return false;
    }

    /*
     * Let the player interact with the mercenary. 
     */
    public void interact(Dungeon dungeon) {
        this.isAllied = true;

        // Remove player treasure cost
        dungeon.getPlayer().removeTreasure(bribeAmount);
    }

    /*
     * Check if the mercenary is allied or not
     */
    public boolean isAllied() {
        return isAllied;
    }
}

