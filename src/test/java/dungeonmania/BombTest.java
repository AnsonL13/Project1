package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntities.Bomb;
import dungeonmania.CollectableEntities.BombState;
import dungeonmania.CollectableEntities.InactiveBombState;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BombTest {
    @Test
    @DisplayName("Test pick up a bomb")
    public void testPickUpOneBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bombTest_pickUpBomb", "c_collectableEntitiesTest");
        // Pickup the bomb
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check bomb in inventory
        assertEquals(1, getInventory(initDungonRes, "bomb").size());
    }
    @Test
    @DisplayName("Test place a bomb")
    public void testPickUpUsedBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bombTest_pickUpBomb", "c_collectableEntitiesTest");
        // Pickup the bomb
        initDungonRes = dmc.tick(Direction.RIGHT);

        // Check bomb in inventory
        assertEquals(1, getInventory(initDungonRes, "bomb").size());
        
        // question, how to get bomb here
        Bomb bomb = bombs

        initDungonRes = dmc.tick(Direction.UP);
        initDungonRes = dmc.tick(Direction.UP);
        //put down bomb
        initDungonRes = dmc.placeBomb(bomb);

        assertEquals(0, getInventory(initDungonRes, "bomb").size());
        EntityResponse bombRes = getEntities(initDungonRes,"bomb").get(0);
        EntityResponse expectedBomb = new EntityResponse("0", "bomb", new Position(3, 4), false);
        assertEquals(bombRes, expectedBomb);
       // try to pick up a used bomb
        initDungonRes = dmc.tick(Direction.UP);
        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(0, getInventory(initDungonRes, "bomb").size());
    } 
}