package me.namioll.survivalHelper;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatMessages implements Listener {

    private SurvivalHelper plugin;
    public ChatMessages(SurvivalHelper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        String message = e.getMessage();
        String prefix = plugin.getConfig().getString("players." + name + ".prefix", "&7Игрок");
        String title = plugin.getConfig().getString("players." + name + ".title", "");
        Boolean admin = plugin.getConfig().getBoolean("players." + name + ".admin", false);
        Boolean root = plugin.getConfig().getBoolean("players." + name + ".roots", false);
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        title = ChatColor.translateAlternateColorCodes('&', title);
        String formatted;
        if (admin) {
            formatted = prefix + "§6 " + name;
        }
        else if (!root) {
            formatted = prefix + "§8 " + name;
        }
        else {
            formatted = prefix + "§f " + name;
        }

        if (!title.isEmpty()) {
            formatted += "§f " + title;
        }
        formatted += "§8§l:§f " + message;
        e.setFormat(formatted);
    }
}
