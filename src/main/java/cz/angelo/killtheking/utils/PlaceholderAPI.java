package cz.angelo.killtheking.utils;

import cz.angelo.killtheking.Config;
import cz.angelo.killtheking.Data;
import cz.angelo.killtheking.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPI extends PlaceholderExpansion {

    private Main plugin;

    public PlaceholderAPI(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "ktk";
    }

    @Override
    public String getAuthor() {
        return "Angelo753";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (identifier.equals("time_to_start")){
            return String.valueOf(Data.lobbyCountdown);
        }
        if (identifier.equals("arena_max")){
            return Data.arenaMaxPlayers;
        }
        if (identifier.equals("stage")){
            return Data.stage;
        }
        if (identifier.equals("online")){
            return String.valueOf(Main.getInstance.playersInGame.size() + 1);
        }
        for (String mapVotes : Data.arenaSection.getKeys(false)){
            if (identifier.equals("mapVotes_" + mapVotes)){
                return String.valueOf(Data.mapVotes.get(Config.get("arenas").get("arenas." + mapVotes).toString()));
            }
        }

        if (identifier.equals("start_countdown")){
            String s = String.valueOf(player.getPlayer().getLevel());
            return s;
        }
        return null;
    }
}
