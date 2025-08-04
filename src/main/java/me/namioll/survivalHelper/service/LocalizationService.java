package me.namioll.survivalHelper.service;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

public final class LocalizationService {

    private final JavaPlugin plugin;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final ConcurrentHashMap<String, YamlConfiguration> languageCache = new ConcurrentHashMap<>();

    private String currentLanguage = "ru";
    private static final String DEFAULT_LANGUAGE = "ru";
    private static final String LANGUAGES_FOLDER = "languages";

    public LocalizationService(JavaPlugin plugin) {
        this.plugin = plugin;
        initializeLocalization();
    }

    private void initializeLocalization() {
        createLanguagesFolder();
        createDefaultLanguageFiles();
        loadLanguageFromConfig();
        loadAllLanguages();
    }

    private void createLanguagesFolder() {
        File languagesDir = new File(plugin.getDataFolder(), LANGUAGES_FOLDER);
        if (!languagesDir.exists()) {
            languagesDir.mkdirs();
        }
    }

    private void createDefaultLanguageFiles() {
        // Создаем файл ru.yml если его нет
        File ruFile = new File(plugin.getDataFolder(), LANGUAGES_FOLDER + "/ru.yml");
        if (!ruFile.exists()) {
            try (InputStream inputStream = plugin.getResource("languages/ru.yml")) {
                if (inputStream != null) {
                    Files.copy(inputStream, ruFile.toPath());
                } else {
                    // Создаем стандартный файл ru.yml
                    createDefaultRussianFile(ruFile);
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING, "Не удалось создать файл ru.yml", e);
                createDefaultRussianFile(ruFile);
            }
        }

        // Создаем файл en.yml для примера
        File enFile = new File(plugin.getDataFolder(), LANGUAGES_FOLDER + "/en.yml");
        if (!enFile.exists()) {
            createDefaultEnglishFile(enFile);
        }
    }

