package dungeonmania.MovingEntities;

import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AssassinTest {
    @Test
    @DisplayName("Test assassin exists")
    public void testAssaSimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("MovingEntity/d_spawned_basicAssassin", "MovingEntity/c_assassinTest_simple");
        int mercCount = countEntityOfType(initDungonRes, "assassin");
        assertEquals(1, mercCount);
    }

    @Test
    @DisplayName("Test assassin bribe")
    public void testAssabribeWork() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse actualDungonRes = dmc.newGame("MovingEntity/d_spawned_basicAssassin", "MovingEntity/c_assassinTest_simple");

        //collect treasure
        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(1, getInventory(actualDungonRes, "treasure").size());
        String assaID = getEntities(actualDungonRes, "assassin").get(0).getId();
        
        actualDungonRes = assertDoesNotThrow(() -> dmc.interact(assaID));
        
        // TODO test no battle
        Position expected = getPlayer(actualDungonRes).get().getPosition();
        actualDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(0, actualDungonRes.getBattles().size());
        assertEquals(expected, getEntities(actualDungonRes, "assassin").get(0).getPosition());
        
    }

    @Test
    @DisplayName("Test fail bribe")
    public void testAssaFailBribe() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse actualDungonRes = dmc.newGame("MovingEntity/d_spawned_basicAssassin", "MovingEntity/c_assassinTest_failBribe");
        
        //collect treasure
        actualDungonRes = dmc.tick(Direction.LEFT);
        assertEquals(1, getInventory(actualDungonRes, "treasure").size());
        String assaID = getEntities(actualDungonRes, "assassin").get(0).getId();
        
        //bribe
        actualDungonRes = assertDoesNotThrow(() -> dmc.interact(assaID));
        
        //fail rate is 1 so fail
        actualDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals(1, actualDungonRes.getBattles().size());

    }

    // White box testing
    @Test
    @DisplayName("Test assa can see player")
    public void testAssaCanSeePlayer() {

        //Low chance that this test may pass if implementation is wrong but
        // it improves coverage
        Position intial = new Position(0, 0);
        Assassin assassin = new Assassin("1", intial, 1, 1, 1, 1, 1, 1,5, 0.0);

        assassin.setPotionStatus(true, false);

        intial = intial.translateBy(Direction.DOWN);
        assassin.move(new Position(0, 4), new ArrayList<Entity>());
        assertEquals(intial, assassin.getPosition());

        intial = intial.translateBy(Direction.DOWN);
        assassin.move(new Position(0, 4), new ArrayList<Entity>());
        assertEquals(intial, assassin.getPosition());

        intial = intial.translateBy(Direction.DOWN);
        assassin.move(new Position(0, 4), new ArrayList<Entity>());
        assertEquals(intial, assassin.getPosition());
    }

    @Test
    @DisplayName("Test assa cannot see player")
    public void testAssaPlayerInvisible() {

        //Test for branch coverage when player is invisble
        Position intial = new Position(0, 0);
        Assassin assassin = new Assassin("1", intial, 1, 1, 1, 1, 1, 1,1, 1.0);
        assassin.setPotionStatus(true, false);

        assassin.move(new Position(0, 4), new ArrayList<Entity>());
        assassin.move(new Position(0, -4), new ArrayList<Entity>());
        assassin.move(new Position(10, 3), new ArrayList<Entity>());
        assassin.move(new Position(-5, 0), new ArrayList<Entity>());


    }

 /* @Test
    @DisplayName("Test assassin moves")
    public void testAssaMovesToPlayerStraight() {
    
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
    @DisplayName("Test assassin moves around wall")
    public void testAssaMovesAroundWall3() {
        List<Entity> stuck = new ArrayList<Entity>();
        stuck.add(new Wall("0", "Wall", new Position(1, 0), false));
        stuck.add(new Wall("0", "Wall", new Position(0, 1), false));
        stuck.add(new Wall("0", "Wall", new Position(0, -1), false));


        Position intial = new Position(0, 0);
        Mercenary mercenary = new Mercenary("1", intial, 1, 1, 1, 1, 1, 1);

        // Expected to move to left
        mercenary.move(new Position(2, 0), stuck);
        assertEquals(new Position(-1, 0), mercenary.getPosition());

    }*/
}
