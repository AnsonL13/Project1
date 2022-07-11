package dungeonmania.goal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TreasureGoalTests {

    @Test
    @DisplayName("Treasure complete simple")
    public void testTresureSimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        assertTrue(getGoals(initDungonRes).contains(":treasure"));
        assertFalse(getGoals(initDungonRes).contains(":exit"));
        assertFalse(getGoals(initDungonRes).contains(":boulders"));
        assertFalse(getGoals(initDungonRes).contains(":enemies"));
        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals("", getGoals(initDungonRes));
    }

    @Test
    @DisplayName("Treasure complete multiple")
    public void testTreasureMultiple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
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
        assertTrue(getGoals(initDungonRes).contains(":treasure"));
        assertEquals("", getGoals(initDungonRes));
    }
    
}
