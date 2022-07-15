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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class BattleTest2 {

    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath, int roundAmount) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10));
            assertEquals(round.getDeltaEnemyHealth(), -(playerAttack / 5));
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
    @DisplayName("Test simple zombie battle")
    public void testZombieDie() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse initialResponse = controller.newGame("d_zombieBattleTest", "c_battleTest_superInvisiblePotion");
        int zombieCount = countEntityOfType(initialResponse, "zombie_toast"); 
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, zombieCount);
        
        // Push the player into the zombie
        DungeonResponse postBattleResponse = controller.tick(Direction.RIGHT);
        BattleResponse battle = postBattleResponse.getBattles().get(0);

        assertBattleCalculations("zombie", battle, true, "c_battleTest_superInvisiblePotion", 5);
        assertEquals(1, getEntities(postBattleResponse, "player").size());
        assertEquals(0, getEntities(postBattleResponse, "zombie_toast").size());
    }

    @Test
    @DisplayName("Test zombie battle with sword")
    public void testZombieBattleWithSword() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse initialResponse = controller.newGame("d_battleZombiewithSword", "c_battleTest_superInvisiblePotion");
        int zombieCount = countEntityOfType(initialResponse, "zombie_toast"); 
        assertEquals(1, countEntityOfType(initialResponse, "sword"));
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, zombieCount);
        
        // Push the player into the zombie
        DungeonResponse postBattleResponse = controller.tick(Direction.RIGHT);
        BattleResponse battle = postBattleResponse.getBattles().get(0);

        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", "c_battleTest_superInvisiblePotion"));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile("zombie" + "_health", "c_battleTest_superInvisiblePotion"));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", "c_battleTest_superInvisiblePotion"));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile("zombie" + "_attack", "c_battleTest_superInvisiblePotion"));

        double sword = Double.parseDouble(getValueFromConfigFile("sword" + "_attack", "c_battleTest_superInvisiblePotion"));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10));
            assertEquals(round.getDeltaEnemyHealth(), -((playerAttack + sword) / 5));
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();    
        }

        assertTrue(enemyHealth <= 0);
        assertTrue(playerHealth > 0);

        assertEquals(4, rounds.size());
        
        assertEquals(1, getEntities(postBattleResponse, "player").size());
        assertEquals(0, getEntities(postBattleResponse, "zombie_toast").size());
        assertEquals(0, getEntities(postBattleResponse, "sword").size());
    }
}