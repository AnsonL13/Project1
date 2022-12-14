package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DungeonManiaController {

    private Dungeon dungeon;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        String dungeonsString = null;
        String configsString = null;

        // Get the file
        try {
            dungeonsString = FileLoader.loadResourceFile("dungeons/" + dungeonName + ".json");
            configsString = FileLoader.loadResourceFile("configs/" + configName + ".json");
        }
        catch(Exception IOException) {
            throw new IllegalArgumentException();
        }

        // Turn the String into a JsonObject
        JsonObject dungeonJson = JsonParser.parseString(dungeonsString).getAsJsonObject();
        JsonObject configJson = JsonParser.parseString(configsString).getAsJsonObject();

        // Create the new dungeon
        this.dungeon = new Dungeon(dungeonName, dungeonJson, configJson);

        return getDungeonResponseModel();
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {

        // Get all entities.
        List<EntityResponse> entities = new ArrayList<EntityResponse>();
        for (Entity entity : dungeon.getEntities()) {                    
            EntityResponse entityResponse = new EntityResponse(entity.getId(), entity.getType(), entity.getPosition(), entity.isInteractable());
            entities.add(entityResponse);
        }
        
        // Get all items in the players inventory
        List<ItemResponse> inventory = new ArrayList<ItemResponse>();
        for (Item item : dungeon.getInventory()) {
            ItemResponse itemResponse = new ItemResponse(item.getId(), item.getType());
            inventory.add(itemResponse);
        }

        // Get all the battles that have happened
        List<BattleResponse> battles = new ArrayList<BattleResponse>();
        for (Battle battle : dungeon.getBattles()) {

            List<RoundResponse> roundResponses = new ArrayList<RoundResponse>();
            for (Round round : battle.getRounds()) {

                List<ItemResponse> weaponryUsed = new ArrayList<ItemResponse>();
                for (Item item : round.getWeaponryUsed()) {
                    if (round.getWeaponryUsed() == null) break;
                    ItemResponse itemResponse = new ItemResponse(item.getId(), item.getType());
                    weaponryUsed.add(itemResponse);
                }

                RoundResponse roundResponse = new RoundResponse(round.getDeltaCharacterHealth(), round.getDeltaEnemyHealth(), weaponryUsed);
                roundResponses.add(roundResponse);

            }

            BattleResponse battleResponse = new BattleResponse(battle.getEnemy(), roundResponses, battle.getInitialPlayerHealth(), battle.getInitialEnemyHealth());
            battles.add(battleResponse);
        }

        List<String> buildables = dungeon.getBuildables();

        // Get the list of incomplete goals
        String goals = dungeon.getGoals().listIncompleteGoals();

        // Get Animation
        List<AnimationQueue> animations = dungeon.getAnimations();

        DungeonResponse newDungeonResponse = new DungeonResponse(dungeon.getDungeonId(), dungeon.getDungeonName(), entities,
        inventory, battles, buildables, goals, animations);
        
        return newDungeonResponse;
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        dungeon.tick(itemUsedId);
        return getDungeonResponseModel();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        dungeon.tick(movementDirection);
        return getDungeonResponseModel();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        dungeon.build(buildable);
        return getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        dungeon.interact(entityId);
        return getDungeonResponseModel();
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        SaveHelper.SaveGame(name, this.dungeon);
        return getDungeonResponseModel();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        this.dungeon = SaveHelper.LoadGame(name);
        return getDungeonResponseModel();        
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return SaveHelper.showAllGame();
    }

    /**
     * /game/rewind
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        dungeon.rewind(ticks);
        return getDungeonResponseModel();        
    }

    /**
     * /game/new/generate
     */
    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String configName) throws IllegalArgumentException {
        DungeonGenerator dungeonGenerator = new DungeonGenerator(xStart, yStart, xEnd, yEnd, configName);
        this.dungeon = dungeonGenerator.generateDungeon();

        return getDungeonResponseModel();
    }
}
