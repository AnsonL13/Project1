package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

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

    @Test
    @DisplayName("Test listing saved games")
    public void testListingGames() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Check the list of games is empty upon initialisation. 
        List<String> gamesList = dmc.allGames();
        assertEquals(0, gamesList.size());

        // Save one game
        dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        dmc.tick(Direction.DOWN);
        dmc.saveGame("d_movementTest_testMovementDown1");
        
        // Check list of games is correct
        gamesList = dmc.allGames();
        assertEquals(1, gamesList.size());

        // Save another game
        dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        dmc.tick(Direction.UP);
        dmc.saveGame("d_movementTest_testMovementDown2");

        // Check list of games is correct
        gamesList = dmc.allGames();
        assertEquals(2, gamesList.size());

        // Delete file
        File myObj = new File("src/main/java/dungeonmania/savedgames/d_movementTest_testMovementDown1.ser"); 
        myObj.delete();
        File myObj2 = new File("src/main/java/dungeonmania/savedgames/d_movementTest_testMovementDown2.ser"); 
        myObj2.delete();
    }

    @Test
    @DisplayName("Test load game can throw illegalArgument Exception")
    public void testLoadGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");

        dmc.tick(Direction.DOWN);

        dmc.saveGame("dungeon1");

        assertThrows(IllegalArgumentException.class, () -> dmc.loadGame("djlfsjdlkfjwslf"));

        // Delete file
        File myObj = new File("src/main/java/dungeonmania/savedgames/dungeon1.ser"); 
        myObj.delete();
    }
}
