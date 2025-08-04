package me.namioll.survivalHelper.listener;

import me.namioll.survivalHelper.manager.PlayerDataManager;
import me.namioll.survivalHelper.service.MessageService;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;

public final class PlayerPermissionListener implements Listener {

    private final PlayerDataManager playerManager;
    private final MessageService messageService;

    private static final Set<Material> RESTRICTED_BREAK_BLOCKS = Set.of(
            Material.CHEST,
            Material.SHULKER_BOX,
            Material.ENDER_CHEST,
            Material.TRAPPED_CHEST
    );

    private static final Set<Material> RESTRICTED_PLACE_BLOCKS = Set.of(
            Material.TNT,
            Material.HOPPER,
            Material.DROPPER,
            Material.DISPENSER
    );

    private static final Set<Material> RESTRICTED_INTERACT_BLOCKS = Set.of(
            Material.CHEST,
            Material.SHULKER_BOX,
            Material.ENDER_CHEST,
            Material.TRAPPED_CHEST,
            Material.HOPPER,
            Material.DROPPER,
            Material.DISPENSER
    );

    public PlayerPermissionListener(PlayerDataManager playerManager, MessageService messageService) {
        this.playerManager = playerManager;
        this.messageService = messageService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerManager.initializeNewPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();
        var blockType = event.getBlock().getType();

        if (RESTRICTED_BREAK_BLOCKS.contains(blockType) && playerManager.hasRoots(player)) {
            event.setCancelled(true);
            messageService.sendNoRootsPermission(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        var blockType = event.getBlock().getType();

        if (RESTRICTED_PLACE_BLOCKS.contains(blockType) && playerManager.hasRoots(player)) {
            event.setCancelled(true);
            messageService.sendNoRootsPermission(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            return;
        }

        var player = event.getPlayer();
        var blockType = event.getClickedBlock().getType();

        if (RESTRICTED_INTERACT_BLOCKS.contains(blockType) && playerManager.hasRoots(player)) {
            event.setCancelled(true);
            messageService.sendNoRootsPermission(player);
        }
    }
}
