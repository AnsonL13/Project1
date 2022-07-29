package dungeonmania.Goal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TreasureGoalTest {

    @Test
    @DisplayName("Treasure complete simple")
    public void testTresureSimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_treasureTest_simple", "c_movementTest_testMovementDown");
        assertTrue(getGoals(initDungonRes).contains(":treasure"));
        assertFalse(getGoals(initDungonRes).contains(":exit"));
        assertFalse(getGoals(initDungonRes).contains(":boulders"));
        assertFalse(getGoals(initDungonRes).contains(":enemies"));
        initDungonRes = dmc.tick(Direction.UP);
        assertEquals("", getGoals(initDungonRes));
    }

    @Test
    @DisplayName("Treasure complete multiple")
    public void testTreasureMultiple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_treasureTest_multiple", "c_treasureTest_multiple");
        assertTrue(getGoals(initDungonRes).contains(":treasure"));
        assertFalse(getGoals(initDungonRes).contains(":exit"));
        assertFalse(getGoals(initDungonRes).contains(":boulders"));
        assertFalse(getGoals(initDungonRes).contains(":enemies"));

        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(initDungonRes, "treasure").size());
        assertTrue(getGoals(initDungonRes).contains(":treasure"));

        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(2, getInventory(initDungonRes, "treasure").size());
        assertTrue(getGoals(initDungonRes).contains(":treasure"));

        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(3, getInventory(initDungonRes, "treasure").size());
        assertFalse(getGoals(initDungonRes).contains(":treasure"));
        assertEquals("", getGoals(initDungonRes));
    }

    @Test
    @DisplayName("Treasure complete with sunstone")
    public void testTresureSimpleSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("goals/d_treasureTest_simpleStone", "c_movementTest_testMovementDown");
        assertTrue(getGoals(initDungonRes).contains(":treasure"));
        assertFalse(getGoals(initDungonRes).contains(":exit"));
        assertFalse(getGoals(initDungonRes).contains(":boulders"));
        assertFalse(getGoals(initDungonRes).contains(":enemies"));
        initDungonRes = dmc.tick(Direction.DOWN);

        assertEquals(1, getInventory(initDungonRes, "sun_stone").size());
        assertEquals("", getGoals(initDungonRes));
    }

    @Test
    @DisplayName("Treasure complete with sunstione")
    public void testTresureSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("goals/d_treasureTest_simpleStone", "c_treasureTest_multiple");
        assertTrue(getGoals(initDungonRes).contains(":treasure"));
        assertFalse(getGoals(initDungonRes).contains(":exit"));
        assertFalse(getGoals(initDungonRes).contains(":boulders"));
        assertFalse(getGoals(initDungonRes).contains(":enemies"));

        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(initDungonRes, "sun_stone").size());
        assertTrue(getGoals(initDungonRes).contains(":treasure"));

        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(initDungonRes, "treasure").size());
        assertTrue(getGoals(initDungonRes).contains(":treasure"));

        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(2, getInventory(initDungonRes, "treasure").size());
        assertFalse(getGoals(initDungonRes).contains(":treasure"));
        assertEquals("", getGoals(initDungonRes));
    }
    
}
