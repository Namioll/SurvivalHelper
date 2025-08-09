package me.namioll.survivalHelper;

import me.namioll.survivalHelper.command.SurvivalHelperCommand;
import me.namioll.survivalHelper.config.ConfigManager;
import me.namioll.survivalHelper.listener.ChatListener;
import me.namioll.survivalHelper.listener.PlayerPermissionListener;
import me.namioll.survivalHelper.manager.PlayerDataManager;
import me.namioll.survivalHelper.service.LocalizationService;
import me.namioll.survivalHelper.service.MessageService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class SurvivalHelper extends JavaPlugin {

    private LocalizationService localizationService;
    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private MessageService messageService;

    @Override
    public void onEnable() {
        try {
            initializeServices();
            registerCommands();
            registerEventListeners();

            getLogger().info("SurvivalHelper плагин успешно загружен!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Ошибка при загрузке плагина", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (configManager != null) {
            configManager.saveConfig();
        }
        getLogger().info("SurvivalHelper плагин отключен.");
    }

    private void initializeServices() {
        this.localizationService = new LocalizationService(this);
        this.configManager = new ConfigManager(this, localizationService);
        this.messageService = new MessageService(localizationService);
        this.playerDataManager = new PlayerDataManager(configManager);
    }

    private void registerCommands() {
        var command = getCommand("sv");
        if (command != null) {
            var executor = new SurvivalHelperCommand(playerDataManager, configManager, messageService, localizationService);
            command.setExecutor(executor);
            command.setTabCompleter(executor);
        }
    }

    private void registerEventListeners() {
        var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ChatListener(playerDataManager), this);
        pluginManager.registerEvents(new PlayerPermissionListener(playerDataManager, messageService), this);
    }

    // Getters for dependency injection
    public LocalizationService getLocalizationService() { return localizationService; }
    public ConfigManager getConfigManager() { return configManager; }
    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    public MessageService getMessageService() { return messageService; }
}
