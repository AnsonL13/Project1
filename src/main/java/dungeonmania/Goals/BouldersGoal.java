package dungeonmania.Goals;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entity;

public class BouldersGoal implements Goal {
    private String name;
    private Dungeon dungeon;

    public BouldersGoal(String name, Dungeon dungeon) {
        this.name = name;
        this.dungeon = dungeon;
    }

    @Override
	public boolean goalComplete() {
        // Do logic to find out if all boulders are correctly placed
        List<Entity> boulders = new ArrayList<Entity>(); 
        List<Entity> switches = new ArrayList<Entity>(); 
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType().equals("switch")) {
                switches.add(entity);
            }

            else if (entity.getType().equals("boulder")) {
                boulders.add(entity);
            }
        }
        
        for (Entity floorSwitch : switches) {
            boolean boulderChecker = false;
            for (Entity boulder : boulders) {
                if (floorSwitch.getPosition().getX() == boulder.getPosition().getX() && 
                    floorSwitch.getPosition().getY() == boulder.getPosition().getY()) {
                        boulderChecker = true;
                        break;
                }
            }

            if (!boulderChecker) {
                return false;
            }
        }

		return true;
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
