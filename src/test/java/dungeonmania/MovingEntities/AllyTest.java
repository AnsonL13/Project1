package dungeonmania.MovingEntities;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;

import static dungeonmania.TestUtils.countEntityOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.exceptions.InvalidActionException;
public class AllyTest {

    @Test
    @DisplayName("Test bribed merc allied movement")
    public void testMercenaryAlliedMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribeMercenary", "c_battleTests_basicMercenaryMercenaryDies");


        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(3, getInventory(initDungonRes, "treasure").size());
        String merceanryId = getEntities(initDungonRes, "mercenary").get(0).getId();
        initDungonRes = assertDoesNotThrow(() -> dmc.interact(merceanryId));
        assertEquals(2, getInventory(initDungonRes, "treasure").size());

        // check ally gets player prev
        initDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        Position expectedAlly = new Position(4, 1);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.LEFT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.RIGHT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());       
        
        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after walk into merc
        initDungonRes = dmc.tick(Direction.LEFT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());  

    }
}
