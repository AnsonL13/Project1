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

public class ExitGoalTest {

    @Test
    @DisplayName("Exit complete")
    public void testMovementDown() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        assertTrue(getGoals(initDungonRes).contains(":exit"));
        assertFalse(getGoals(initDungonRes).contains(":treasure"));
        assertFalse(getGoals(initDungonRes).contains(":boulders"));
        assertFalse(getGoals(initDungonRes).contains(":enemies"));
        dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals("", getGoals(initDungonRes));
    }
    
}

