package dungeonmania.Goals;

import dungeonmania.Dungeon;
import dungeonmania.StaticEntities.ZombieToastSpawner;

public class EnemiesGoal implements Goal {
    private String name;
    private boolean isCompleted;
    private int enemyGoal;
    private Dungeon dungeon;

    public EnemiesGoal(String name, boolean isCompleted, int enemyGoal, Dungeon dungeon) {
        this.name = name;
        this.isCompleted = isCompleted;
        this.enemyGoal = enemyGoal;
        this.dungeon = dungeon;
    }

    @Override
	public boolean goalComplete() {
        // Do logic to find out if required number of enemies have been destroyed. 
        if (dungeon.getBattles().size() >= enemyGoal && ! ifSpawners()) {
            return true;
        }
        
		return false;
	}

    private boolean ifSpawners() {
        Boolean ifContain = dungeon.getEntities()
            .stream().anyMatch(o -> o instanceof ZombieToastSpawner);
        return ifContain;
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

    public int getEnemyGoal() {
        return enemyGoal;
    }

    @Override
    public boolean canComplete() {
        // TODO Auto-generated method stub
        return true;
    }
}
