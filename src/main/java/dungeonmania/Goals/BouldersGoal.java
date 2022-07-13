package dungeonmania.Goals;

public class BouldersGoal implements Goal {
    private String name;
    private boolean isCompleted;

    public BouldersGoal(String name, boolean isCompleted) {
        this.name = name;
        this.isCompleted = isCompleted;
    }

    @Override
	public boolean goalComplete() {
        // Do logic to find out if all boulders are correctly placed
        
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
}
