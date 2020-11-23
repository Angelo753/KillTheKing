package cz.angelo.killtheking;

import cz.angelo.killtheking.utils.colors.Colored;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerScoreboard {

    public static ScoreboardManager m;
    public static org.bukkit.scoreboard.Scoreboard sb;
    public static Objective o;

    public static void setScoreboard(Player p){
        m = Bukkit.getScoreboardManager();
        sb = m.getNewScoreboard();
        o = sb.registerNewObjective("aaa", "bbb");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        Team ktk = sb.registerNewTeam("ktk");
        ktk.addPlayer(p);
        o.setDisplayName(Colored.translate(PlaceholderAPI.setPlaceholders(p, Data.scoreboardWaitTitle)));
        List<String> data = Config.get("config").getStringList("scoreboard.waiting.data");
        for (int i = 0; i < data.size(); i++) {
            ktk.addEntry(Colored.translate(PlaceholderAPI.setPlaceholders(p, data.get(i))));
            o.getScore(Colored.translate(PlaceholderAPI.setPlaceholders(p, data.get(i)))).setScore(i);
        }
        ktk.setSuffix("");
        ktk.setPrefix("");
        new BukkitRunnable(){
            public void run() {
                if (!Bukkit.getOnlinePlayers().contains(p)) {
                    this.cancel();
                }
                o.setDisplayName(Colored.translate(Config.get("config").getString("scoreboard.waiting.title")));
                List<String> data = Config.get("config").getStringList("scoreboard.waiting.data");
                for (int i = 0; i < data.size(); i++) {
                    ktk.addEntry(Colored.translate(PlaceholderAPI.setPlaceholders(p, data.get(i))));
                    o.getScore(Colored.translate(PlaceholderAPI.setPlaceholders(p, data.get(i)))).setScore(i);
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance, 0L, 20L);
        p.setScoreboard(sb);
    }

}
