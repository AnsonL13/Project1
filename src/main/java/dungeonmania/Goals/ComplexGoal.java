package dungeonmania.Goals;

import java.util.ArrayList;

public class ComplexGoal implements Goal {
    private String name;
    private boolean isCompleted;

    ArrayList<Goal> children = new ArrayList<Goal>();

    public ComplexGoal(String name, boolean isCompleted) {
        this.name = name;
        this.isCompleted = isCompleted;
    }

    // Find out if subgoals have been completed
    @Override
	public boolean goalComplete() {
		boolean hasChildrenCompleted = false;

        if (this.name.equals("AND")) {
            hasChildrenCompleted = true;
            for(Goal c : children) {
                hasChildrenCompleted = hasChildrenCompleted && c.goalComplete();
            }
        }
        
        if (this.name.equals("OR")) {
            hasChildrenCompleted = false;
            for(Goal c : children) {
                hasChildrenCompleted = hasChildrenCompleted || c.goalComplete();
            }
        }
		
		return hasChildrenCompleted;
	}
	
	@Override
	public String nameString() {
		String answer = "[" + this.getName()  + " ("; 
		for(Goal c : children) {
			answer = answer + " " + c.nameString();
		}	
		answer = answer + ")]";
		return answer;
	}

    @Override
    public String listIncompleteGoals() {

        if (name.equals("OR") && ! goalComplete()) {
            String answer = "(" + children.get(0).listIncompleteGoals() + " " + this.getName() + " " + children.get(1).listIncompleteGoals() + ")";
            return answer;
        }

        else if (name.equals("AND") && ! goalComplete()) {
            // Case where only one first goal complete.
            if (children.get(0).goalComplete() && canComplete()) {
                return children.get(1).listIncompleteGoals();
            }
            
            // Case where only second goal complete.
            else if (children.get(1).goalComplete() && canComplete()) {
                return children.get(0).listIncompleteGoals();
            }

            // Case where neither goal complete.
            else {
                return "(" + children.get(0).listIncompleteGoals() + " " + this.getName() + " " + children.get(1).listIncompleteGoals() + ")";
            }
        }

        return "";
    }

    public boolean add(Goal child) {
		children.add(child);
		return true;
	}

	public boolean remove(Goal child) {
		children.remove(child);
		return true;
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

    // Check if exit is completed before others
    public boolean canComplete() {
        if (nameString().equals("OR")) return true;

        if (children.get(0).nameString().contains("exit") && children.get(1).goalComplete()) {
            return true;
        } else if (children.get(1).nameString().contains("exit") && children.get(0).goalComplete()) {
            return true;
        }
        return false;
    }
}
