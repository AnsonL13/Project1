package dungeonmania.Goals;

public interface Goal {
    public String nameString();
	public boolean goalComplete();
    public String listIncompleteGoals();
    public boolean add(Goal child);
	public boolean remove(Goal child);
}