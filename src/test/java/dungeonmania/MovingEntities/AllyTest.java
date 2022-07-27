package dungeonmania.MovingEntities;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Battle;
import dungeonmania.DungeonManiaController;
import dungeonmania.Player;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AllyTest {

    @Test
    @DisplayName("Test bribed merc allied movement")
    public void testMercenaryAlliedMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_bribeMercenary", "c_battleTests_basicMercenaryMercenaryDies");

        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(3, getInventory(initDungonRes, "treasure").size());
        String merceanryId = getEntities(initDungonRes, "mercenary").get(0).getId();
        initDungonRes = assertDoesNotThrow(() -> dmc.interact(merceanryId));
        assertEquals(2, getInventory(initDungonRes, "treasure").size());

        // check ally gets player prev
        initDungonRes = dmc.tick(Direction.LEFT);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        Position expectedAlly = new Position(4, 1);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.LEFT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.RIGHT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());       
        
        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after walk into merc
        initDungonRes = dmc.tick(Direction.LEFT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());  

    }

    @Test
    @DisplayName("Test bribed merc allied movement from far")
    public void testMercenaryAlliedMovementFar() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("movement2/d_bribeTest_bribeAgain", "c_zombieTest_highSpawn");

        // grab treasure
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(3, getInventory(initDungonRes, "treasure").size());

        // bribe 
        String merceanryId = getEntities(initDungonRes, "mercenary").get(0).getId();
        initDungonRes = assertDoesNotThrow(() -> dmc.interact(merceanryId));
        assertEquals(2, getInventory(initDungonRes, "treasure").size());

        // check ally walks closer to player
        initDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse merc = getEntities(initDungonRes, "mercenary").get(0);
        Position expectedAlly = new Position(4, 1);
        assertEquals(expectedAlly, merc.getPosition());

        // check after move tick
        initDungonRes = dmc.tick(Direction.RIGHT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly.translateBy(Direction.LEFT), merc.getPosition());

        // get player prev
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.DOWN);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());

        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after move tick
        initDungonRes = dmc.tick(Direction.RIGHT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());       
        
        // get player prev
        initPlayer = getPlayer(initDungonRes).get();
        expectedAlly = initPlayer.getPosition();

        // check after walk into merc
        initDungonRes = dmc.tick(Direction.LEFT);
        merc = getEntities(initDungonRes, "mercenary").get(0);
        assertEquals(expectedAlly, merc.getPosition());  

    }

    @Test
    @DisplayName("Test mercenary interaction, bribe with one treasure and bribe again")
    public void testBribingTheMercenaryAlready() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("movement2/d_bribeTest_bribeAgain", "c_zombieTest_highSpawn");

        // grab treasure
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);
        initDungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(3, getInventory(initDungonRes, "treasure").size());

        // bribe 
        String merceanryId = getEntities(initDungonRes, "mercenary").get(0).getId();
        initDungonRes = assertDoesNotThrow(() -> dmc.interact(merceanryId));
        assertEquals(2, getInventory(initDungonRes, "treasure").size());

        // try to bribe again but nothing happens
        initDungonRes = assertDoesNotThrow(() -> dmc.interact(merceanryId));
        assertEquals(2, getInventory(initDungonRes, "treasure").size());
    }

    // TODO failed bribe from another boundary

    // white box testing with allied merc

    @Test
    @DisplayName("Test simple zombie battle with ally")
    public void testAllyZombieDie() {
        // Create zombie and player
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 5, 2, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 1, 2);
        Mercenary merc = new Mercenary("1", new Position(0, 2),4,0,1,1,1,1);
        player.addAlly(merc);
        Battle result = Battle.battleCalculate(player, zombie);

        // check results
        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(2, result.getRounds().size());
    } 

    @Test
    @DisplayName("Test simple zombie battle with ally")
    public void testAllyZombieDieInvisible() {
        // Create zombie and player
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 10, 2, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 1, 1);
        Mercenary merc = new Mercenary("1", new Position(0, 2),0,10,1,1,1,1);
        player.addAlly(merc);
        Battle result = Battle.battleCalculate(player, zombie);

        // check results
        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(10, result.getRounds().size());
    }
}
