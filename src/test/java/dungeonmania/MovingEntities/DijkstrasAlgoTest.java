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
        Position expectedPath = new Position(2, 2);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        initDungonRes = dmc.tick(Direction.DOWN);
        expectedPath = expectedPath.translateBy(Direction.RIGHT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        initDungonRes = dmc.tick(Direction.DOWN);
        expectedPath = expectedPath.translateBy(Direction.RIGHT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());   

        initDungonRes = dmc.tick(Direction.UP);
        expectedPath = expectedPath.translateBy(Direction.RIGHT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());
        
        initDungonRes = dmc.tick(Direction.UP);
        expectedPath = expectedPath.translateBy(Direction.RIGHT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedPath, merc.getPosition());

    }
}
