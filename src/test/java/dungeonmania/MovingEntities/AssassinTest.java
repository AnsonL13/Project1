package dungeonmania.MovingEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import static dungeonmania.TestUtils.countEntityOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Position;

public class AssassinTest {
    @Test
    @DisplayName("Test assassin exists")
    public void testAssaSimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_spawned_basicAssassin", "");
        int mercCount = countEntityOfType(initDungonRes, "assassin");
        assertEquals(1, mercCount);
    }
/*
    // White box testing
    @Test
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
