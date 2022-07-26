package dungeonmania.MovingEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getEntities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.response.models.EntityResponse;

public class DijkstrasAlgoTest {
    @Test
    @DisplayName("Test mercenary wall")
    public void testMercSimpleWallBlock() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("movement2/d_mercenaryTest_dijkstraSimple", "c_potionsTest");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        //walk player to wall
        initDungonRes = dmc.tick(Direction.DOWN);
        
        //check merc goes to shortest path
        Position expectedPath = new Position(2, 3);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        initDungonRes = dmc.tick(Direction.DOWN);
        expectedPath = expectedPath.translateBy(Direction.RIGHT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        initDungonRes = dmc.tick(Direction.DOWN);
        expectedPath = expectedPath.translateBy(Direction.UP);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        initDungonRes = dmc.tick(Direction.DOWN);
        expectedPath = expectedPath.translateBy(Direction.UP);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());
        
        initDungonRes = dmc.tick(Direction.DOWN);
        expectedPath = expectedPath.translateBy(Direction.LEFT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());

    }

    @Test
    @DisplayName("Test mercenary over swamp")
    public void testMercSimpleSwamp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("movement2/d_mercenaryTest_dijkstraSwamp", "c_potionsTest");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        //walk player to wall
        initDungonRes = dmc.tick(Direction.RIGHT);
        
        //check merc goes to shortest path
        Position expectedPath = new Position(1, -1);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        //walk player to wall
        initDungonRes = dmc.tick(Direction.RIGHT);
        
        //check merc goes to shortest path
        expectedPath = expectedPath.translateBy(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

    }

    @Test
    @DisplayName("Test mercenary over mutiple swamp")
    public void testMercMultipleSwamp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("movement2/d_mercenaryTest_multipleSwamp", "c_potionsTest");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        //walk player to wall
        initDungonRes = dmc.tick(Direction.DOWN);
        
        //check merc goes to shortest path
        Position expectedPath = new Position(3, -1);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        //merc stuck
        initDungonRes = dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.DOWN);
        
        initDungonRes = dmc.tick(Direction.DOWN);
        //check merc goes to shortest path
        expectedPath = expectedPath.translateBy(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());  
        
    }

    @Test
    @DisplayName("Test mercenary over lowest swamp and normal tiles")
    public void testMercMixDijkstra() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("movement2/d_mercenaryTest_mixSwamp", "c_potionsTest");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        //walk player to wall
        initDungonRes = dmc.tick(Direction.DOWN);
        
        //check merc goes to shortest path
        Position expectedPath = new Position(1, -1);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        //walk player to wall and merc stuck
        initDungonRes = dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.DOWN);
        initDungonRes = dmc.tick(Direction.DOWN);
        
        //check merc goes to shortest path
        expectedPath = expectedPath.translateBy(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

    }

    @Test
    @DisplayName("Test mercenary over lowest swamp")
    public void testMercSmallestSwamp() {
        // TODO 
        /*
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("movement2/d_mercenaryTest_dijkstraSwamp", "c_potionsTest");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        //walk player to wall
        initDungonRes = dmc.tick(Direction.RIGHT);
        
        //check merc goes to shortest path
        Position expectedPath = new Position(1, -1);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        //walk player to wall
        initDungonRes = dmc.tick(Direction.RIGHT);
        
        //check merc goes to shortest path
        expectedPath = expectedPath.translateBy(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   */

    }

    @Test
    @DisplayName("Test mercenary to Portal")
    public void testMercPortal() {
        // TODO 
        /*
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("movement2/d_mercenaryTest_dijkstraSwamp", "c_potionsTest");
        
        initDungonRes = dmc.tick(Direction.RIGHT);

        //check merc goes to shortest path -> portal
        Position expectedPath = new Position(1, -1);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());
        
        initDungonRes = dmc.tick(Direction.RIGHT);

        //check merc goes out portal
        expectedPath = new Position(1, -1);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());
        */
    }
}
