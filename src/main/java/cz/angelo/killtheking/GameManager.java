package cz.angelo.killtheking;

import cz.angelo.killtheking.utils.colors.Colored;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager implements Listener {

    private int lobbyCountdown = Integer.parseInt(Data.lobbyCountdown);
    private int playersNeeded = Integer.parseInt(Data.playersNeeded);

    private boolean isStarted = false;

    Location lobbySpawn;
    Location kingSpawn;
    Location murderSpawn;

    public void setupGame(){
        ConfigurationSection arenas = Config.get("arenas").getConfigurationSection("arenas");
        if (!arenas.getKeys(false).isEmpty()){
            //Lobby spawn
            double xlobby = Config.get("arenas").getDouble("arenas.test.lobby.x");
            double ylobby = Config.get("arenas").getDouble("arenas.test.lobby.y");
            double zlobby = Config.get("arenas").getDouble("arenas.test.lobby.z");
            float yawlobby = Config.get("arenas").getInt("arenas.test.lobby.yaw");
            float pitchlobby = Config.get("arenas").getInt("arenas.test.lobby.pitch");
            World wlobby = Bukkit.getWorld(Config.get("arenas").getString("arenas.test.lobby.world"));

            //King spawn
            double xking = Config.get("arenas").getDouble("arenas.test.king.x");
            double yking = Config.get("arenas").getDouble("arenas.test.king.y");
            double zking = Config.get("arenas").getDouble("arenas.test.king.z");
            float yawking = Config.get("arenas").getInt("arenas.test.king.yaw");
            float pitchking = Config.get("arenas").getInt("arenas.test.king.pitch");
            World wking = Bukkit.getWorld(Config.get("arenas").getString("arenas.test.king.world"));

            //Murder spawn
            double xmurder = Config.get("arenas").getDouble("arenas.test.murder.x");
            double ymurder = Config.get("arenas").getDouble("arenas.test.murder.y");
            double zmurder = Config.get("arenas").getDouble("arenas.test.murder.z");
            float yawmurder = Config.get("arenas").getInt("arenas.test.murder.yaw");
            float pitchmurder = Config.get("arenas").getInt("arenas.test.murder.pitch");
            World wmurder = Bukkit.getWorld(Config.get("arenas").getString("arenas.test.murder.world"));

            if (Bukkit.getWorlds().contains(wlobby) && Bukkit.getWorlds().contains(wking) && Bukkit.getWorlds().contains(wmurder)){
                //Set locations
                this.lobbySpawn = new Location(wlobby, xlobby, ylobby, zlobby, yawlobby, pitchlobby);
                this.kingSpawn = new Location(wking, xking, yking, zking, yawking, pitchking);
                this.murderSpawn = new Location(wmurder, xmurder, ymurder, zmurder, yawmurder, pitchmurder);
            }else {
                //This world doesn't exists
            }
        }
    }

    public void lobbyWait(Player p){
        int online = Bukkit.getOnlinePlayers().size();
        p.teleport(this.lobbySpawn);
        playerCheck(online);
    }

    public void gameStart(){
        for (Player p : Bukkit.getOnlinePlayers()){
            getKing();
            if (p != Data.king) {
                p.teleport(this.murderSpawn);
            }else {
                p.teleport(this.kingSpawn);
            }
        }
        setStarted(true);
    }

    public void getKing(){
        double highest = 0.00;
        Player p = null;
        for (Map.Entry<UUID, PlayerManager> entry : Main.getInstance.playerManager.entrySet()){
            if (entry.getValue().getChance() >= highest){
                highest = entry.getValue().getChance();
                p = Bukkit.getPlayer(entry.getValue().getUuid());
            }
        }
        Data.king = p;
    }

    public void gameStop(){

    }

    public void getMapWinner() {
        int highest = 0;
        String mapWinner = null;
        for (Map.Entry<String, Integer> entry : Data.mapVotes.entrySet()) {
            if (entry.getValue() >= highest) {
                highest = entry.getValue();
                mapWinner = entry.getKey();
            }
        }
        Data.mapWinner = mapWinner;
    }

    public void lobbyCountdown(){
        new BukkitRunnable(){
            public void run() {
                if (lobbyCountdown > 0) {
                    if (Bukkit.getOnlinePlayers().size() < playersNeeded){
                        lobbyCountdown = Integer.parseInt(Data.lobbyCountdown);
                        for (Player player :Bukkit.getOnlinePlayers()){
                            player.setLevel(lobbyCountdown);
                        }
                        this.cancel();
                    }else {
                        for (Player p : Bukkit.getOnlinePlayers()){
                            p.setLevel(lobbyCountdown);
                            if (lobbyCountdown <= Data.startBroadcastSecond) {
                                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                                p.sendMessage(Colored.translate(PlaceholderAPI.setPlaceholders(p, Data.countDownMessage)));
                            }
                        }
                        lobbyCountdown--;
                    }
                }else {
                    gameStart();
                    Data.stage = "In-Game";
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance, 0, 20);
    }

    public void playerCheck(int online) {
        if (online == playersNeeded) {
            this.lobbyCountdown();
        }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = true;
    }

    public static void sendActionbar(Player p){
        if (!Main.getInstance.gameManager.isStarted()){
            double chanceKing = Main.getInstance.playerManager.get(p.getUniqueId()).getChance();
            double chanceMurder = 100.00 - Main.getInstance.playerManager.get(p.getUniqueId()).getChance();
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Sance: " + Math.round(chanceKing * 100.0) / 100.0 + "/" + Math.round(chanceMurder * 100.0) / 100.0));
        }
    }

    public void mapVoteGui(Player p){
        Inventory inv = Bukkit.getServer().createInventory(null, Data.mapVoteGuiSize, Colored.translate(Data.mapVoteGuiName));
        for (String arena : Data.arenaSection.getKeys(false)){
            Material material = Material.valueOf(Config.get("arenas").getString("arenas." + arena + ".gui.item"));
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            List<String> loreList = Config.get("arenas").getStringList("arenas." + arena + ".gui.lore");
            List<String> lore = new ArrayList<>();
            for (int i = 0; i < loreList.size(); i++){
                lore.add(Colored.translate(PlaceholderAPI.setPlaceholders(p, loreList.get(i))));
            }
            meta.setDisplayName(Colored.translate(Config.get("arenas").getString("arenas." + arena + ".gui.name")));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(Config.get("arenas").getInt("arenas." + arena + ".gui.slot"), item);
        }
        p.openInventory(inv);
    }

}
