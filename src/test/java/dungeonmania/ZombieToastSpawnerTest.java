package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;


public class ZombieToastSpawnerTest {
    @Test
    @DisplayName("Test interacting with zombie toast spawner with a sword")
    public void testZombieToastSpawnerInteract() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieSpawnerInteractTest", "c_zombieTest_simpleSpawn");
        EntityResponse initZombieToast = getEntities(initDungonRes, "zombie_toast_spawner").get(0);

        // move player downward
        initDungonRes = dmc.tick(Direction.DOWN);

        // Check sword in inventory
        assertEquals(1, getInventory(initDungonRes, "sword").size());

        // Go back to adjaceent square of zombie tast spawner
        initDungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(1, getInventory(initDungonRes, "sword").size());

        // Destroy the spawner
        assertDoesNotThrow(() -> dmc.interact(initZombieToast.getId()));
        initDungonRes = dmc.tick(Direction.UP);
        assertEquals(0, getEntities(initDungonRes, "zombie_toast_spawner").size());
    }

    @Test
    @DisplayName("Test interacting with zombie toast spawner while not cardinally adjacent")
    public void testZombieToastSpawnerInteractNotAdjacent() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieSpawnerInteractTest", "c_zombieTest_simpleSpawn");
        EntityResponse initZombieToast = getEntities(initDungonRes, "zombie_toast_spawner").get(0);

        // move player downward
        initDungonRes = dmc.tick(Direction.DOWN);

        // Check sword in inventory
        assertEquals(1, getInventory(initDungonRes, "sword").size());

        // Throw exception.
        assertThrows(InvalidActionException.class, () -> dmc.interact(initZombieToast.getId()));
        initDungonRes = dmc.tick(Direction.UP);
        assertEquals(1, getEntities(initDungonRes, "zombie_toast_spawner").size());
    }

    @Test
    @DisplayName("Test interacting with zombie toast spawner with no sword")
    public void testZombieToastSpawnerInteractNoSword() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieSpawnerInteractTest", "c_zombieTest_simpleSpawn");
        EntityResponse initZombieToast = getEntities(initDungonRes, "zombie_toast_spawner").get(0);

        assertEquals(0, getInventory(initDungonRes, "sword").size());

        // Throw exception.
        assertThrows(InvalidActionException.class, () -> dmc.interact(initZombieToast.getId()));
        initDungonRes = dmc.tick(Direction.UP);
        assertEquals(1, getEntities(initDungonRes, "zombie_toast_spawner").size());
    }
}
