package dungeonmania.LogicalEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getEntities;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class SwitchTest {
    @Test
    @DisplayName("Test switch and or xor co_and")
    public void testSwitch() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("LogicalEntities/d_switchTest", "c_logicalEntities");

        // Check light bulbs are initialised.
        assertEquals(7, getEntities(initDungonRes, "light_bulb_off").size());
        assertEquals(0, getEntities(initDungonRes, "light_bulb_on").size());
        
        DungeonResponse res = dmc.tick(Direction.LEFT);

        assertEquals(6, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.LEFT);

        assertEquals(7, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(6, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.RIGHT);

        assertEquals(7, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);

        assertEquals(6, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.DOWN);

        assertEquals(7, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);

        assertEquals(6, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.UP);

        assertEquals(7, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());
    } 

    @Test
    @DisplayName("Test switch is not activated when there are three switches and one is not active")
    public void testSwitch3Switches() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("LogicalEntities/d_Switch3Switches", "c_logicalEntities");

        // Check light bulb is unintialised
        assertEquals(1, getEntities(initDungonRes, "light_bulb_off").size());
        assertEquals(0, getEntities(initDungonRes, "light_bulb_on").size());
        
        // Player pushes the boulder onto one switch
        DungeonResponse res = dmc.tick(Direction.RIGHT);

        // Check light bulb is still deactiavted
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());

        // Player pushes the boulder onto one switch
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        // Check light bulb is still deactiavted
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());

        // Player pushes the boulder onto one switch
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        // Check light bulb is activated
        assertEquals(0, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());
    } 
}
