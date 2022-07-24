package dungeonmania.MovingEntities;

import java.util.List;
import java.util.Random;

import dungeonmania.util.Position;
import dungeonmania.Dungeon;
import dungeonmania.Entity;

public class Assassin extends Mercenary {
    private int reconRadius;
    private Double failRate;

    public Assassin(String id, String type, Position position, boolean isInteractable, 
            int allyAttack, int allyDefence,  int bribeAmount, int bribeRadius, 
            int Attack, int Health, int reconRadius, Double failRate) {
        super(id, position, allyAttack, allyDefence, bribeAmount, bribeRadius, Attack, Health);
        super.setType("assassin");
        this.reconRadius = reconRadius;
        this.failRate = failRate;
    }

    public Assassin(String id, Position position, int allyAttack, 
            int allyDefence, int bribeAmount, int bribeRadius,
            int Attack, int Health, int reconRadius, Double failRate) {
        super(id, position, allyAttack, allyDefence, bribeAmount, bribeRadius, Attack, Health);
        super.setType("assassin");
        this.reconRadius = reconRadius;
        this.failRate = failRate;

    }
    // TODO move for invisible potion
    //@Override
    //public void move(Position playerPos, List<Entity> entities) {}
    
    @Override 
    public void interact(Dungeon dungeon) {
        super.interact(dungeon);

        Random rand = new Random(); //instance of random class
        Double chance = rand.nextDouble(); // check if 0 to 1
        if (chance > failRate) return; // if success
        super.setAllied(false);
    }
}
