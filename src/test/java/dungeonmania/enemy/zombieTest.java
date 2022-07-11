package dungeonmania.enemy;

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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class zombieTest {
    @Test
    @DisplayName("Test zombie is spawned")
    public void testZombieSpawned() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // check for zombie after tick
        int zombieCount = countEntityOfType(actualDungonRes, "zombie");
        assertEquals(1, zombieCount);

    }  
    
    @Test
    @DisplayName("Test zombiespawner is created")
    public void testZombieSpawer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");
        assertEquals(1, getEntities(initDungonRes, "zombie_toast_spawner").size());
    } 

    @Test
    @DisplayName("Test zombiespawner spawns no zombie")
    public void testZombieSpawerNone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_spiderTest_basicMovement");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // check for zombie after tick
        int zombieCount = countEntityOfType(actualDungonRes, "zombie");
        assertEquals(0, zombieCount);

    } 

    @Test
    @DisplayName("Test zombiespawner after 10 ticks")
    public void testZombieSpawerAfter10() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");

        // create the expected result
        initDungonRes = dmc.tick(Direction.RIGHT);
        // check for zombie after tick
        int zombieCount = countEntityOfType(initDungonRes, "zombie");
        assertEquals(0, zombieCount);

        for (int i = 0; i < 9; ++i) {
            initDungonRes = dmc.tick(Direction.LEFT);
            zombieCount = countEntityOfType(initDungonRes, "zombie");
            assertEquals(0, zombieCount);
    
        }
        initDungonRes = dmc.tick(Direction.RIGHT);
        zombieCount = countEntityOfType(initDungonRes, "zombie");
        assertEquals(1, zombieCount);


    } 

    @Test
    @DisplayName("Test zombiespawner spawn multiple zombies")
    public void testZombieSpawerMultiple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");
        
        // move player downward
        initDungonRes = dmc.tick(Direction.DOWN);
        int zombieCount = countEntityOfType(initDungonRes, "zombieToast");
        assertEquals(1, zombieCount);

        initDungonRes = dmc.tick(Direction.DOWN);
        zombieCount = countEntityOfType(initDungonRes, "zombieToast");
        assertEquals(2, zombieCount);

        initDungonRes = dmc.tick(Direction.DOWN);
        zombieCount = countEntityOfType(initDungonRes, "zombieToast");
        assertEquals(3, zombieCount);
    } 

}
