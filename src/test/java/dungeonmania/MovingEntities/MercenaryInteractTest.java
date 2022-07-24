package dungeonmania.MovingEntities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;

import static dungeonmania.TestUtils.countEntityOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.exceptions.InvalidActionException;


public class MercenaryInteractTest {
    @Test
    @DisplayName("Test mercenary interaction, bribe with one treasure")
    public void testBribingTheMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribeMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        initDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(initDungonRes, "treasure").size());

        initDungonRes = dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.UP);
        assertEquals(1, getInventory(initDungonRes, "treasure").size());
        String merceanryId = getEntities(initDungonRes, "mercenary").get(0).getId();

        initDungonRes = assertDoesNotThrow(() -> dmc.interact(merceanryId));
        assertEquals(0, getInventory(initDungonRes, "treasure").size());
        initDungonRes = dmc.tick(Direction.DOWN);

    }

    @Test
    @DisplayName("Test mercenary interaction, bribe with one treasure, but two in inventory")
    public void testBribingTheMercenary2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribeMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        initDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(initDungonRes, "treasure").size());

        initDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(initDungonRes, "treasure").size());

        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(2, getInventory(initDungonRes, "treasure").size());

        initDungonRes = dmc.tick(Direction.UP);
        assertEquals(2, getInventory(initDungonRes, "treasure").size());

        String merceanryId = getEntities(initDungonRes, "mercenary").get(0).getId();
        initDungonRes = assertDoesNotThrow(() -> dmc.interact(merceanryId));
        assertEquals(1, getInventory(initDungonRes, "treasure").size());
    }

    @Test
    @DisplayName("Test mercenary interaction, bribe with not treasure in inventory")
    public void testBribingTheMercenary3() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribeMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(0, getInventory(initDungonRes, "treasure").size());

        initDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(0, getInventory(initDungonRes, "treasure").size());

        initDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(0, getInventory(initDungonRes, "treasure").size());

        initDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(0, getInventory(initDungonRes, "treasure").size());

        String merceanryId = getEntities(initDungonRes, "mercenary").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(merceanryId));
        assertEquals(0, getInventory(initDungonRes, "treasure").size());
    }

    @Test
    @DisplayName("Test mercenary interaction, bribe but out of range")
    public void testBribingTheMercenary4() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribeMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        String merceanryId = getEntities(initDungonRes, "mercenary").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(merceanryId));
        assertEquals(0, getInventory(initDungonRes, "treasure").size());
    }

    @Test
    @DisplayName("Test mercenary interaction, bribe with one sceptre")
    public void testBribingTheMercenaryWithSceptre() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribeMercenaryWithSceptre", "c_buildTests_M3");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        initDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(initDungonRes, "treasure").size());

        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(initDungonRes, "sun_stone").size());
        assertEquals(1, getInventory(initDungonRes, "wood").size());
        assertEquals(2, getInventory(initDungonRes, "treasure").size());

        assertDoesNotThrow(() -> dmc.build("sceptre"));
        initDungonRes = dmc.getDungeonResponseModel();
        assertEquals(1, getInventory(initDungonRes, "sceptre").size());

        String merceanryId = getEntities(initDungonRes, "mercenary").get(0).getId();

        initDungonRes = assertDoesNotThrow(() -> dmc.interact(merceanryId));
        assertEquals(1, getInventory(initDungonRes, "treasure").size());
        initDungonRes = dmc.tick(Direction.DOWN);
    }

}
