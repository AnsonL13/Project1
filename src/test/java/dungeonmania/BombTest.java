package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class BombTest {
    @Test
    @DisplayName("Test surrounding entities are not removed when placing a bomb next to an inctive switch with config file bomb radius set to 2")
    public void placeInactiveBombRadius2() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        // Avoid activating the switch
        res = dmc.tick(Direction.DOWN);

        // Pick up Bomb
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent to switch
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        assertEquals(0, getInventory(res, "bomb").size());

        // Check Bomb did not explode with radius 2
        //
        //              Boulder/Switch      Wall            Wall
        //              Bomb                Treasure
        //
        //              Treasure
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(2, getEntities(res, "wall").size());
        assertEquals(2, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test bomb radius 2 explosion")
    public void placeActiveBombRadius2AboveSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        // Activate Switch
        res = dmc.tick(Direction.RIGHT);

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "bomb").size());

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);

        // Place Cardinally Adjacent above the switch
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        assertEquals(0, getInventory(res, "bomb").size());

        // Check Bomb exploded with radius 2
        //
        //              Boulder/Switch      Wall            Wall
        //              Bomb                Treasure
        //
        //              Treasure
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(0, getEntities(res, "wall").size());
        assertEquals(1, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test cannot pick up active bomb")
    public void notPickupActiveBomb() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        // Activate Switch
        res = dmc.tick(Direction.DOWN);

        // Pick up Bomb
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent below the switch
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        assertEquals(0, getInventory(res, "bomb").size());

        // Move off the active bomb square and go back on
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);

        // Check bomb did not go in inventory
        assertEquals(0, getInventory(res, "bomb").size());
        assertThrows(InvalidActionException.class, () -> dmc.tick(bombId));
        assertEquals(0, getInventory(res, "bomb").size());

        // Check bomb not exploded
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(2, getEntities(res, "wall").size());
        assertEquals(2, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test bomb blows up enemies")
    public void destroyEnemiesWithBomb() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombsVsMercenary", "c_bombsVsMercenary");

        // Get bomb
        res = dmc.tick(Direction.RIGHT);

        // activate switch
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent to the left of the switch
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        assertEquals(0, getInventory(res, "bomb").size());

        // Check bomb exploded
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(0, getEntities(res, "wall").size());
        assertEquals(0, getEntities(res, "mercenary").size());
        assertEquals(1, getEntities(res, "player").size());

        // Check walls are gone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        EntityResponse actualPlayer = getPlayer(res).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(actualPlayer.getId(), actualPlayer.getType(), new Position(6, 0), false);
        assertEquals(expectedPlayer, actualPlayer);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @DisplayName("Test bomb put down with no bomb in inventory")
    public void invalidBombUsage() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombsVsMercenary", "c_bombsVsMercenary");

        String bombId = getEntities(res, "bomb").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.tick(bombId));
        assertEquals(0, getInventory(res, "bomb").size());

        // Get bomb
        res = dmc.tick(Direction.RIGHT);

        // activate switch
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "bomb").size());

        res = dmc.tick(Direction.LEFT);

        EntityResponse actualPlayer = getPlayer(res).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(actualPlayer.getId(), actualPlayer.getType(), new Position(1, 0), false);
        assertEquals(expectedPlayer, actualPlayer);
        assertEquals(1, res.getBattles().size());
    }
}
