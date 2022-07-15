package dungeonmania.MovingEntities;

import dungeonmania.util.Position;
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
    private RunAwayMovement runAwayMovement = new RunAwayMovement(this);
    private RandomMovement randomMovement = new RandomMovement(this);
    private FollowMovement followMovement = new FollowMovement(this);

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
        this.movement = followMovement;
        
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
        this.movement = followMovement;
                
    }
    
    /** 
     * @param player
     * @param entities
     * @return boolean
     */
    @Override
    public boolean move(Position player, List<Entity> entities) {
        Position newPos = movement.moveEnemy(player, entities);
        if (newPos != null) {
            super.setPosition(newPos);
        }  
        
        setPotions();
        return super.isBattle(player);
    }
    
    /** 
     * @param duration
     */
    @Override
    public void setInvincible(int duration) {
        super.setInvincible(duration);
        movement = runAwayMovement;
    }
    
    /** 
     * @param duration
     */
    @Override
    public void setInvisible(int duration) {
        super.setInvisible(duration);
        movement = randomMovement;
    }

    @Override
    public void setPotions() {
        super.setPotions();
        if (super.isInvisible() != true && super.isInvicible() != true) {
            movement = followMovement;
        }
    }
    
    /** 
     * @return String
     */
    public String getSimpleName() {
        return "mercenary";
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
                return true;
        }

        // Check if player has enough gold
        if (player.treasureAmount() < bribeAmount) {
            return false;
        }

        return true;
    }
}

