package cz.angelo.killtheking.utils.nms;

import cz.angelo.killtheking.utils.colors.Colored;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Handler_1_16R3 implements NMSHandler {

    @Override
    public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle){
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeIn);
        connection.sendPacket(packet);
        if (title != null){
            title = Colored.translate(title);
            IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }
        if (subtitle != null){
            subtitle = Colored.translate(subtitle);
            IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
            connection.sendPacket(packetPlayOutTitle);
        }
    }

    @Override
    public void sendActionBar(Player player, String msg) {
        try {
            Constructor<?> constructor = PacketPlayOutChat.class.getConstructor(IChatBaseComponent.class, byte.class);
            Object icbc = IChatBaseComponent.class.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + msg + "\"}");
            Object packet = constructor.newInstance(icbc, (byte) 2);
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            connection.getClass().getMethod("sendPacket", Packet.class).invoke(connection, packet);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

}
