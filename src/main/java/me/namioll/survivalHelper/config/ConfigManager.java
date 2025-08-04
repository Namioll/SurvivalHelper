package me.namioll.survivalHelper.config;

import me.namioll.survivalHelper.model.PlayerData;
import me.namioll.survivalHelper.service.LocalizationService;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ConfigManager {

    private final JavaPlugin plugin;
    private final LocalizationService localizationService;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final ConcurrentHashMap<String, PlayerData> cache = new ConcurrentHashMap<>();

    public ConfigManager(JavaPlugin plugin, LocalizationService localizationService) {
        this.plugin = plugin;
        this.localizationService = localizationService;
        initializeConfig();
    }

    private void initializeConfig() {
        plugin.getConfig().options().copyDefaults(true);

        // Устанавливаем значения по умолчанию
        plugin.getConfig().addDefault("language", "ru");
        plugin.getConfig().addDefault("chat.enable-custom-format", true);
        plugin.getConfig().addDefault("permissions.new-player-roots", false);
        plugin.getConfig().addDefault("restrictions.blocks.break", new String[]{"CHEST", "SHULKER_BOX", "ENDER_CHEST", "TRAPPED_CHEST"});
        plugin.getConfig().addDefault("restrictions.blocks.place", new String[]{"TNT", "HOPPER", "DROPPER", "DISPENSER"});
        plugin.getConfig().addDefault("restrictions.blocks.interact", new String[]{"CHEST", "SHULKER_BOX", "ENDER_CHEST", "TRAPPED_CHEST", "HOPPER", "DROPPER", "DISPENSER"});

        plugin.saveDefaultConfig();
        loadCache();
    }

    public void reloadConfig() {
        lock.writeLock().lock();
        try {
            plugin.reloadConfig();
            cache.clear();
            loadCache();
            localizationService.reloadLanguages();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void saveConfig() {
        lock.readLock().lock();
        try {
            plugin.saveConfig();
        } finally {
            lock.readLock().unlock();
        }
    }

    private void loadCache() {
        var config = plugin.getConfig();
        var playersSection = config.getConfigurationSection("players");

        if (playersSection != null) {
            for (var playerName : playersSection.getKeys(false)) {
                var playerData = loadPlayerData(playerName, config);
                cache.put(playerName, playerData);
            }
        }
    }

    private PlayerData loadPlayerData(String name, FileConfiguration config) {
        var basePath = "players." + name + ".";
        String defaultPrefix = localizationService.getMessage("chat.prefix.default");

        return new PlayerData(
                name,
                config.getString(basePath + "prefix", defaultPrefix),
                config.getString(basePath + "title", ""),
                config.getBoolean(basePath + "roots", false),
                config.getBoolean(basePath + "admin", false)
        );
    }

    public PlayerData getPlayerData(String name) {
        lock.readLock().lock();
        try {
            return cache.computeIfAbsent(name, this::createDefaultPlayer);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void savePlayerData(PlayerData playerData) {
        lock.writeLock().lock();
        try {
            var config = plugin.getConfig();
            var basePath = "players." + playerData.name() + ".";

            config.set(basePath + "prefix", playerData.prefix());
            config.set(basePath + "title", playerData.title());
            config.set(basePath + "roots", playerData.hasRoots());
            config.set(basePath + "admin", playerData.isAdmin());

            cache.put(playerData.name(), playerData);
            saveConfig();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private PlayerData createDefaultPlayer(String name) {
        var defaultData = PlayerData.defaultPlayer(name, localizationService.getMessage("chat.prefix.default"));
        savePlayerData(defaultData);
        return defaultData;
    }

    public boolean playerExists(String name) {
        lock.readLock().lock();
        try {
            return plugin.getConfig().contains("players." + name);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getLanguage() {
        return plugin.getConfig().getString("language", "ru");
    }
}