package dungeonmania.Goals;

import java.io.Serializable;

public interface Goal extends Serializable{
    public String nameString();
	public boolean goalComplete();
    public String listIncompleteGoals();
    public boolean add(Goal child);
	public boolean remove(Goal child);
	public boolean canComplete();

}