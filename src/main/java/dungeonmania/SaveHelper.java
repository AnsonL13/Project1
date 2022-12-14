package dungeonmania;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveHelper {
    public static void SaveGame(String name, Dungeon response) {
        // Serialization 

        try {
            //Saving of object in a file
            createFolder("src/main/java/dungeonmania/savedgames/");
            FileOutputStream file = new FileOutputStream("src/main/java/dungeonmania/savedgames/" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
              
            // Method for serialization of object
            out.writeObject(response);
              
            out.close();
            file.close();
              
        }
        
        catch(IOException ex) {
            System.out.println("IOException is caught " + ex);
        }
    }

    public static Dungeon LoadGame(String name) throws IllegalArgumentException {
        Dungeon dungeon = null;

        List<String> allGames = showAllGame();

        // Check if id is not a valid game name
        if (! allGames.contains(name)) {
            throw new IllegalArgumentException();
        }
  
        // Deserialization
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream("src/main/java/dungeonmania/savedgames/" + name + ".ser");
            ObjectInputStream in = new ObjectInputStream(file);
              
            // Method for deserialization of object
            dungeon = (Dungeon) in.readObject();
              
            in.close();
            file.close();
              
        }
          
        catch(IOException ex) {
            System.out.println("IOException is caught");
        }
          
        catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }

        return dungeon;
    }

    public static List<String> showAllGame() {
        List<String> filenames = new ArrayList<String>();

        try {
            File folder = new File("src/main/java/dungeonmania/savedgames");
            File[] listOfFiles = folder.listFiles();

            // Get the filenames from the folder.
            for (int i = 0; i < listOfFiles.length; i++) {
                // Edit the filename to extract the name of the dungeon only. 
                filenames.add(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4));
            }
        }

        catch (NullPointerException ex) {
            return filenames;
        }

        return filenames;
    }

    /*
     * Creates a folder to store saved dungeons if there is no folder to begin with. 
     */
    public static void createFolder(String path) {
        File f = new File(path);
            try{
                f.mkdir();
                
            } catch(Exception e){
                e.printStackTrace();
            } 
    }
}
