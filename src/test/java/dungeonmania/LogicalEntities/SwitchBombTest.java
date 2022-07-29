package dungeonmania.LogicalEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getEntities;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class SwitchBombTest {
    @Test
    @DisplayName("Test bomb and or xor co_and")
    public void testSwitchBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("LogicalEntities/d_multipleSwitchBombTest", "c_logicalEntities");

        // Check light bulbs are initialised.
        assertEquals(7, getEntities(initDungonRes, "light_bulb_off").size());
        assertEquals(0, getEntities(initDungonRes, "light_bulb_on").size());
        
        DungeonResponse res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);

        assertEquals(0, getEntities(res, "light_bulb_on").size());
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(1, getEntities(res, "boulder").size());
    } 

    @Test
    @DisplayName("Test bomb is not activated when there are three switches and one is not active")
    public void testBomb3Switches() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("LogicalEntities/d_Switchbomb3Switches", "c_logicalEntities");

        assertEquals(1, getEntities(initDungonRes, "bomb").size());
        
        DungeonResponse res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getEntities(res, "bomb").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getEntities(res, "bomb").size());

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, getEntities(res, "bomb").size());
    } 
}
