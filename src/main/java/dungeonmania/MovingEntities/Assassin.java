package dungeonmania.MovingEntities;

import java.util.List;
import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.util.Position;

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
    @Override
    public void move(Position playerPos, List<Entity> entities) {
        boolean changedPotionStat = false;
        if (super.isInvisible() && canSeePlayer(playerPos)) {
            super.setPotionStatus(false, super.isInvincible());
            changedPotionStat = true;
        }

        super.move(playerPos, entities);
        if (changedPotionStat) super.setPotionStatus(true, super.isInvincible());
    }
    
    @Override 
    public void interact(Dungeon dungeon) {
        super.interact(dungeon);

        Random rand = new Random(); //instance of random class
        Double chance = rand.nextDouble(); // check if 0 to 1
        if (chance <= failRate) { // fail
            super.setAllied(false);
        }
    }


    private boolean canSeePlayer(Position player) {
        int xTopBoundary = super.getPosition().getX() + reconRadius;
        int xBottomBoundary = super.getPosition().getX() - reconRadius;
        int yTopBoundary = super.getPosition().getY() + reconRadius;
        int yBottomBoundary = super.getPosition().getY() - reconRadius;

        // Check if player is radius of invalid invisible
        if (player.getX() >= xBottomBoundary && player.getX() <= xTopBoundary &&
            player.getY() >= yBottomBoundary && player.getY() <= yTopBoundary) {
                return true;
        }

        return false;
    }
}