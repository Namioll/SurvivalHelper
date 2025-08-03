package me.namioll.survivalHelper;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class NewPlayerPermission implements Listener {

    private SurvivalHelper plugin;
    public NewPlayerPermission(SurvivalHelper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        if (!plugin.getConfig().contains("players." + name)) {
            plugin.getConfig().set("players." + name + ".prefix", "&7Игрок");
            plugin.getConfig().set("players." + name + ".title", "");
            plugin.getConfig().set("players." + name + ".roots", false);
            plugin.getConfig().set("players." + name + ".admin", false);
            plugin.saveConfig();
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        Material block = e.getBlock().getType();
        if ((block == Material.CHEST)||  (block == Material.SHULKER_BOX)) {
            if (!plugin.getConfig().getBoolean("players." + name + ".roots", false)) {
                e.setCancelled(true);
                p.sendMessage("§cВы не можете сделать это, пока администратор не посчитает Вас честным игроком.");
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        if (e.getClickedBlock() != null) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.CHEST) || (e.getClickedBlock().getType() == Material.SHULKER_BOX)) {
                if (!plugin.getConfig().getBoolean("players." + name + ".roots", false)) {
                    e.setCancelled(true);
                    p.sendMessage("§cВы не можете сделать это, пока администратор не посчитает Вас честным игроком.");
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        Material block = e.getBlock().getType();
        if ((block == Material.TNT)  || (block == Material.HOPPER)) {
            if (!plugin.getConfig().getBoolean("players." + name + ".roots", false)) {
                e.setCancelled(true);
                p.sendMessage("§cВы не можете сделать это, пока администратор не посчитает Вас честным игроком.");
            }
        }
    }
}