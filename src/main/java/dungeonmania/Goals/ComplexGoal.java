package dungeonmania.Goals;

import java.util.ArrayList;

public class ComplexGoal implements Goal {
    private String name;

    ArrayList<Goal> children = new ArrayList<Goal>();

    public ComplexGoal(String name) {
        this.name = name;
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
		String answer = "[" + this.name  + " ("; 
		for(Goal c : children) {
			answer = answer + " " + c.nameString();
		}	
		answer = answer + ")]";
		return answer;
	}

    /*
     * Generates the list of incomplete goals in the required format
     */
    @Override
    public String listIncompleteGoals() {

        if (name.equals("OR") && ! goalComplete()) {
            String answer = "(" + children.get(0).listIncompleteGoals() 
                                + " " + this.name + " " 
                                + children.get(1).listIncompleteGoals() + ")";
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

                return "(" + exitString(0) + " " + this.name + " " + exitString(1) + ")";
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

    /*
     * Exit goal must be completed last. Check if goal can be completed. 
     */
    private boolean canComplete() {
        if (childString(0).contains("exit") && children.get(1).goalComplete()) {
            return true;
        } else if (childString(1).contains("exit") && children.get(0).goalComplete()) {
            return true;
        } else if (! childString(0).contains("exit") && ! childString(1).contains("exit")) {
            return true;
        }

        return false;
    }

    private String exitString(int index) {
        if (childString(index).equals("exit")) {
            return ":exit";
        }
        return children.get(index).listIncompleteGoals();
    }

    private String childString(int i) {
        return children.get(i).nameString();
    }


}