    private void createDefaultRussianFile(File file) {
        YamlConfiguration config = new YamlConfiguration();

        // Messages
        config.set("messages.no-permission", "§cИзвините, но у вас нет прав! :с");
        config.set("messages.player-not-found", "§cОшибка! Игрок не найден.");
        config.set("messages.config-reloaded", "§aКонфигурация успешно перезагружена.");
        config.set("messages.roots-granted", "§aВы успешно выдали игроку права.");
        config.set("messages.roots-revoked", "§cВы успешно забрали права у игрока.");
        config.set("messages.no-roots-permission", "§cВы не можете сделать это, пока администратор не посчитает Вас честным игроком.");
        config.set("messages.console-only", "§cЭта команда доступна только из консоли!");
        config.set("messages.player-only", "§cЭта команда доступна только игрокам!");

        // Help messages
        config.set("help.main", """
            §6==== SurvivalHelper Помощь ====
            §c/sv roots [Ник] §7- дать/отнять игроку право играть полноценно
            §c/sv reload §7- перезагрузить конфигурацию
            §c/sv lang [язык] §7- изменить язык плагина
            §6================================""");

        config.set("help.roots", "§c/sv roots [Ник] §7- дать/отнять игроку право играть полноценно");
        config.set("help.reload", "§c/sv reload §7- перезагрузить конфигурацию");
        config.set("help.language", "§c/sv lang [язык] §7- изменить язык плагина");

        // Language messages
        config.set("language.changed", "§aЯзык успешно изменен на: §f{language}");
        config.set("language.not-found", "§cЯзык '{language}' не найден! Доступные языки: {languages}");
        config.set("language.current", "§eТекущий язык: §f{language}");
        config.set("language.available", "§eДоступные языки: §f{languages}");

        // Chat formats
        config.set("chat.prefix.default", "&7Игрок");
        config.set("chat.format.admin", "{prefix}§6 {name}");
        config.set("chat.format.no-roots", "{prefix}§8 {name}");
        config.set("chat.format.with-roots", "{prefix}§f {name}");

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Не удалось сохранить файл ru.yml", e);
        }
    }

    private void createDefaultEnglishFile(File file) {
        YamlConfiguration config = new YamlConfiguration();

        // Messages
        config.set("messages.no-permission", "§cSorry, but you don't have permission! :c");
        config.set("messages.player-not-found", "§cError! Player not found.");
        config.set("messages.config-reloaded", "§aConfiguration successfully reloaded.");
        config.set("messages.roots-granted", "§aYou have successfully granted rights to the player.");
        config.set("messages.roots-revoked", "§cYou have successfully revoked rights from the player.");
        config.set("messages.no-roots-permission", "§cYou cannot do this until an administrator considers you a trusted player.");
        config.set("messages.console-only", "§cThis command is only available from console!");
        config.set("messages.player-only", "§cThis command is only available to players!");

        // Help messages
        config.set("help.main", """
            §6==== SurvivalHelper Help ====
            §c/sv roots [Nick] §7- give/take player rights to play fully
            §c/sv reload §7- reload configuration
            §c/sv lang [language] §7- change plugin language
            §6==============================""");

        config.set("help.roots", "§c/sv roots [Nick] §7- give/take player rights to play fully");
        config.set("help.reload", "§c/sv reload §7- reload configuration");
        config.set("help.language", "§c/sv lang [language] §7- change plugin language");

        // Language messages
        config.set("language.changed", "§aLanguage successfully changed to: §f{language}");
        config.set("language.not-found", "§cLanguage '{language}' not found! Available languages: {languages}");
        config.set("language.current", "§eCurrent language: §f{language}");
        config.set("language.available", "§eAvailable languages: §f{languages}");

        // Chat formats
        config.set("chat.prefix.default", "&7Player");
        config.set("chat.format.admin", "{prefix}§6 {name}");
        config.set("chat.format.no-roots", "{prefix}§8 {name}");
        config.set("chat.format.with-roots", "{prefix}§f {name}");

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save en.yml file", e);
        }
    }

    private void loadLanguageFromConfig() {
        String configLanguage = plugin.getConfig().getString("language", DEFAULT_LANGUAGE);
        if (isLanguageAvailable(configLanguage)) {
            this.currentLanguage = configLanguage;
        } else {
            plugin.getLogger().warning("Language '" + configLanguage + "' not found, using default: " + DEFAULT_LANGUAGE);
            this.currentLanguage = DEFAULT_LANGUAGE;
        }
    }

    private void loadAllLanguages() {
        lock.writeLock().lock();
        try {
            File languagesDir = new File(plugin.getDataFolder(), LANGUAGES_FOLDER);
            File[] languageFiles = languagesDir.listFiles((dir, name) -> name.endsWith(".yml"));

            if (languageFiles != null) {
                for (File file : languageFiles) {
                    String language = file.getName().replace(".yml", "");
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    languageCache.put(language, config);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String getMessage(String key, String... placeholders) {
        lock.readLock().lock();
        try {
            YamlConfiguration langConfig = languageCache.get(currentLanguage);
            if (langConfig == null) {
                langConfig = languageCache.get(DEFAULT_LANGUAGE);
            }

            if (langConfig == null) {
                return "§cMissing language configuration!";
            }

            String message = langConfig.getString(key, "§cMissing translation: " + key);

            // Replace placeholders
            if (placeholders.length > 0) {
                for (int i = 0; i < placeholders.length; i += 2) {
                    if (i + 1 < placeholders.length) {
                        message = message.replace("{" + placeholders[i] + "}", placeholders[i + 1]);
                    }
                }
            }

            return message;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setLanguage(String language) {
        if (isLanguageAvailable(language)) {
            this.currentLanguage = language;
            // Обновляем конфиг
            plugin.getConfig().set("language", language);
            plugin.saveConfig();
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public boolean isLanguageAvailable(String language) {
        return languageCache.containsKey(language);
    }

    public String[] getAvailableLanguages() {
        return languageCache.keySet().toArray(new String[0]);
    }

    public void reloadLanguages() {
        loadLanguageFromConfig();
        loadAllLanguages();
    }
}