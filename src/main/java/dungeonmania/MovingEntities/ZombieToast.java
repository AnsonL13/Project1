package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.Battle;
import dungeonmania.Enemy;
import dungeonmania.Entity;
import dungeonmania.Item;
import dungeonmania.Player;
import dungeonmania.Round;
import dungeonmania.Weapon;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity implements Enemy {
    private String type;
    private boolean isInteractable;

    //    private MovingPatterns;
    //    private MovingPatterns = new RunAwayMovement;
    //    private MovingPatterns = new RandomMovement;


    public ZombieToast(String id, String type, Position position, boolean isInteractable, int attack, int health) {
        super(id, health, attack, position);
        this.type = type;
        this.isInteractable = isInteractable;

    }

    public ZombieToast(String id, int attack, int health, Position position) {
        super(id, health, attack, position);

    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getType() {
        return type;
    }

    public boolean move(Position player, List<Entity> entities) {  
        Position newPos = null;
        super.setPotions();

        if (super.isInBattle()) {

        } else if (super.isInvicible()) {
            newPos = runAway(player);
        } else {
            newPos = randomMove();
        }

        if (canMove(newPos, entities)) {
            super.setPos(newPos);
        }  

        return super.isBattle(player);
    }
    
    private Position randomMove () {
        Random rand = new Random(); //instance of random class
        int upper = 4;
        int randomdir = rand.nextInt(upper); 
        Position pos = super.getPosition();
        switch(randomdir) {
            case 0:
                pos = pos.translateBy(Direction.UP);
                break;
            case 1:
                pos = pos.translateBy(Direction.LEFT);
                break;
            case 2:
                pos = pos.translateBy(Direction.DOWN);
                break;
            case 3:
                pos = pos.translateBy(Direction.RIGHT);
                break;

        }

        return pos;  

    }

    private boolean canMove(Position position, List<Entity> entities) {
        if (position == null) return false;
        for (Entity entity : entities) {
            if (entity instanceof MovingEntity && entity.getPosition().equals(position)) {
                return false;
            } else if (entity instanceof Boulder && entity.getPosition().equals(position)) {
                return false;
            } else if (entity instanceof Wall && entity.getPosition().equals(position)) {
                return false;
            } else if (entity instanceof Door && entity.getPosition().equals(position)) {
                return false;

            }
        }
        return true;
    }

    private Position runAway(Position player) {
        Position pos = super.getPosition();
        if (player.getX() > super.getPosition().getX()) {
            pos = pos.translateBy(Direction.LEFT);
        } else {
            pos = pos.translateBy(Direction.RIGHT);
        }
        return pos;
   /*     if (canMove(pos)) {
            super.setPos(pos);
            return;
        } else if (player.getY() > super.getPosition().getY()) {
            pos.translateBy(Direction.UP);
        } else {
            pos.translateBy(Direction.DOWN);
        }

        if (canMove(pos)) {
            super.setPos(pos);
        }*/
    }

    public String getSimpleName() {
        return "zombie";
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
        
        // Check if player is invincible
        if (super.isInvicible()) {
            double deltaPlayerHealth = 0;
            double deltaEnemyHealth = - super.getHealth();
            super.setHealth(0);
            rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
        }


        else {
            while (super.getHealth() > 0 && player.getPlayerHealth() > 0) {
                // Find change in health
                double deltaPlayerHealth = - ((enemyAttack - playerShield) / 10);
                double deltaEnemyHealth = - ((playerBow * (playerSword + playerAttack)) / 5);
    
                // Update zombie health
                super.setHealth(super.getHealth() - (int) ((playerBow * (playerSword + playerAttack)) / 5));
    
                // Update player health
                double health = player.getPlayerHealth() - ((enemyAttack - playerShield) / 10);
                player.setPlayerHealth(health);
    
                // Add round info to list
                rounds.add(new Round(deltaPlayerHealth, deltaEnemyHealth, items));
            }
        }

        Battle battle = new Battle(type, rounds, playerHealth, enemyHealth, super.getId());

        // Find the winner.
        // zombie won
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
