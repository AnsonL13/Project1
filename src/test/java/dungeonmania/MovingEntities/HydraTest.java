package dungeonmania.MovingEntities;

import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class HydraTest {
    @Test
    @DisplayName("Test hydra exists")
    public void testHydraSimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungeonRes = dmc.newGame("MovingEntity/d_spawned_basicHydra", "MovingEntity/c_hydraTest_simple");
        int count = countEntityOfType(initDungeonRes, "hydra");
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Test hydra movement")
    public void testHydraRandomMove() {
        Position intial = new Position(0, 0);
        Hydra hydra = new Hydra("0", 5, 5, intial);
        hydra.move(new Position(5,5), new ArrayList<Entity>());

        assertNotEquals(hydra.getPosition(), new Position(0, 0));
        assertEquals(hydra.getId(), "0");
    } 
}
