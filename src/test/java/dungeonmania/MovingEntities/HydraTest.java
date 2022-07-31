package dungeonmania.MovingEntities;

import static dungeonmania.TestUtils.countEntityOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static dungeonmania.TestUtils.getEntities;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Battle;
import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.Player;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Wall;
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
    @DisplayName("Test hydra random movement")
    public void testHydraRandomMove() {
        Position intial = new Position(0, 0);
        Hydra hydra = new Hydra("0", 5, 5, intial);
        hydra.move(new Position(5,5), new ArrayList<Entity>());

        assertNotEquals(hydra.getPosition(), new Position(0, 0));
        assertEquals(hydra.getId(), "0");
    } 

    @Test
    @DisplayName("Test hydra run away")
    public void testHydraRunaway() {
        Position initial = new Position(0, 0);
        Hydra hydra = new Hydra("0", 5, 5, initial);
        // player affected by invincibility potion, run away
        hydra.setPotionStatus(false, true);
        hydra.move(new Position(5, 5), new ArrayList<Entity>());

        assertEquals(new Position(-1, 0), hydra.getPosition());
        assertEquals(hydra.getId(), "0");
    }

    @Test
    @DisplayName("Test hydra cannot move through boulder, door and wall")
    public void testHydraStuck() {
        Position position = new Position(5, 5);
        Hydra hydra = new Hydra("0", 5, 5, position);

        List<Entity> stuck = new ArrayList<Entity>();
        stuck.add(new Boulder("0", "boulder", new Position(4, 5), false));
        stuck.add(new Door("0", "door", new Position(5, 4), false, 0));
        stuck.add(new Wall("0", "wall", new Position(5, 6), false));
        stuck.add(new Wall("0", "wall", new Position(6, 5), false));

        hydra.move(new Position(0, 0), stuck);
        assertEquals(new Position(5, 5), hydra.getPosition());
    }

    @Test
    @DisplayName("Test hydra back to random movae after potion")
    public void testHydraRandomAfterPotion() {
        Position initial = new Position(0, 0);
        Hydra hydra = new Hydra("0", 5, 5, initial);

        hydra.setPotionStatus(false, true);
        hydra.move(new Position(5, 5), new ArrayList<Entity>());
        assertEquals(new Position(-1, 0), hydra.getPosition());
        assertEquals(hydra.getId(), "0");

        hydra.setPotionStatus(false, false);
        hydra.move(new Position(5, 5), new ArrayList<Entity>());
        assertNotEquals(new Position(-1, 0), hydra.getPosition());
    }

    @Test
    @DisplayName("Test health will not increase if health increase rate is 0")
    public void testHydraHealthNotIncrease() {
        Position initial = new Position(0, 0);
        Hydra hydra = new Hydra("0", "hydra", initial, false, 1, 5, 0, 3);
        Player player = new Player("0", "player", initial.translateBy(Direction.DOWN), false, 5, 5);
        
        assertEquals(5, hydra.getHealth());
        
        player.battle();
        Battle result = Battle.battleCalculate(player, hydra);

        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(0, hydra.getHealth());
    }

    @Test
    @DisplayName("Test health will always increase if health increase rate is 1")
    public void testHydraHealthAlwaysIncrease() {
        Position initial = new Position(0, 0);
        Hydra hydra = new Hydra("0", "hydra", initial, false, 1, 5, 1, 3);
        Player player = new Player("0", "player", initial.translateBy(Direction.DOWN), false, 5, 5);
        
        assertEquals(5, hydra.getHealth());
        
        player.battle();
        Battle result = Battle.battleCalculate(player, hydra);

        assertTrue(result.isEnemyWon());
        assertFalse(result.isPlayerWon());
        assertEquals(0, player.getPlayerHealth());
    }

    // Black box testing
    @Test
    @DisplayName("Test health will not increase if health increase rate is 0")
    public void testHydraHealthNotIncrease2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("MovingEntity/d_hydraTest", "MovingEntity/c_noHealthIncreaseHydraTest");
        int hydraCount = getEntities(initDungonRes,"hydra").size();
        int playerCount = getEntities(initDungonRes,"player").size();
        
        assertEquals(1, hydraCount);
        assertEquals(1, playerCount);

        DungeonResponse res = dmc.tick(Direction.RIGHT);

        hydraCount = getEntities(res,"hydra").size();
        playerCount = getEntities(res,"player").size();

        assertEquals(0, hydraCount);
        assertEquals(1, playerCount);
    }

    @Test
    @DisplayName("Test health will always increase if health increase rate is 1")
    public void testHydraHealthAlwaysIncrease2() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("MovingEntity/d_hydraTest", "MovingEntity/c_invincibleHydraTest");
        int hydraCount = getEntities(initDungonRes,"hydra").size();
        int playerCount = getEntities(initDungonRes,"player").size();
        
        assertEquals(1, hydraCount);
        assertEquals(1, playerCount);

        DungeonResponse res = dmc.tick(Direction.RIGHT);

        hydraCount = getEntities(res,"hydra").size();
        playerCount = getEntities(res,"player").size();

        assertEquals(1, hydraCount);
        assertEquals(0, playerCount);
    }
}
