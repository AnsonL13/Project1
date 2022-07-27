package dungeonmania.Goal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static dungeonmania.TestUtils.getGoals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;


public class complexGoalTest {
    @Test
    @DisplayName("Boulber and treasure not exit")
    public void testComplexGoal1() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_complexgoal1", "c_complexGoalsTest_andAll");
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.DOWN);

        assertEquals("", getGoals(initDungonRes));
    }

    @Test
    @DisplayName("Boulber and treasure and exit")
    public void testComplexGoal2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_complexgoal2", "c_complexGoalsTest_andAll");
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.DOWN);

        assertEquals("", getGoals(initDungonRes));
    }

    @Test
    @DisplayName("Boulber and treasure and exit")
    public void testComplexGoalNoAND() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("goals/d_complexGoals_treasureboulder", "c_complexGoalsTest_andAll");
        
        // Checking correct string returns
        assertTrue(getGoals(initDungonRes).contains("AND"));
        initDungonRes = dmc.tick(Direction.RIGHT);

        assertFalse(getGoals(initDungonRes).contains("AND"));
        initDungonRes = dmc.tick(Direction.RIGHT);


        assertEquals("", getGoals(initDungonRes));
    }
}