package dungeonmania.MovingEntities;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.Battle;
import dungeonmania.Entity;
import dungeonmania.Item;
import dungeonmania.Player;
import dungeonmania.Round;
import dungeonmania.Weapon;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    private boolean isClockwise;
    private String type;
    private boolean isInteractable;
    private boolean movedUp;

    public Spider(String id, String type, Position position, boolean isInteractable, int attack, int health) {
        super(id, health, attack, position);
        this.type = type;
        this.isInteractable = isInteractable;
    }

    public Spider(String id, Position position, int attack, int health) {
        super(id, health, attack, position);
        this.type = "spider";
        this.isInteractable = false;
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    private void moveUpwards(Position position, List<Entity> entities) {
        position = super.getPosition();
        position.translateBy(Direction.UP);
        if (canMove(position, entities)) {
            super.setPos(position);
        }
    }

    @Override
    public boolean move(Position player, List<Entity> entities) {
        // When the spider spawns, they immediately move the 1 square upwards
        // Cannot tranverse boulders, reverse direction
        // Begin 'circling' their spawn spot 
        ArrayList<Position> coordinates = new ArrayList<Position>();
        Position position = null;
        Position newPos = super.getPosition();
        coordinates.add(newPos.translateBy(Direction.UP));
        coordinates.add(newPos.translateBy(Direction.RIGHT));
        coordinates.add(newPos.translateBy(Direction.DOWN));
        coordinates.add(newPos.translateBy(Direction.DOWN));
        coordinates.add(newPos.translateBy(Direction.LEFT));
        coordinates.add(newPos.translateBy(Direction.LEFT));
        coordinates.add(newPos.translateBy(Direction.UP));
        coordinates.add(newPos.translateBy(Direction.UP));
        coordinates.add(newPos.translateBy(Direction.RIGHT));
        if (movedUp == false) {
            moveUpwards(position, entities);
        } else if (isClockwise = true) {
            circling(coordinates, position, entities);
        } else { 
            reverseDirection(coordinates, position, entities);
        }
    
            
        return false;
    }


    private void circling(ArrayList<Position> coordinates, Position position, List<Entity> entities) {
        position = super.getPosition();
        //Direction direction;
        if (canMove(position, entities)) {
            for (int i = 0; i < coordinates.size(); i++) {
                super.setPos(coordinates.get(i));
            }
        }
        
    }

    private void reverseDirection(ArrayList<Position> coordinates, Position position,  List<Entity> entities) {
        position = super.getPosition();
        if (canMove(position, entities)) {
            for (int i = coordinates.size() - 1; i >= 0; i--) {
                super.setPos(coordinates.get(i));
            }
        }
        
    }

    private boolean canMove(Position position, List<Entity> entities) {
        if (position == null) {
            return false;
        }
        for (Entity entity : entities) {
            if (entity instanceof MovingEntity && entity.getPosition().equals(position)) {
                return false;
            }
            else if (entity instanceof Boulder && entity.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    public String getSimpleName() {
        return "spider";
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getType() {
        return type;
    }

    public Battle battleCalculate(Player player) {
        double playerHealth = player.getPlayerHealth();
        double playerAttack = player.getPlayerAttack();
        double playerBow = 1;
        double playerSword = 0;
        double playerShield = 0;
        double enemyHealth = super.getHealth();
        double enemyAttack = super.getAttack();

        // Get weapons. 
        List<Weapon> weaponryUsed = player.getPlayerWeapons();

        for (Weapon weapon : weaponryUsed) {
            if (weapon.getType().equals("bow")) {
                playerBow = 2;
            }

            if (weapon.getType().equals("sword")) {
                playerSword = weapon.getAttackDamage();
            }

            if (weapon.getType().equals("shield")) {
                playerShield = weapon.getDefenceDamage();
                if (playerShield > enemyAttack) playerShield = enemyAttack;
            }
        }

        // List of items used for every round. 
        List<Item> items = new ArrayList<Item>();

        // Add all weapons being used. 
        items.addAll(weaponryUsed);

        // If a player is using a potion, add it to the list of items.
        if (super.isInvicible() || super.isInvisible()) {
            items.add(player.currentPotion());
        }

        List<Round> rounds = new ArrayList<Round>();
        
        // Check if player is invinsible
        if (super.isInvicible()) {
            double deltaPlayerHealth = 0;
            double deltaEnemyHealth = - super.getHealth();
            super.setHealth(0);
            rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
        }


        else {
            playerAttack = playerAttack + playerSword;
            playerAttack *= playerBow;
            while (super.getHealth() > 0 || player.getPlayerHealth() > 0) {
                // Find change in health
                double deltaPlayerHealth = - ((enemyAttack - playerShield) / 10);
                double deltaEnemyHealth = - ((playerBow * (playerSword + playerAttack)) / 5);
    
                // Update spider health
                BigDecimal c = BigDecimal.valueOf(super.getHealth()).subtract(BigDecimal.valueOf(playerAttack / 5));
                super.setHealth(c.doubleValue());

                // Update player health
                c = BigDecimal.valueOf(player.getPlayerHealth()).subtract(BigDecimal.valueOf((enemyAttack - playerShield) / 10));
                player.setPlayerHealth(c.doubleValue());
    
                // Add round info to list
                rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
            }
        }

        Battle battle = new Battle(type, rounds, playerHealth, enemyHealth, super.getId());

        // Find the winner.
        // Spider won
        if (super.getHealth() > 0) {
            battle.setEnemyWon(true);
        }

        // Player won
        else if (player.getPlayerHealth() > 0) {
            battle.setPlayerWon(true);
        }

        return battle;
    }
    
}

