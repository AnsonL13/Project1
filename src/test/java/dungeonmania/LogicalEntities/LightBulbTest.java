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
    @DisplayName("Test light bulb and or xor co_and")
    public void testLightBulb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("LogicalEntities/d_LightBulbTestOrXorCo_And", "c_logicalEntities");

        // Check light bulbs are initialised.
        assertEquals(7, getEntities(initDungonRes, "light_bulb_off").size());
        assertEquals(0, getEntities(initDungonRes, "light_bulb_on").size());
        
        // Player pushes the boulder onto the switch with the XOR circuit
        DungeonResponse res = dmc.tick(Direction.LEFT);

        // Check light bulb is activated
        assertEquals(6, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        // Player pushes the boulder onto the switch with the OR circuit
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Check light bulb is activated
        assertEquals(5, getEntities(res, "light_bulb_off").size());
        assertEquals(2, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);

        // Check light bulb is activated
        assertEquals(4, getEntities(res, "light_bulb_off").size());
        assertEquals(3, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);

        // Check light bulb is activated
        assertEquals(3, getEntities(res, "light_bulb_off").size());
        assertEquals(4, getEntities(res, "light_bulb_on").size());


        // Deactivate AND circuit
        res = dmc.tick(Direction.UP);

        assertEquals(4, getEntities(res, "light_bulb_off").size());
        assertEquals(3, getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // Deactivate xor circuit
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);

        assertEquals(5, getEntities(res, "light_bulb_off").size());
        assertEquals(2, getEntities(res, "light_bulb_on").size());

        // Deactivate or circuit
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(6, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        // Deactivate co_and circuit
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        assertEquals(7, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());
    } 

    @Test
    @DisplayName("Test light bulb is not activated when a logic bomb destroys the wires")
    public void testlogicBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("LogicalEntities/d_switchBombTest", "c_logicalEntities");

        // Check light bulbs are initialised.
        assertEquals(1, getEntities(initDungonRes, "light_bulb_off").size());
        assertEquals(4, getEntities(initDungonRes, "wire").size());
        
        // Player pushes the boulder onto the switch
        DungeonResponse res = dmc.tick(Direction.RIGHT);

        // Check light bulb is still deactiavted
        assertEquals(1, getEntities(res, "light_bulb_off").size());

        // Check wires are gone
        assertEquals(0, getEntities(res, "wire").size());
    } 

    @Test
    @DisplayName("Test light bulb is not activated when there are three switches and one is not active")
    public void testLightBulb3Switches() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("LogicalEntities/d_LightBulb3Switches", "c_logicalEntities");

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
