package dungeonmania.Goals;

public class TreasureGoal implements Goal {
    private String name;
    private boolean isCompleted;
    private int treasureGoal;

    public TreasureGoal(String name, boolean isCompleted, int treasureGoal) {
        this.name = name;
        this.isCompleted = isCompleted;
        this.treasureGoal = treasureGoal;
    }

    @Override
	public boolean goalComplete() {
        // Do logic to find out if required number of treasure is found
        
        // Logic not complete. Return false for now. 
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
