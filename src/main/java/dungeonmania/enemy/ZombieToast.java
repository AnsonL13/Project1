package dungeonmania.enemy;

import java.util.Random;

public class ZombieToast extends Enemy{

    public ZombieToast (int health, int attack) {
        super(health, attack);
    }

    public void move(boolean isInvicible, boolean isInvisible, int x, int y) {  
        if (super.isInBattle()) {

        } else if (isInvicible) {

        } else {
            randomMove();
        }

        super.isBattle(x, y);
    }
    
    private void randomMove () {
        Random rand = new Random(); //instance of random class
        int upper = 4;
        int randomdir = rand.nextInt(upper); 
        int x = super.getX();
        int y = super.getY();
        switch(randomdir) {
            case 0:
                x += 1;
                break;
            case 1:
                y += 1;
                break;
            case 2:
                x -= 1;
                break;
            case 3:
                y -=1;
                break;
          }
        if (canMove(x, y)) {
            super.setX(x);
            super.setY(y);
        }  
    }

    private boolean canMove(int x, int y) {
        return true;
    }

    private void runAway() {
        
    }

}
