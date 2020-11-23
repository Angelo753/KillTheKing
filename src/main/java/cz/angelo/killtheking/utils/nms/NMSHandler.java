package cz.angelo.killtheking.utils.nms;

import org.bukkit.entity.Player;

public interface NMSHandler {

    void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle);

    void sendActionBar(Player player, String msg);

}