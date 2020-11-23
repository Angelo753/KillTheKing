package cz.angelo.killtheking;

import cz.angelo.killtheking.utils.PlaceholderAPI;
import cz.angelo.killtheking.utils.nms.Handler_1_16R3;
import cz.angelo.killtheking.utils.nms.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public HashMap<UUID,PlayerManager> playerManager = new HashMap<UUID, PlayerManager>();
    public ArrayList<Player> playersInGame = new ArrayList<Player>();
    public ArrayList<Player> playersLeft = new ArrayList<Player>();

    public static Main getInstance;
    public GameMechanics gameMechanics;
    public GameManager gameManager;
    public static NMSHandler nms;

    private static String version;
    private Runnables stage;

    @Override
    public void onLoad(){
        for (Player p : Bukkit.getOnlinePlayers()){
            p.getInventory().clear();
            this.playerManager.put(p.getUniqueId(), new PlayerManager(p.getUniqueId(), false, 0, false, 50.00, false));
            this.playersInGame.add(p);
        }
        Data.stage = "Waiting";
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new GameMechanics(), this);
        NMSRegister();
        pluginRegister();
        registerExpansion();
        instanceClasses();
        BukkitTask runnables = new Runnables(this).runTaskTimer(this, 0L, 20L);
        this.gameManager.setupGame();
        for (String arena : Data.arenaSection.getKeys(false)){
            Data.mapVotes.put(Objects.requireNonNull(Config.get("arenas")).get("arenas." + arena).toString(), 0);
            Data.mapCounter++;
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void pluginRegister(){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null){
            this.getLogger().warning("Tento plugin potrebuje PlaceholderAPI.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void instanceClasses(){
        getInstance = this;
        gameMechanics = new GameMechanics();
        gameManager = new GameManager();
    }

    public void registerExpansion() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        }
    }

    public static void NMSRegister(){
        version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        if (version.contains("1_16")){
            nms = new Handler_1_16R3();
        }
    }

}
