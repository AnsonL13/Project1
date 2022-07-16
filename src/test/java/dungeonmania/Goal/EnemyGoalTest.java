package dungeonmania.Goal;

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


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class EnemyGoalTest {
    @Test
    @DisplayName("Testing killing spider goal")
    public void testEnemySpiderGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoal_simpleSpider", "c_complexGoalsTest_andAll");

        assertTrue(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":exit"));

        // kill spider
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }  
    
    @Test
    @DisplayName("Testing killing mercenary goal")
    public void testEnemyMercenaryGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoal_mercenary", "c_movementTest_testMovementDown");

        assertTrue(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":exit"));

        // kill mercenary
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }  

    @Test
    @DisplayName("Testing destroy spawner for enemy goal")
    public void testEnemySpawnerGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoal_simpleSpawner", "c_battleTest_noWinners");

        assertTrue(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":exit"));

        // pick up sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "sword").size());
        String spawnerID = getEntities(res, "zombie_toast_spawner").get(0).getId();

        // destory spawner
        assertDoesNotThrow(() -> dmc.interact(spawnerID));
        assertEquals("", getGoals(dmc.getDungeonResponseModel()));
    }

    @Test
    @DisplayName("Testing killing multiple spider goal")
    public void testMultipleEnemySpiderGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoal_multipleSpider", "c_complexGoalsTest_andAll");

        assertTrue(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":treasure"));
        assertFalse(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":exit"));

        // kill spider
        assertEquals(3, getEntities(res, "spider").size());
        res = dmc.tick(Direction.RIGHT);

        assertEquals(2, getEntities(res, "spider").size());
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, getEntities(res, "spider").size());
        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, getEntities(res, "spider").size());
        assertEquals("", getGoals(res));
    } 
}

