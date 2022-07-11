package dungeonmania.goals;

public class TreasureGoal implements Goal {

    @Override
    public void progessGoal() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isCompleted() {
        // TODO Auto-generated method stub
        // dungeon.getCollectable entities == dungeon.getInventory
        return false;
    }

    @Override
    public String getSimpleName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getString() {
        // TODO Auto-generated method stub
        return "treasure";
    }
    
}
