package dungeonmania.MovingEntities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingEntitySystemTest {
    @Test
    @DisplayName("Test multiple mobs and movement")
    public void testMultipleMobsMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movingTest_allEntity", "c_movingTest_allEntity");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        EntityResponse initMerc = getEntities(initDungonRes, "mercenary").get(0);

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 0), false);
        EntityResponse expectedMerc = new EntityResponse(initMerc.getId(), initPlayer.getType(), new Position(-1, 0), true);

        //Check for goals and moving entity
        assertTrue(getGoals(initDungonRes).contains(":exit"));
        assertTrue(getGoals(initDungonRes).contains(":treasure"));
        assertEquals(expectedPlayer, initPlayer);
        assertEquals(expectedMerc, initMerc);

        // move player right
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        EntityResponse actualMerc = getEntities(actualDungonRes, "mercenary").get(0);

        // create expected over exit
        expectedPlayer = new EntityResponse(actualPlayer.getId(), actualPlayer.getType(), new Position(1, 0), false);
        expectedMerc = new EntityResponse(actualMerc.getId(), actualMerc.getType(), new Position(0, 0), true);

        //Check for goals and moving entity
        assertTrue(getGoals(actualDungonRes).contains(":exit"));
        assertTrue(getGoals(actualDungonRes).contains(":treasure"));
        assertEquals(expectedPlayer, actualPlayer);
        assertEquals(expectedMerc, actualMerc);

        // move player right pick up treasure
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungonRes).get();
        actualMerc = getEntities(actualDungonRes, "mercenary").get(0);
        EntityResponse actualKey = getEntities(actualDungonRes, "treasure").get(0);

        // create expected after move
        expectedPlayer = new EntityResponse(actualPlayer.getId(), actualPlayer.getType(), new Position(2, 0), false);
        expectedMerc = new EntityResponse(actualMerc.getId(), actualMerc.getType(), new Position(1, 0), true);
        EntityResponse treasure = new EntityResponse(actualKey.getId(), actualKey.getType(), new Position(2, 0), false);

        //Check for goals and moving entity
        assertTrue(getGoals(actualDungonRes).contains(":exit"));
        assertFalse(getGoals(actualDungonRes).contains(":treasure"));
        assertEquals(expectedPlayer, actualPlayer);
        assertEquals(expectedMerc, actualMerc);
        assertEquals(treasure, getInventory(actualDungonRes, "treasure").get(0));
        assertEquals(1, countEntityOfType(actualDungonRes, "zombie_toast_spawner"));
        int zombieCount = initDungonRes.getEntities()
            .stream()
            .filter(it -> it.getType().equals("zombie_toast"))
            .collect(Collectors.toList()).size();
        assertEquals(1, zombieCount);

        // move player into wall and create battle
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungonRes).get();

        // create expected 
        expectedPlayer = new EntityResponse(actualPlayer.getId(), actualPlayer.getType(), new Position(2, 0), false);

        //Check for goals and moving entity and battle
        assertTrue(getGoals(actualDungonRes).contains(":exit"));
        assertFalse(getGoals(actualDungonRes).contains(":treasure"));
        assertEquals(expectedPlayer, actualPlayer);
        assertEquals(treasure, getInventory(actualDungonRes, "treasure").get(0));
        assertEquals(0, countEntityOfType(actualDungonRes, "mercenary"));

        // Get battle response
        BattleResponse battle = actualDungonRes.getBattles().get(0);
        List<RoundResponse> rounds = battle.getRounds();
        assertEquals(2, rounds.size());

        //Check round one
        assertEquals(9.9, rounds.get(0).getDeltaCharacterHealth());
        assertEquals(1, rounds.get(0).getDeltaEnemyHealth());

        //Check round two
        assertEquals(9.8, rounds.get(1).getDeltaCharacterHealth());
        assertEquals(0, rounds.get(1).getDeltaEnemyHealth());

        // Spawn spider and zombie
        actualDungonRes = dmc.tick(Direction.DOWN);
        actualDungonRes = dmc.tick(Direction.LEFT);
        zombieCount = initDungonRes.getEntities()
            .stream()
            .filter(it -> it.getType().equals("zombie_toast"))
            .collect(Collectors.toList()).size();
        assertEquals(2, zombieCount);
        assertEquals(1, countEntityOfType(actualDungonRes, "spider"));

        // End game
        actualDungonRes = dmc.tick(Direction.LEFT);
        assertFalse(getGoals(actualDungonRes).contains(":exit"));
        assertFalse(getGoals(actualDungonRes).contains(":treasure"));

    }

    @Test
    @DisplayName("Test multiple mobs and movement invisbility movement")
    public void testMultipleMobsPotions() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_battleTest_superInvisiblePotion", "c_battleTest_superInvisiblePotion");

        //Check for goals
        assertTrue(getGoals(initDungonRes).contains(":exit"));
        assertTrue(getGoals(initDungonRes).contains(":enemies"));
        int mercCount = countEntityOfType(initDungonRes, "mercenary");
        assertEquals(1, mercCount);

        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse potion = getEntities(actualDungonRes, "invisibility_potion").get(0);

        //Check for goals
        assertTrue(getGoals(actualDungonRes).contains(":exit"));
        assertTrue(getGoals(actualDungonRes).contains(":enemies"));

        //Check assert throw potion not in inventory yet
        assertThrows(InvalidActionException.class, () -> dmc.tick(Direction.RIGHT));

        // Pick up potion
        actualDungonRes = dmc.tick(Direction.RIGHT);

        //Check expected
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        EntityResponse expectedPlayer = new EntityResponse(actualPlayer.getId(), actualPlayer.getType(), new Position(2, 0), false);
        assertEquals(expectedPlayer, actualPlayer);
        assertEquals(potion, getInventory(actualDungonRes, "invisibility_potion").get(0));

        // Use potion
        assertDoesNotThrow(() -> dmc.tick(potion.getId()));
        
        // Pick up sword
        actualDungonRes = dmc.tick(Direction.RIGHT);

        // Move to spawner
        actualDungonRes = dmc.tick(Direction.DOWN);
        actualDungonRes = dmc.tick(Direction.DOWN);
        actualDungonRes = dmc.tick(Direction.LEFT);
        actualDungonRes = dmc.tick(Direction.LEFT);

        String spawnerID = getEntities(actualDungonRes, "zombie_toast_spawner").get(0).getId();

        //Destory Spawner
        assertDoesNotThrow(() ->  dmc.interact(spawnerID));

        // Move to exit
        actualDungonRes = dmc.tick(Direction.UP);
        actualDungonRes = dmc.tick(Direction.UP);

        // Check no battles and goals all done
        assertFalse(getGoals(actualDungonRes).contains(":exit"));
        assertFalse(getGoals(actualDungonRes).contains(":enemies"));
        assertEquals(0,  actualDungonRes.getBattles().size());
    }
}
