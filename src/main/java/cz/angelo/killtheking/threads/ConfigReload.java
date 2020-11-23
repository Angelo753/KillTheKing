package cz.angelo.killtheking.threads;

import cz.angelo.killtheking.Config;
import cz.angelo.killtheking.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class ConfigReload extends BukkitRunnable {

    Main plugin;

    public ConfigReload(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Config.reload();
    }

}
