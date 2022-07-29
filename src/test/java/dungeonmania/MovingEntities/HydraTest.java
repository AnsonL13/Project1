package dungeonmania.MovingEntities;

import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static dungeonmania.TestUtils.getPlayer;

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
    @DisplayName("Test health will increase after being attacked if health increase rate out of range")
    public void testHydraHealthNotIncrease() {
        Position initial = new Position(0, 0);
        Hydra hydra = new Hydra("0", "hydra", initial, false, 1, 5, 0.8, 3, false);
        Player player = new Player("0", "player", initial.translateBy(Direction.DOWN), false, 5, 5);
        
        assertFalse(hydra.isAttack());
        assertEquals(5, hydra.getOriginHealth());
        
        player.battle();
        Battle result = Battle.battleCalculate(player, hydra);

        assertTrue(hydra.isAttack());
        assertEquals(8, hydra.getHealth());

        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
    }
}
