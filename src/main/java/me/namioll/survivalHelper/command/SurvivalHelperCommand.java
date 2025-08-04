package me.namioll.survivalHelper.command;

import me.namioll.survivalHelper.config.ConfigManager;
import me.namioll.survivalHelper.manager.PlayerDataManager;
import me.namioll.survivalHelper.service.LocalizationService;
import me.namioll.survivalHelper.service.MessageService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class SurvivalHelperCommand implements CommandExecutor, TabCompleter {

    private final PlayerDataManager playerManager;
    private final ConfigManager configManager;
    private final MessageService messageService;
    private final LocalizationService localizationService;

    public SurvivalHelperCommand(PlayerDataManager playerManager,
                                 ConfigManager configManager,
                                 MessageService messageService,
                                 LocalizationService localizationService) {
        this.playerManager = playerManager;
        this.configManager = configManager;
        this.messageService = messageService;
        this.localizationService = localizationService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            messageService.sendPlayerOnly(sender);
            return true;
        }

        if (!playerManager.isAdmin(player)) {
            messageService.sendNoPermission(sender);
            return true;
        }

        return switch (args.length) {
            case 1 -> handleSingleArgument(sender, args[0]);
            case 2 -> handleDoubleArgument(sender, args[0], args[1]);
            default -> {
                messageService.sendHelp(sender);
                yield true;
            }
        };
    }

    private boolean handleSingleArgument(CommandSender sender, String arg) {
        switch (arg.toLowerCase()) {
            case "reload" -> {
                configManager.reloadConfig();
                messageService.sendConfigReloaded(sender);
            }
            case "lang", "language" -> {
                messageService.sendCurrentLanguage(sender);
                messageService.sendAvailableLanguages(sender);
            }
            default -> messageService.sendHelp(sender);
        }
        return true;
    }

    private boolean handleDoubleArgument(CommandSender sender, String command, String argument) {
        return switch (command.toLowerCase()) {
            case "roots" -> handleRootsCommand(sender, argument);
            case "lang", "language" -> handleLanguageCommand(sender, argument);
            default -> {
                messageService.sendHelp(sender);
                yield true;
            }
        };
    }

    private boolean handleRootsCommand(CommandSender sender, String targetName) {
        var target = Bukkit.getPlayer(targetName);
        if (target == null) {
            messageService.sendPlayerNotFound(sender);
            return true;
        }

        boolean newRootsStatus = playerManager.togglePlayerRoots(target.getName());

        if (newRootsStatus) {
            messageService.sendRootsGranted(sender);
        } else {
            messageService.sendRootsRevoked(sender);
        }

        return true;
    }

    private boolean handleLanguageCommand(CommandSender sender, String language) {
        if (!localizationService.isLanguageAvailable(language)) {
            messageService.sendLanguageNotFound(sender, language);
            return true;
        }

        localizationService.setLanguage(language);
        messageService.sendLanguageChanged(sender, language);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player player) || !playerManager.isAdmin(player)) {
            return List.of();
        }

        return switch (args.length) {
            case 1 -> Stream.of("roots", "reload", "lang", "language")
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .toList();
            case 2 -> switch (args[0].toLowerCase()) {
                case "roots" -> Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .toList();
                case "lang", "language" -> Arrays.stream(localizationService.getAvailableLanguages())
                        .filter(lang -> lang.toLowerCase().startsWith(args[1].toLowerCase()))
                        .toList();
                default -> List.of();
            };
            default -> List.of();
        };
    }
}
