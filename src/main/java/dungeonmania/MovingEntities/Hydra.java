package dungeonmania.MovingEntities;

import java.util.List;
import java.util.Random;

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

    /**
     * @param id
     * @param type
     * @param position
     * @param isInteractable
     * @param attack
     * @param health
     */
    public Hydra(String id, String type, Position position, boolean isInteractable, int attack, int health,
            double healthIncreaseRate, int healthIncreaseAmount) {
        super(id, attack, health, position);
        this.type = "hydra";
        this.isInteractable = false;
        this.movement = new RandomMovement(this);
        this.healthIncreaseRate = healthIncreaseRate;
        this.healthIncreaseAmount = healthIncreaseAmount;
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
        return this.health;
    }

    /*
     * Checks whether the Hydras health will increase or not. 
     */
    public boolean isHealthIncrease() {
        // Increase rate is 0. Health will never increase. 
        if (this.getHealthIncreaseRate() == 0) {
            return false;
        }

        // Increase rate is 1. Health will always increase. 
        else if (this.getHealthIncreaseRate() == 1) {
            this.health += healthIncreaseAmount;
            return true;
        }

        // Calculate change of health increase based on healthIncreaseRate
        else {
            double randomValue = Math.random() * 100;
            boolean healthIncrease = randomValue <= (healthIncreaseRate * 100);

            if (healthIncrease) {
                this.health += healthIncreaseAmount;
                return true;
            }

            return false;
        }
    }

    public void setHealth(double health) {
        this.health = health;
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
     * @param hydraPos
     * @param entities
     * @return void
     *         Movement of Hydra.
     */
    @Override
    public void move(Position hydraPos, List<Entity> entities) {
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

        newPos = movement.moveEnemy(hydraPos, entities);

        if (newPos != null) {
            super.setPosition(newPos);
        }
    }
}

