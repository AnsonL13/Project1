package dungeonmania.LogicalEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getEntities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class LightBulbTest {
    @Test
    @DisplayName("Test light bulb or xor co_and")
    public void testLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("LogicalEntities/d_LightBulbTestOrXorCo_And", "c_logicalEntities");

        // Check light bulbs are initialised.
        assertEquals(5, getEntities(initDungonRes, "light_bulb_off").size());
        assertEquals(0, getEntities(initDungonRes, "light_bulb_on").size());
        
        // Player pushes the boulder onto the switch with the XOR circuit
        DungeonResponse res = dmc.tick(Direction.LEFT);

        // Check light bulb is activated
        assertEquals(4, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        // Player pushes the boulder onto the switch with the OR circuit
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Check light bulb is activated
        assertEquals(3, getEntities(res, "light_bulb_off").size());
        assertEquals(2, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);

        // Check light bulb is activated
        assertEquals(2, getEntities(res, "light_bulb_off").size());
        assertEquals(3, getEntities(res, "light_bulb_on").size());
    } 
}
