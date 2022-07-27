package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PersistenceTests {
    @Test
    @DisplayName("Test persistence")
    public void testPersistence() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");

        dmc.tick(Direction.DOWN);

        initDungonRes = dmc.saveGame("boulders");
        
        DungeonResponse res = dmc.loadGame("boulders");

        assertEquals(initDungonRes.getAnimations(), res.getAnimations());
        assertEquals(initDungonRes.getBattles(), res.getBattles());
        assertEquals(initDungonRes.getBuildables(), res.getBuildables());
        assertEquals(initDungonRes.getDungeonId(), res.getDungeonId());
        assertEquals(initDungonRes.getDungeonName(), res.getDungeonName());
        assertEquals(initDungonRes.getEntities(), res.getEntities());
        assertEquals(initDungonRes.getGoals(), res.getGoals());
        assertEquals(initDungonRes.getInventory(), res.getInventory());

        // Delete file
        File myObj = new File("src/main/java/dungeonmania/savedgames/boulders.ser"); 
        myObj.delete();
    }
}
