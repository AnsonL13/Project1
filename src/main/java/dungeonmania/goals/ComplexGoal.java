package dungeonmania.goals;

public class ComplexGoal implements Goal {
    private Goal goalOne;
    private Goal goalTwo;
    private boolean and;

    public ComplexGoal (boolean isAnd, Goal goalOne, Goal goalTwo) {
        this.goalOne = goalOne;
        this.goalTwo = goalTwo;
        this.and = isAnd;
    }

    @Override
    public void progessGoal() {
        goalOne.progessGoal();
        goalTwo.progessGoal();
        
    }

    @Override
    public boolean isCompleted() {
        if (and) {
            return ((goalOne.isCompleted() && goalTwo.isCompleted()) ? true : false);
        } 
        return  ((goalOne.isCompleted() || goalTwo.isCompleted()) ? true : false);
        
    }

    @Override
    public boolean getSimpleName() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getString() {
        // TODO Auto-generated method stub
        return false;
    }

    
}
