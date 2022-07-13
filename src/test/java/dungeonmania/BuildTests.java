package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BuildTests {

    // Black box testing
    @Test
    @DisplayName("Check string is incorret throws exception")
    public void testBuildArgumentExcepetionString() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");
        assertThrows(IllegalArgumentException.class, () -> dmc.build("sheld"));
        
    }

    @Test
    @DisplayName("Check no mats throws exception")
    public void testBuildActionExcepetionString() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Check no mats for sheild but is boe throws exception")
    public void testBuildActionExcepetionStringBow() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_buildTest_bowMats", "c_movementTest_testMovementDown");
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
        assertDoesNotThrow( () -> dmc.build("bow"));

    }

/*
    @Test
    @DisplayName("Check player has shield")
    public void testBuildShield() {
        Dungeon dungeon = new Dungeon("1", dungeonJson, configJson);
        dungeon.build("shield");
    }

    @Test
    @DisplayName("Check player has bow")
    public void testBuildBow() {
        Dungeon dungeon = new Dungeon("1", dungeonJson, configJson);
        dungeon.build("shield");
    }*/

}
