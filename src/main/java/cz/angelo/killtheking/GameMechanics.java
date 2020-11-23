package cz.angelo.killtheking;

import cz.angelo.killtheking.utils.colors.Colored;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.*;

public class GameMechanics implements Listener {

    PlayerManager playerManager;

    //Player join
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        //Join messages
        e.setJoinMessage(Colored.translate(PlaceholderAPI.setPlaceholders(p, Data.joinMessage)));

        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(20);
        p.setExp(0);
        p.setLevel(Integer.parseInt(Data.lobbyCountdown));

        //Register player
        Main.getInstance.playersInGame.add(p);
        Main.getInstance.gameManager.lobbyWait(p);
        Main.getInstance.playerManager.put(uuid, new PlayerManager(uuid, false, 0, false, 50.00, false));

        //Title/Subtitle
        if (Config.get("messages").getBoolean("join.join_title.enabled")) {
            int fadeIn = Config.get("messages").getInt("join.join_title.fadeIn");
            int stay = Config.get("messages").getInt("join.join_title.stay");
            int fadeOut = Config.get("messages").getInt("join.join_title.fadeOut");
            String title = Colored.translate(PlaceholderAPI.setPlaceholders(p, Config.get("messages").getString("join.join_title.title")));
            String subtitle = Colored.translate(PlaceholderAPI.setPlaceholders(p, Config.get("messages").getString("join.join_title.subtitle")));
            Main.nms.sendTitle(p, fadeIn, stay, fadeOut, title, subtitle);
        }

        //MapVoteGui
        Main.getInstance.gameManager.mapVoteGui(p);

    }

    //PlayerQuit
    @EventHandler
    public static void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        UUID uuid = p .getUniqueId();

        //Unregister player
        Main.getInstance.playerManager.remove(uuid);
        Main.getInstance.playersInGame.remove(p);
        Main.getInstance.playersLeft.add(p);

        //Leave message
        e.setQuitMessage(Colored.translate(PlaceholderAPI.setPlaceholders(p, Data.leaveMessage)));

    }

    @EventHandler
    public static void blockBreak(BlockBreakEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public static void blockPlace(BlockPlaceEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public static void itemDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public static void itemPickup(PlayerPickupItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public static void interactEvent(PlayerInteractEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public static void inventoryClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if (e.getView().getTitle().equals(Colored.translate(Config.get("config").getString("mapVoteGui.name")))) {
            for (String arenas : Data.arenaSection.getKeys(false)) {
                int slot = Config.get("arenas").getInt("arenas." + arenas + ".gui.slot");
                if (e.getSlot() == slot) {
                    p.sendMessage("4");
                    for (String mapVotes : Data.arenaSection.getKeys(false)){
                        int votes = Data.mapVotes.get(Config.get("arenas").get("arenas." + mapVotes).toString());
                        Data.mapVotes.replace(Config.get("arenas").get("arenas." + mapVotes).toString(), votes + 1);
                    }

                    p.sendMessage(Colored.translate(PlaceholderAPI.setPlaceholders(p, Data.mapVoteMessage)));
                    break;
                }
            }

        }
    }

    @EventHandler
    public static void foodChange(FoodLevelChangeEvent e){
        if (!Main.getInstance.gameManager.isStarted()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void entityDamage(EntityDamageEvent e){
        if (!Main.getInstance.gameManager.isStarted()) {
            if (e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public static void cmdTypeEvent(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        String preCmd = "";
        for (int i = 0; i < Data.allowedCommands.size(); i++){
            preCmd = "/" + preCmd + Data.allowedCommands.get(i) + " ";
        }
        if (!preCmd.contains(e.getMessage())) {
            e.setCancelled(true);
            p.sendMessage(Colored.translate(PlaceholderAPI.setPlaceholders(p, Data.noPermsMessage)));
        }
    }

}
