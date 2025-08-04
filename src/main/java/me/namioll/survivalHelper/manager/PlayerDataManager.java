package me.namioll.survivalHelper.manager;

import me.namioll.survivalHelper.config.ConfigManager;
import me.namioll.survivalHelper.model.PlayerData;
import org.bukkit.entity.Player;

public final class PlayerDataManager {

    private final ConfigManager configManager;

    public PlayerDataManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public PlayerData getOrCreatePlayerData(String playerName) {
        return configManager.getPlayerData(playerName);
    }

    public PlayerData getOrCreatePlayerData(Player player) {
        return getOrCreatePlayerData(player.getName());
    }

    public void initializeNewPlayer(Player player) {
        var playerName = player.getName();
        if (!configManager.playerExists(playerName)) {
            var defaultData = PlayerData.defaultPlayer(playerName, "");
            configManager.savePlayerData(defaultData);
        }
    }

    public boolean togglePlayerRoots(String targetName) {
        var playerData = configManager.getPlayerData(targetName);
        var updatedData = playerData.withRoots(!playerData.hasRoots());
        configManager.savePlayerData(updatedData);
        return updatedData.hasRoots();
    }

    public boolean hasRoots(String playerName) {
        return configManager.getPlayerData(playerName).hasRoots();
    }

    public boolean hasRoots(Player player) {
        return !hasRoots(player.getName());
    }

    public boolean isAdmin(String playerName) {
        return configManager.getPlayerData(playerName).isAdmin();
    }

    public boolean isAdmin(Player player) {
        return !isAdmin(player.getName());
    }
}