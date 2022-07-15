package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.BuildableEntities.Bow;
import dungeonmania.BuildableEntities.Shield;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.MovingEntities.ZombieToast;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
public class BattleTest {
    // White box testing

    @Test
    @DisplayName("Test zombie battle invicible")
    public void testZombieInvicible() {
        // Create zombie and set player is invincible
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 10, 10, intial);
        zombie.setInvincible(5);

        //Create player
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 5, 5);

        // Create battle
        Battle result = zombie.battleCalculate(player);

        //Check expected results
        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(1, result.getRounds().size());
    } 

    @Test
    @DisplayName("Test simple zombie battle")
    public void testZombieDie() {
        // Create zombie and player
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 1, 1, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 5, 5);

        Battle result = zombie.battleCalculate(player);

        // check results
        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(1, result.getRounds().size());
    } 

    @Test
    @DisplayName("Test simple player dies")
    public void testPlayerDie() {
        // Create zombie and player
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 5, 5, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 1, 1);

        Battle result = zombie.battleCalculate(player);

        // check results
        assertFalse(result.isPlayerWon());
        assertTrue(result.isEnemyWon());
        assertEquals(2, result.getRounds().size());
    } 

    @Test
    @DisplayName("Test player battle sword")
    public void testPlayerHasSwordBattle() {
        // Create zombie and player
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 1, 1, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 4, 5);
        
        //Add sword and cal battle after sword
        player.addToWeapons(new Sword("0", "sword", intial, false, 1, 1));
        Battle result = zombie.battleCalculate(player);

        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(1, result.getRounds().size());
    }
    
    @Test
    @DisplayName("Test player battle bow")
    public void testPlayerHasBowBattle() {
        // Create zombie and player with box added to inventory
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 1, 1, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 5, 5);
        player.addToWeapons(new Bow("1", "bow", false, 1));

        Battle result = zombie.battleCalculate(player);

        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(1, result.getRounds().size());
    } 

    @Test
    @DisplayName("Test player battle shield")
    public void testPlayerHasShield() {
        // Create zombie and player with sheild added to inventory
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 10, 1, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 1, 1);
        player.addToWeapons(new Shield("1", "shield", false, 10, 1));

        // check player wins battle dure to shield
        Battle result = zombie.battleCalculate(player);

        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(5, result.getRounds().size());
    } 

    @Test
    @DisplayName("Test player battle bow and sword")
    public void testPlayerHasBowSword() {
        // Create zombie and player with bow and sword added to inventory
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 1, 10, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 4, 1);
        player.addToWeapons(new Sword("0", "sword", intial, false, 1, 1));
        player.addToWeapons(new Bow("1", "bow", false, 1));

        // check that calculations are correct
        Battle result = zombie.battleCalculate(player);

        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(5, result.getRounds().size());
    }
    
    @Test
    @DisplayName("Test player battle bow and sword but Zombie Win")
    public void testPlayerHasBowSwordZombieWin() {
        // Create zombie and player with bow and sword added to inventory
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 2, 10, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 2, 1);
        player.addToWeapons(new Sword("0", "sword", intial, false, 1, 1));
        player.addToWeapons(new Bow("1", "bow", false, 1));

        // check that calculations are correct
        Battle result = zombie.battleCalculate(player);

        assertFalse(result.isPlayerWon());
        assertTrue(result.isEnemyWon());
        assertEquals(5, result.getRounds().size());
    } 

    @Test
    @DisplayName("Test player battle lost after durability over")
    public void testPlayerLoseSword() {
        // Create zombie and player with sword added to inventory
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 1, 10, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 2, 1);
        player.addToWeapons(new Sword("0", "sword", intial, false, 8, 1));

        // check that calculations are correct
        Battle result = zombie.battleCalculate(player);
        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(5, result.getRounds().size());

        // check sword is gone
        assertEquals(0, player.getPlayerWeapons().size());        

        //create another battle where player loses
        ZombieToast zombieTwo = new ZombieToast("0", 2, 10, intial);
        result = zombieTwo.battleCalculate(player);
        assertFalse(result.isPlayerWon());
        assertTrue(result.isEnemyWon());
        assertEquals(3, result.getRounds().size());
    } 

    @Test
    @DisplayName("Test player battle lost after durability over")
    public void testPlayerLoseShield() {
        // Create zombie and player with shield added to inventory
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 10, 1, intial);
        Player player = new Player("1", "player", intial.translateBy(Direction.DOWN), false, 3, 1);
        player.addToWeapons(new Shield("1", "shield", false, 10, 1));

        // check that calculations are correct

        Battle result = zombie.battleCalculate(player);
        assertTrue(result.isPlayerWon());
        assertFalse(result.isEnemyWon());
        assertEquals(2, result.getRounds().size());

        //create another battle where player loses
        assertEquals(0, player.getPlayerWeapons().size());        
        ZombieToast zombieTwo = new ZombieToast("0", 10, 1, intial);
        result = zombieTwo.battleCalculate(player);
        assertFalse(result.isPlayerWon());
        assertTrue(result.isEnemyWon());
        assertEquals(1, result.getRounds().size());
    } 
}
