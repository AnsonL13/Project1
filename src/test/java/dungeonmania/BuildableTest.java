package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getInventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class BuildableTest {
    @Test
    @DisplayName("Test building a bow")
    public void testBuildBow() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_buildTest_bowMats", "c_collectableEntitiesTest");
        // Pickup the wood
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(1, getInventory(initDungonRes, "wood").size());

        // Pickup an arrow
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(1, getInventory(initDungonRes, "arrow").size());

        // Pickup an arrow
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(2, getInventory(initDungonRes, "arrow").size());

        // Pickup an arrow
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(3, getInventory(initDungonRes, "arrow").size());

        assertDoesNotThrow(() -> dmc.build("bow"));
        initDungonRes = dmc.getDungeonResponseModel();
        assertEquals(0, getInventory(initDungonRes, "wood").size());
        assertEquals(0, getInventory(initDungonRes, "arrow").size());
        assertEquals(1, getInventory(initDungonRes, "bow").size());
    }

    @Test
    @DisplayName("Test building a Shield with a key")
    public void testBuildShieldWithKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_buildShieldTest", "c_collectableEntitiesTest");
        // Pickup the wood
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(1, getInventory(initDungonRes, "wood").size());

        // Pickup the wood
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(2, getInventory(initDungonRes, "wood").size());

        // Pickup an key
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(1, getInventory(initDungonRes, "key").size());

        // Build the shield
        assertDoesNotThrow(() -> dmc.build("shield"));
        initDungonRes = dmc.getDungeonResponseModel();
        assertEquals(0, getInventory(initDungonRes, "wood").size());
        assertEquals(0, getInventory(initDungonRes, "key").size());
        assertEquals(1, getInventory(initDungonRes, "shield").size());
    }

    @Test
    @DisplayName("Test building a Shield with a treasure")
    public void testBuildShieldWithTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_buildShieldTest", "c_collectableEntitiesTest");
        // Pickup the wood
        initDungonRes = dmc.tick(Direction.LEFT);

        // Check wood in inventory
        assertEquals(1, getInventory(initDungonRes, "wood").size());

        // Pickup the wood
        initDungonRes = dmc.tick(Direction.LEFT);

        // Check wood in inventory
        assertEquals(2, getInventory(initDungonRes, "wood").size());

        // Pickup an treasure
        initDungonRes = dmc.tick(Direction.LEFT);

        // Check wood in inventory
        assertEquals(1, getInventory(initDungonRes, "treasure").size());

        // Build the shield
        assertDoesNotThrow(() -> dmc.build("shield"));
        initDungonRes = dmc.getDungeonResponseModel();
        assertEquals(0, getInventory(initDungonRes, "wood").size());
        assertEquals(0, getInventory(initDungonRes, "treasure").size());
        assertEquals(1, getInventory(initDungonRes, "shield").size());
    }

    @Test
    @DisplayName("Test not enough items to build bow or shield")
    public void testNotEnoughItems() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_buildShieldTest", "c_collectableEntitiesTest");

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        // Pickup the wood
        initDungonRes = dmc.tick(Direction.LEFT);

        // Check wood in inventory
        assertEquals(1, getInventory(initDungonRes, "wood").size());

        // Pickup the wood
        initDungonRes = dmc.tick(Direction.LEFT);

        // Check wood in inventory
        assertEquals(2, getInventory(initDungonRes, "wood").size());

        initDungonRes = dmc.tick(Direction.UP);

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));
    }

    @Test
    @DisplayName("Test throwing IllegalArgumentException when passing non buildable")
    public void testNonBuildable() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_buildShieldTest", "c_collectableEntitiesTest");

        assertThrows(IllegalArgumentException.class, () -> dmc.build("sword"));
        assertThrows(IllegalArgumentException.class, () -> dmc.build("key"));
    }

    @Test
    @DisplayName("Test cannot go through door after key gone")
    public void testCannotGoThroughDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_buildShieldTest", "c_collectableEntitiesTest");
        // Pickup the wood
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(1, getInventory(initDungonRes, "wood").size());

        // Pickup the wood
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(2, getInventory(initDungonRes, "wood").size());

        // Pickup an key
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check wood in inventory
        assertEquals(1, getInventory(initDungonRes, "key").size());

        // Build the shield
        assertDoesNotThrow(() -> dmc.build("shield"));
        initDungonRes = dmc.getDungeonResponseModel();
        assertEquals(0, getInventory(initDungonRes, "wood").size());
        assertEquals(0, getInventory(initDungonRes, "key").size());
        assertEquals(1, getInventory(initDungonRes, "shield").size());

        initDungonRes = dmc.tick(Direction.RIGHT);

        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 1), false);

        assertEquals(expectedPlayer, initPlayer);
    }
}