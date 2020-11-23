package cz.angelo.killtheking.utils.colors;

import org.bukkit.ChatColor;

public class Colored {

    public static String translate(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
