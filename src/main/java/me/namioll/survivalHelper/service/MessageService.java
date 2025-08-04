package me.namioll.survivalHelper.service;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class MessageService {

    private final LocalizationService localizationService;

    public MessageService(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    public void sendNoPermission(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("messages.no-permission"));
    }

    public void sendPlayerNotFound(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("messages.player-not-found"));
    }

    public void sendConfigReloaded(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("messages.config-reloaded"));
    }

    public void sendRootsGranted(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("messages.roots-granted"));
    }

    public void sendRootsRevoked(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("messages.roots-revoked"));
    }

    public void sendNoRootsPermission(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("messages.no-roots-permission"));
    }

    public void sendConsoleOnly(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("messages.console-only"));
    }

    public void sendPlayerOnly(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("messages.player-only"));
    }

    public void sendHelp(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("help.main"));
    }

    public void sendLanguageChanged(CommandSender sender, String language) {
        sendMessage(sender, localizationService.getMessage("language.changed", "language", language));
    }

    public void sendLanguageNotFound(CommandSender sender, String language) {
        String availableLanguages = String.join(", ", localizationService.getAvailableLanguages());
        sendMessage(sender, localizationService.getMessage("language.not-found",
                "language", language,
                "languages", availableLanguages));
    }

    public void sendCurrentLanguage(CommandSender sender) {
        sendMessage(sender, localizationService.getMessage("language.current",
                "language", localizationService.getCurrentLanguage()));
    }

    public void sendAvailableLanguages(CommandSender sender) {
        String availableLanguages = String.join(", ", localizationService.getAvailableLanguages());
        sendMessage(sender, localizationService.getMessage("language.available",
                "languages", availableLanguages));
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public String getMessage(String key, String... placeholders) {
        return localizationService.getMessage(key, placeholders);
    }
}
