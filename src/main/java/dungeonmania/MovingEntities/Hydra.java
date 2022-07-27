package dungeonmania.MovingEntities;

import java.util.List;
import dungeonmania.Entity;
import dungeonmania.MovingEntities.PositionMovements.Movement;
import dungeonmania.MovingEntities.PositionMovements.RandomMovement;
import dungeonmania.MovingEntities.PositionMovements.RunAwayMovement;
import dungeonmania.util.Position;

public class Hydra extends MovingEntity {

    private String type;
    private boolean isInteractable;
    private Movement movement;
    private double healthIncreaseRate;
    private int healthIncreaseAmount;
    private boolean isAttack;

    /**
     * @param id
     * @param type
     * @param position
     * @param isInteractable
     * @param attack
     * @param health
     */
    public Hydra(String id, String type, Position position, boolean isInteractable, int attack, int health,
            int healthIncreaseRate, int healthIncreaseAmount, boolean isAttack) {
        super(id, attack, health, position);
        this.type = type;
        this.isInteractable = isInteractable;
        this.movement = new RandomMovement(this);
        this.healthIncreaseRate = healthIncreaseRate;
        this.healthIncreaseAmount = healthIncreaseAmount;
        this.isAttack = false;

    }

    // getters and setters
    public double getHealthIncreaseRate() {
        return healthIncreaseRate;
    }

    public void setHealthIncreaseRate(int healthIncreaseRate) {
        this.healthIncreaseRate = healthIncreaseRate;
    }

    public int getHealthIncreaseAmount() {
        return healthIncreaseAmount;
    }

    public void setHealthIncreaseAmount(int healthIncreaseAmount) {
        this.healthIncreaseAmount = healthIncreaseAmount;
    }

    @Override
    public double getHealth() {
        if (healthIncreaseRate < 0 || healthIncreaseRate > 1 || isAttack) {
        }
        else if (! isAttack) {
            health = health + healthIncreaseAmount;
        }
        return health;
    }

    /**
     * @param id
     * @param attack
     * @param health
     * @param position
     */
    public Hydra(String id, int attack, int health, Position position) {
        super(id, attack, health, position);
        this.isInteractable = false;
        this.type = "hydra";
        this.movement = new RandomMovement(this);
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
     * @param newMovement
     * @return void
     *         Changes the movement strategy of Hydra.
     */
    public void changeMovement(Movement newMovement) {
        this.movement = newMovement;
    }

    /**
     * 
     */
    @Override
    public void move(Position zombiePos, List<Entity> entities) {
        Position newPos = null;

        // Check if player is invicible
        if (isInvincible) {
            // run away
            changeMovement(new RunAwayMovement(this));
        }

        // Check if player is invisible
        else if (isInvisible) {
            // random move
            changeMovement(new RandomMovement(this));
        }

        else {
            changeMovement(new RandomMovement(this));
        }

        newPos = movement.moveEnemy(zombiePos, entities);

        if (newPos != null) {
            super.setPosition(newPos);
        }
    }
}
