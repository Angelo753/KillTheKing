package cz.angelo.killtheking;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class Config {

    public static FileConfiguration config = null;
    public static FileConfiguration arenas = null;
    public static FileConfiguration messages = null;
    public static File file = null;
    public static File arenasFile = null;
    public static File messagesFile = null;


    public static void reload() {
        if (config == null){
            file = new File(Main.getInstance.getDataFolder(), "config.yml");
        }
        if (arenas == null){
            arenasFile = new File(Main.getInstance.getDataFolder(), "arenas.yml");
        }
        if (messages == null){
            messagesFile = new File(Main.getInstance.getDataFolder(), "messages.yml");
        }
        config = YamlConfiguration.loadConfiguration(file);
        arenas = YamlConfiguration.loadConfiguration(arenasFile);
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        Reader defConfigStream = null;
        Reader defConfigStreamArenas = null;
        Reader defConfigStreamMessages = null;

        if (!file.exists()){
            Main.getInstance.saveResource("config.yml", false);
        }
        if (!arenasFile.exists()){
            Main.getInstance.saveResource("arenas.yml", false);
        }
        if (!messagesFile.exists()){
            Main.getInstance.saveResource("messages.yml", false);
        }

        try {
            defConfigStream = new InputStreamReader(Main.getInstance.getResource("config.yml"), "UTF8");
            defConfigStreamArenas = new InputStreamReader(Main.getInstance.getResource("arenas.yml"), "UTF8");
            defConfigStreamMessages = new InputStreamReader(Main.getInstance.getResource("messages.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null){
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);
        }
        if (defConfigStreamArenas != null){
            YamlConfiguration defConfigArenas = YamlConfiguration.loadConfiguration(defConfigStreamArenas);
            arenas.setDefaults(defConfigArenas);
        }
        if (defConfigStreamMessages != null){
            YamlConfiguration defConfigMessages = YamlConfiguration.loadConfiguration(defConfigStreamMessages);
            arenas.setDefaults(defConfigMessages);
        }
    }

    public static FileConfiguration get(String s) {
        if (config == null || arenas == null || messages == null){
            reload();
        }
        switch (s){
            case "config":
                return config;
            case "arenas":
                return arenas;
            case "messages":
                return messages;
        }
        return null;
    }

    public static void save(){
        if (config == null || file == null){
            return;
        }
        try {
            get("config").save(file);
            get("arenas").save(arenasFile);
            get("messages").save(messagesFile);
        }catch (IOException e){
        }
    }

}
