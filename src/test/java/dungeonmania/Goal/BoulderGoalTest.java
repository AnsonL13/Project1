package dungeonmania.Goal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import static dungeonmania.TestUtils.getGoals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class BoulderGoalTest { 
    @Test
    @DisplayName("Testing one switch goal")
    public void testBoulderGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderGoal_simple", "c_movementTest_testMovementDown");

        assertTrue(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":exit"));

        // move boulder
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }  
    
    @Test
    @DisplayName("Testing multiple switches goal")
    public void testMultipleBoulderGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderGoal_multiple", "c_movementTest_testMovementDown");

        assertTrue(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":exit"));

        // active
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":boulders"));

        // active
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":boulders"));

        // active
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }  

    @Test
    @DisplayName("Testing multiple switches and unprogress")
    public void testUnprogressBoulderGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderGoal_multiple", "c_movementTest_testMovementDown");

        assertTrue(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":exit"));

        // active
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":boulders"));

        // active
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":boulders"));

        // deactivate
        res = dmc.tick(Direction.DOWN);

        // active
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":boulders"));
    }

}
