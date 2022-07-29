package dungeonmania.MovingEntities;

import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class TimeTurnerTest {
    @Test
    @DisplayName("Test zombie is spawned")
    public void testZombieSpawned() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_timeTurner_simpleSpawn", "c_zombieTest_simpleSpawn");

        // check timerTurner created
        assertEquals(1, getEntities(initDungonRes, "time_turner"));

        // pick up timeTurner
        dmc.tick(Direction.RIGHT);

        // check inventory
        assertEquals(1, getInventory(initDungonRes, "time_turner"));

    }
    
}
