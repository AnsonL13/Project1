package dungeonmania.enemy;

import java.util.List;
import java.util.Random;

import dungeonmania.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy{

//    private MovingPatterns;
//    private MovingPatterns = new RunAwayMovement;
//    private MovingPatterns = new RandomMovement;



    public ZombieToast (int id, int health, int attack, Position position) {
        super(id, health, attack, position);
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
            if (entity instanceof Enemy && entity.getPosition().equals(position)) {
                return false;
            } else if (entity instanceof Enemy && entity.getPosition().equals(position)) {

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

}
