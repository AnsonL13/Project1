package dungeonmania.Goals;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.util.Position;

public class ExitGoal implements Goal {
    private String name;
    private boolean isCompleted;
    private Dungeon dungeon;

    public ExitGoal(String name, boolean isCompleted, Dungeon dungeon) {
        this.name = name;
        this.isCompleted = isCompleted;
        this.dungeon = dungeon;
    }

    @Override
	public boolean goalComplete() {
        // Do logic to find out if player is at the exit
        Position playerPosition = dungeon.getPlayer().getPosition();
        
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType().equals("exit")) {
                Position exitPosition = entity.getPosition();
                if (playerPosition.getX() == exitPosition.getX() && playerPosition.getY() == exitPosition.getY()) {
                    return true;
                }
            }
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
}
