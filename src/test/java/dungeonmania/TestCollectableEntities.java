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

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestCollectableEntities {
    @Test
    @DisplayName("Test generate new CollectableEntities")
    public void testNewCollectableEntities() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse initDungonRes = dmc.newGame("d_collectableEntitiesTest", "c_collectableEntitiesTest");
        EntityResponse treasure = getEntities(initDungonRes,"treasure").get(0);
        EntityResponse expectedTreasure = new EntityResponse("0", "treasure", new Position(1, 1), false);
        assertEquals(expectedTreasure, treasure);

        EntityResponse bomb = getEntities(initDungonRes,"bomb").get(0);
        EntityResponse expectedBomb = new EntityResponse("1", "bomb", new Position(3, 2), false);
        assertEquals(bomb, expectedBomb);

        EntityResponse invincibilityPotion = getEntities(initDungonRes,"invincibility_potion").get(0);
        EntityResponse expectedinvincibilityPotion = new EntityResponse("2", "invincibility_potion", new Position(2, 4), false);
        assertEquals(invincibilityPotion, expectedinvincibilityPotion);

        EntityResponse invisibilityPotion = getEntities(initDungonRes,"invisibility_potion").get(0);
        EntityResponse expectedinvisibilityPotion = new EntityResponse("3", "invisibility_potion", new Position(2, 5), false);
        assertEquals(invisibilityPotion, expectedinvisibilityPotion);

        EntityResponse wood = getEntities(initDungonRes,"wood").get(0);
        EntityResponse expectedWood = new EntityResponse("4", "wood", new Position(5, 2), false);
        assertEquals(wood, expectedWood);

        EntityResponse sword = getEntities(initDungonRes,"sword").get(0);
        EntityResponse expectedSword = new EntityResponse("5", "sword", new Position(5, 3), false);
        assertEquals(sword, expectedSword);

        EntityResponse arrow = getEntities(initDungonRes,"arrow").get(0);
        EntityResponse expectedArrow = new EntityResponse("6", "arrow", new Position(2, 2), false);
        assertEquals(arrow, expectedArrow);
    }


    
}
