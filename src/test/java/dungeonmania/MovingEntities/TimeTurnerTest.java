package dungeonmania.MovingEntities;

import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.countEntityOfType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TimeTurnerTest {
    @Test
    @DisplayName("Test time turner collected")
    public void testTimeTurnerCollected() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("TimeTurner/d_timeTurner_simpleSpawn", "c_zombieTest_simpleSpawn");

        // check timerTurner created
        assertEquals(1, countEntityOfType(initDungonRes, "time_turner"));

        // pick up timeTurner
        initDungonRes = dmc.tick(Direction.RIGHT);

        // check inventory
        assertEquals(1, getInventory(initDungonRes, "time_turner").size());

    }

    @Test
    @DisplayName("Test invalid time travel")
    public void testExceptionInvalidTime() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("TimeTurner/d_timeTurner_simpleSpawn", "c_zombieTest_simpleSpawn");

        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(1));
        
        // check timerTurner created
        assertEquals(1, countEntityOfType(initDungonRes, "time_turner"));

        // pick up timeTurner
        initDungonRes = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.rewind(1));

        // check inventory
        assertEquals(1, getInventory(initDungonRes, "time_turner").size());

    }
    
}
