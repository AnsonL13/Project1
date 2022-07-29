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
        
        // Player pushes the boulder onto the switch with the XOR circuit
        DungeonResponse res = dmc.tick(Direction.RIGHT);

        assertEquals(6, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        // Check light bulb is activated
        res = dmc.tick(Direction.LEFT);

        assertEquals(6, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        
    } 
}
