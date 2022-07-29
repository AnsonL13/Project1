package dungeonmania.LogicalEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwitchDoorTest {
    @Test
    @DisplayName("Test switch doors and or xor co_and")
    public void testSwitchDoors() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("LogicalEntities/d_switchDoors", "c_logicalEntities");

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);

        EntityResponse player = getPlayer(res).get();
        EntityResponse expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(1, 1), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);

        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(-3, 1), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(-3, 0), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        
        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(0, -4), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(2, -3), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        
        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(6, 1), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(2, 6), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);

        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(-1, 4), false);
        assertEquals(expectedPlayer, player);
    } 

    @Test
    @DisplayName("Test unlocking a switch door")
    public void testUnlockSwitchDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("LogicalEntities/d_unlockSwitchDoor", "c_logicalEntities");

        EntityResponse player = getPlayer(res).get();
        EntityResponse expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(-2, 0), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(2, 1), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(1, 1), false);
        assertEquals(expectedPlayer, player);

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        res = dmc.tick(Direction.UP);

        res = dmc.tick(Direction.DOWN);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        player = getPlayer(res).get();
        expectedPlayer = new EntityResponse(player.getId(), player.getType(), new Position(2, 1), false);
        assertEquals(expectedPlayer, player);
    } 

    @Test
    @DisplayName("Test switch door is not activated when there are three switches and one is not active")
    public void testSwitchDoor3Switches() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("LogicalEntities/d_testSwitchDorr3Switches", "c_logicalEntities");
        
        // Player pushes the boulder onto one switch
        res = dmc.tick(Direction.RIGHT);

        // Player pushes the boulder onto one switch
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        // Player pushes the boulder onto one switch
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);

        EntityResponse actualPlayer = getPlayer(res).get();
        EntityResponse expectedPlayer = new EntityResponse(actualPlayer.getId(), actualPlayer.getType(), new Position(3, 0), false);
        assertEquals(expectedPlayer, actualPlayer);
    } 
}
