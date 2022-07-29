package dungeonmania.Goals;

import dungeonmania.Dungeon;
import dungeonmania.Item;

public class TreasureGoal implements Goal {
    private String name;
    private int treasureGoal;
    private Dungeon dungeon;

    public TreasureGoal(String name, int treasureGoal, Dungeon dungeon) {
        this.name = name;
        this.treasureGoal = treasureGoal;
        this.dungeon = dungeon;
    }

    /*
     * Check if required number of treasure has been found
     */
    @Override
	public boolean goalComplete() {
        // Do logic to find out if required number of treasure is found
        int treasureCount = 0;
        for (Item item : dungeon.getPlayer().getInventory()) {
            if (item.getType().equals("treasure")|| item.getType().equals("sun_stone")) {
                treasureCount++;
            }
        }

        if (treasureCount >= treasureGoal) {
            return true;
        }
		return false;
	}
	
	@Override
	public String nameString() {
		return this.name;
	}

    @Override
    public String listIncompleteGoals() {
        if (! goalComplete()) {
            return ":" + this.name;
        }
        return "";
    }

    public boolean add(Goal child) {
		return false;
	}

	public boolean remove(Goal child) {
		return false;
	}

}
