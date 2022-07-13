package dungeonmania.Goals;

import dungeonmania.Dungeon;
import dungeonmania.Item;

public class TreasureGoal implements Goal {
    private String name;
    private boolean isCompleted;
    private int treasureGoal;
    private Dungeon dungeon;

    public TreasureGoal(String name, boolean isCompleted, int treasureGoal, Dungeon dungeon) {
        this.name = name;
        this.isCompleted = isCompleted;
        this.treasureGoal = treasureGoal;
        this.dungeon = dungeon;
    }

    @Override
	public boolean goalComplete() {
        // Do logic to find out if required number of treasure is found
        int treasureCount = 0;
        for (Item item : dungeon.getPlayer().getInventory()) {
            if (item.getType().equals("treasure")) {
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

    // Getters and Setters below .... 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public int getTreasureGoal() {
        return treasureGoal;
    }
}
