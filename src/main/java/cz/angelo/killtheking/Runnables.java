package cz.angelo.killtheking;

import cz.angelo.killtheking.utils.colors.Colored;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class Runnables extends BukkitRunnable {

    Main plugin;

    public static ScoreboardManager m;
    public static org.bukkit.scoreboard.Scoreboard sb;
    public static Objective o;

    public Runnables(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        //Scoreboard
        for (Player p : Bukkit.getOnlinePlayers()) {
            GameManager.sendActionbar(p);
            m = Bukkit.getScoreboardManager();
            sb = m.getNewScoreboard();
            o = sb.registerNewObjective("aaa", "bbb");
            o.setDisplaySlot(DisplaySlot.SIDEBAR);
            Team ktk = sb.registerNewTeam("ktk");
            o.setDisplayName(Colored.translate(PlaceholderAPI.setPlaceholders(p, Data.scoreboardWaitTitle)));
            List<String> data = Config.get("config").getStringList("scoreboard.waiting.data");
            for (int i = 0; i < data.size(); i++) {
                ktk.addEntry(Colored.translate(PlaceholderAPI.setPlaceholders(p, data.get(i))));
                o.getScore(Colored.translate(PlaceholderAPI.setPlaceholders(p, data.get(i)))).setScore(i);
            }
            ktk.setSuffix("");
            ktk.setPrefix("");
            o.setDisplayName(Colored.translate(Config.get("config").getString("scoreboard.waiting.title")));
            p.setScoreboard(sb);
        }



    }
}

