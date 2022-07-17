package dungeonmania.MovingEntities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class mercenaryTest {
    @Test
    @DisplayName("Test mercenary exists")
    public void testMercSimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);
    }

    @Test
    @DisplayName("Test no mercenary exists")
    public void testNoMercSimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_battleTests_basicMercenaryMercenaryDies");
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(0, mercCount);
    }

    // White box testing
    @Test
    @DisplayName("Test mercenary moves")
    public void testMercMovesToPlayerStraight() {
        Position intial = new Position(0, 0);
        Mercenary mercenary = new Mercenary("1", intial, 1, 1, 1, 1, 1, 1);

        // Expected to move to right
        mercenary.move(new Position(2, 1), new ArrayList<Entity>());
        assertEquals(new Position(1, 0), mercenary.getPosition());

        //Move to left
        mercenary.move(new Position(-2, 0), new ArrayList<Entity>());
        assertEquals(new Position(0, 0), mercenary.getPosition());

        //Move down
        mercenary.move(new Position(0, 2), new ArrayList<Entity>());
        assertEquals(new Position(0, 1), mercenary.getPosition());
        
    }
   
    @Test
    @DisplayName("Test mercenary moves around wall")
    public void testMercMovesAroundWall() {
        List<Entity> stuck = new ArrayList<Entity>();
        stuck.add(new Wall("0", "Wall", new Position(1, 0), false));
        stuck.add(new Wall("0", "Wall", new Position(0, 1), false));
        stuck.add(new Wall("0", "Wall", new Position(0, -1), false));


        Position intial = new Position(0, 0);
        Mercenary mercenary = new Mercenary("1", intial, 1, 1, 1, 1, 1, 1);

        // Expected to move to left
        mercenary.move(new Position(2, 0), stuck);
        assertEquals(new Position(-1, 0), mercenary.getPosition());

    }

    @Test
    @DisplayName("Test mercenary runaway after invincibility")
    public void testMercRunAway() {
        Position intial = new Position(0, 0);
        Mercenary mercenary = new Mercenary("1", intial, 1, 1, 1, 1, 1, 1);
        mercenary.setInvincible(2);

        // Simulate merc move
        assertEquals(true, mercenary.isInvicible());
        mercenary.move(new Position(5,5), new ArrayList<Entity>());
        assertEquals(new Position(-1, 0), mercenary.getPosition());
        assertEquals(mercenary.getId(), "1");

        // Simulate merc move
        mercenary.move(new Position(5,5), new ArrayList<Entity>());
        assertEquals(new Position(-2, 0), mercenary.getPosition());
        assertEquals(mercenary.getId(), "1");

        //Check after duration moves closer to player
        assertEquals(mercenary.getId(), "1");

        intial = mercenary.getPosition();
        mercenary.move(new Position(5,5), new ArrayList<Entity>());
        assertEquals(new Position(-1, 0), mercenary.getPosition());
        assertEquals(false, mercenary.isInvicible());

    }

    @Test
    @DisplayName("Test mercenary random after invisibility")
    public void testMercRandomMobe() {
        Position intial = new Position(0, 0);
        Mercenary mercenary = new Mercenary("1", intial, 1, 1, 1, 1, 1, 1);
        mercenary.setInvisible(2);

        // Simulate merc move
        mercenary.move(new Position(5,5), new ArrayList<Entity>());
        assertEquals(mercenary.getId(), "1");
        assertEquals(true, mercenary.isInvisible());
        mercenary.move(new Position(5,5), new ArrayList<Entity>());

        //Check after duration moves closer to player
        assertEquals(false, mercenary.isInvisible());


    }
    
}
