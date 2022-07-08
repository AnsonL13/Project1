package dungeonmania;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import java.util.List;



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
        List<EntityResponse> entities = null;
        List<ItemResponse> inventory = null;
        List<BattleResponse> battles = null;
        List<String> buildables = null;
        String goals = null;

        DungeonResponse newDungeonResponse = new DungeonResponse(dungeon.getDungeonId(), dungeon.getDungeonName(), entities,
        inventory, battles, buildables, goals);

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
}
