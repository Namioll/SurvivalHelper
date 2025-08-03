package me.namioll.survivalHelper;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SurvivalHelperCMD implements CommandExecutor {

    private SurvivalHelper plugin;
    public SurvivalHelperCMD(SurvivalHelper plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            if (cmd.getName().equalsIgnoreCase("sv")) {
                if (plugin.getConfig().getBoolean("players." + sender.getName() + ".admin", false)) {
                    if (args.length == 0) {
                        sendHelp(sender);
                    }
                    else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            plugin.reloadConfig();
                            sender.sendMessage("§aКонфигурация успешно перезагружена.");
                        } else {
                            sendHelp(sender);
                        }
                    }
                    else if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("roots")) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                if (plugin.getConfig().getBoolean("players." + target.getName() + ".roots", false)) {
                                    plugin.getConfig().set("players." + target.getName() + ".roots", false);
                                    sender.sendMessage("§cВы успешно забрали права у игрока.");
                                    plugin.saveConfig();
                                    plugin.reloadConfig();
                                }
                                else {
                                    plugin.getConfig().set("players." + target.getName() + ".roots", true);
                                    sender.sendMessage("§aВы успешно выдали игроку права.");
                                    plugin.saveConfig();
                                    plugin.reloadConfig();
                                }
                            }
                            else {
                                sender.sendMessage("§cОшибка! Игрок не найден.");
                            }
                        }
                    }
                    else {
                        sendHelp(sender);
                    }
                }
                else {
                    sender.sendMessage("§cИзвините, но у вас нет прав! :с");
                }

            }
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§c/sv roots [Ник] - дать/отнять игроку право играть полноценно.");
        sender.sendMessage("§c/sv reload - перезагрузить конфиг.");
    }
}


