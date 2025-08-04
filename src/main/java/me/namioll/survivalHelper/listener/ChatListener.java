package me.namioll.survivalHelper.listener;

import me.namioll.survivalHelper.manager.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class ChatListener implements Listener {

    private final PlayerDataManager playerManager;

    public ChatListener(PlayerDataManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();
        var playerData = playerManager.getOrCreatePlayerData(player);
        var message = event.getMessage();

        var formattedMessage = playerData.getFormattedChatName() + message;
        event.setFormat(formattedMessage);
    }
}