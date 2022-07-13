package dungeonmania.enemy;

import java.util.Random;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy{

    public ZombieToast (int health, int attack, Position position) {
        super(health, attack, position);
    }

    public void move(boolean isInvicible, boolean isInvisible, Position player) {  
        if (super.isInBattle()) {

        } else if (isInvicible) {
            runAway();
        } else {
            randomMove();
        }

        super.isBattle(player);
    }
    
    private void randomMove () {
        Random rand = new Random(); //instance of random class
        int upper = 4;
        int randomdir = rand.nextInt(upper); 
        Position pos = super.getPos();
        switch(randomdir) {
            case 0:
                pos.translateBy(Direction.UP);
                break;
            case 1:
                pos.translateBy(Direction.LEFT);
                break;
            case 2:
                pos.translateBy(Direction.DOWN);
                break;
            case 3:
                pos.translateBy(Direction.RIGHT);
                break;
          }
        if (canMove(pos)) {
            super.setPos(pos);
        }  
    }

    private boolean canMove(Position position) {
        return true;
    }

    private void runAway() {
        
    }

    public String getSimpleName() {
        return "zombie";
    }

}
