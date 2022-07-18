
package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;

import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;


public class BattleTest3 {

    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath, int roundAmount) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(-(enemyAttack / 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-(playerAttack / 5), round.getDeltaEnemyHealth(), 0.001);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        assertEquals(rounds.size(), roundAmount);

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    @Test
    @DisplayName("Test simple spider battle")
    public void testSpiderDie() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse initialResponse = controller.newGame("d_potionsSpiderBattleTest", "c_potionsTest");
        int spiderCount = countEntityOfType(initialResponse, "spider"); 
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, spiderCount);
        
        // Push the player into the spider
        DungeonResponse postBattleResponse = controller.tick(Direction.UP);
        BattleResponse battle = postBattleResponse.getBattles().get(0);

        assertBattleCalculations("spider", battle, true, "c_potionsTest", 33);
        assertEquals(1, getEntities(postBattleResponse, "player").size());
        assertEquals(0, getEntities(postBattleResponse, "spider").size());

        postBattleResponse = controller.tick(Direction.UP);
    }

    @Test
    @DisplayName("Test spider battle with sword")
    public void testSpiderBattleWithSword() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse initialResponse = controller.newGame("d_potionsSpiderBattleTest", "c_potionsTest");
        int spiderCount = countEntityOfType(initialResponse, "spider"); 
        assertEquals(1, countEntityOfType(initialResponse, "sword"));
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, spiderCount);
        
        // Collect the sword
        DungeonResponse postBattleResponse = controller.tick(Direction.RIGHT);
        postBattleResponse = controller.tick(Direction.LEFT);

        // Push the player into the spider
        postBattleResponse = controller.tick(Direction.UP);
        BattleResponse battle = postBattleResponse.getBattles().get(0);

        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", "c_potionsTest"));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile("spider" + "_health", "c_potionsTest"));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", "c_potionsTest"));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile("spider" + "_attack", "c_potionsTest"));

        double sword = Double.parseDouble(getValueFromConfigFile("sword" + "_attack", "c_potionsTest"));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10));
            assertEquals(round.getDeltaEnemyHealth(), -((playerAttack + sword) / 5));
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();    
        }

        assertTrue(enemyHealth <= 0);
        assertTrue(playerHealth > 0);

        assertEquals(17, rounds.size());
        
        assertEquals(1, getEntities(postBattleResponse, "player").size());
        assertEquals(0, getEntities(postBattleResponse, "spider").size());
        assertEquals(1, getInventory(postBattleResponse, "sword").size());
    }

    @Test
    @DisplayName("Test no battle spider with invisibility potion")
    public void testNoBattleInvisibilityPotion() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse initialResponse = controller.newGame("d_potionsSpiderBattleTest", "c_potionsTest");
        int spiderCount = countEntityOfType(initialResponse, "spider"); 
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, spiderCount);
        List<EntityResponse> potion = getEntities(initialResponse, "invisibility_potion");
        assertEquals(1, potion.size());
        String potionId = potion.get(0).getId();
        assertEquals(0, getInventory(initialResponse, "invisibility_potion").size());

        // Collect the invis potion
        DungeonResponse postBattleResponse = controller.tick(Direction.DOWN);
        postBattleResponse = controller.tick(Direction.UP);
        assertEquals(1, getInventory(postBattleResponse, "invisibility_potion").size());

        // Use the invis potion
        assertDoesNotThrow(()->controller.tick(potionId));
        postBattleResponse = controller.getDungeonResponseModel();
        assertEquals(0, getInventory(postBattleResponse, "invisibility_potion").size());

        // Push the player into the spider
        postBattleResponse = controller.tick(Direction.UP);
        assertEquals(0, postBattleResponse.getBattles().size());

        postBattleResponse = controller.tick(Direction.RIGHT);
        assertEquals(0, postBattleResponse.getBattles().size());

        postBattleResponse = controller.tick(Direction.RIGHT);
        assertEquals(1, postBattleResponse.getBattles().size());
        BattleResponse battle = postBattleResponse.getBattles().get(0);

        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", "c_potionsTest"));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile("spider" + "_health", "c_potionsTest"));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", "c_potionsTest"));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile("spider" + "_attack", "c_potionsTest"));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10));
            assertEquals(round.getDeltaEnemyHealth(), -(playerAttack / 5));
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();    
        }

        assertTrue(enemyHealth <= 0);
        assertTrue(playerHealth > 0);

        assertEquals(33, rounds.size());
        
        assertEquals(1, getEntities(postBattleResponse, "player").size());
        assertEquals(0, getEntities(postBattleResponse, "spider").size());
        assertEquals(0, getInventory(postBattleResponse, "sword").size());
        assertEquals(0, getEntities(postBattleResponse, "invisibility_potion").size());
    }

    @Test
    @DisplayName("Test no battle spider with invincibility potion")
    public void testBattleInvincibilityPotion() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse initialResponse = controller.newGame("d_potionsSpiderBattleTest", "c_potionsTest");
        int spiderCount = countEntityOfType(initialResponse, "spider"); 
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, spiderCount);
        List<EntityResponse> potion = getEntities(initialResponse, "invincibility_potion");
        assertEquals(1, potion.size());
        String potionId = potion.get(0).getId();
        assertEquals(0, getInventory(initialResponse, "invincibility_potion").size());

        // Collect the invincibility potion
        DungeonResponse postBattleResponse = controller.tick(Direction.LEFT);
        postBattleResponse = controller.tick(Direction.RIGHT);
        assertEquals(1, getInventory(postBattleResponse, "invincibility_potion").size());

        // Use the invincibility potion
        assertDoesNotThrow(()->controller.tick(potionId));
        postBattleResponse = controller.getDungeonResponseModel();
        assertEquals(0, getInventory(postBattleResponse, "invincibility_potion").size());

        // Push the player into the spider
        postBattleResponse = controller.tick(Direction.UP);
        assertEquals(1, postBattleResponse.getBattles().size());

        BattleResponse battle = postBattleResponse.getBattles().get(0);

        List<RoundResponse> rounds = battle.getRounds();
        assertEquals(1, rounds.size());
        double enemyHealth = Double.parseDouble(getValueFromConfigFile("spider" + "_health", "c_potionsTest"));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), 0);
            assertEquals(round.getDeltaEnemyHealth(), - enemyHealth);
        }

        assertEquals(1, getEntities(postBattleResponse, "player").size());
        assertEquals(0, getEntities(postBattleResponse, "spider").size());
        assertEquals(0, getEntities(postBattleResponse, "invincibility_potion").size());
    }
}
