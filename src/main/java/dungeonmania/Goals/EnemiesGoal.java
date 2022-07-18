package dungeonmania.Goals;

import dungeonmania.Dungeon;
import dungeonmania.StaticEntities.ZombieToastSpawner;

public class EnemiesGoal implements Goal {
    private String name;
    private int enemyGoal;
    private Dungeon dungeon;

    public EnemiesGoal(String name, int enemyGoal, Dungeon dungeon) {
        this.name = name;
        this.enemyGoal = enemyGoal;
        this.dungeon = dungeon;
    }

    /*
     * Finds out if required number of enemies have been destroyed and all spawners are destroyed. 
     */
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

    @Override
    public boolean canComplete() {
        return true;
    }
}
