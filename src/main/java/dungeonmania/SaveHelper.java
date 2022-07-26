package dungeonmania;

import com.google.gson.reflect.TypeToken;

import dungeonmania.response.models.DungeonResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.plaf.synth.SynthStyle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveHelper {
    public static void SaveGame(String name, Dungeon response) {
        // Serialization 
        System.out.println(name);

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

    public static Dungeon LoadGame(String name) {
        Dungeon dungeon = null;
  
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

        File folder = new File("src/main/java/dungeonmania/savedgames");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            filenames.add(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4));
            System.out.println(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4));

        }

        return filenames;
    }

    public static void createFolder(String path) {
        File f = new File(path);
            try{
                if(f.mkdir()) { 
                    System.out.println("Directory Created");
                } else {
                    System.out.println("Directory is not created");
                }
            } catch(Exception e){
                e.printStackTrace();
            } 
    }
}