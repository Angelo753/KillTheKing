package cz.angelo.killtheking;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Data {

    private static PlayerManager playerManager;

    public static List<String> players = new ArrayList<>();

    public static Player king = null;

    public static HashMap<String, Integer> mapVotes = new HashMap<String, Integer>();

    public static ConfigurationSection arenaSection = Objects.requireNonNull(Config.get("arenas")).getConfigurationSection("arenas");

    //Ints
    public static int startBroadcastSecond = Config.get("config").getInt("startBroadcastSecond");

    public static String lobbyCountdown = String.valueOf(Config.get("config").getInt("timeToStart"));

    public static String playersNeeded = String.valueOf(Config.get("config").getInt("playersNeeded"));

    public static String arenaMaxPlayers = String.valueOf(Config.get("config").getInt("arenaMaxPlayers"));

    public static int mapCounter = 0;

    public static int mapVoteGuiSize = Objects.requireNonNull(Config.get("config")).getInt("mapVoteGui.size");

    //Strings

    public static String stage = "";

    public static String leaveMessage = String.valueOf(Objects.requireNonNull(Config.get("messages")).getString("quitMessage"));

    public static String joinMessage = String.valueOf(Objects.requireNonNull(Config.get("messages")).getString("joinMessage"));

    public static String countDownMessage = Objects.requireNonNull(Config.get("messages")).getString("countDownMessage");

    public static String onlinePlayers = String.valueOf(Bukkit.getOnlinePlayers().size());

    public static List<String> allowedCommands = Objects.requireNonNull(Config.get("config")).getStringList("allowedCommands");

    public static String noPermsMessage = Objects.requireNonNull(Config.get("messages")).getString("noPerms");

    public static String scoreboardWaitTitle = Objects.requireNonNull(Config.get("config")).getString("scoreboard.waiting.title");

    public static String mapWinner = null;

    public static String mapVoteGuiName = Objects.requireNonNull(Config.get("config")).getString("mapVoteGui.name");

    public static String mapVoteMessage = Objects.requireNonNull(Config.get("messages")).getString("mapVote");


}
