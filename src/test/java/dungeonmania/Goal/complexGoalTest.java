package dungeonmania.Goal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static dungeonmania.TestUtils.getGoals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.Goals.ComplexGoal;
import dungeonmania.Goals.Goal;
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

    @Test
    @DisplayName("Treasure and exit")
    public void testComplexGoalExitSecond() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("goals/d_complexGoals_exitSecond", "c_complexGoalsTest_andAll");
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.LEFT);

        assertEquals("", getGoals(initDungonRes));
    }

    @Test
    @DisplayName("Complex goal complete first or")
    public void testComplexGoalOrFirst() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("goals/d_complexGoals_orSecond", "c_complexGoalsTest_andAll");
        initDungonRes = dmc.tick(Direction.RIGHT);

        assertEquals("", getGoals(initDungonRes));
    }

    // White box testing
    @Test
    @DisplayName("Not and/or")
    public void testComplexGoalIncorrectName() {
        Goal goal = new ComplexGoal("OcO");
        assertEquals("", goal.listIncompleteGoals());

    }

}
