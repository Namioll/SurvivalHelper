package me.namioll.survivalHelper.model;

import org.bukkit.ChatColor;

public record PlayerData(
        String name,
        String prefix,
        String title,
        boolean hasRoots,
        boolean isAdmin
) {

    public static PlayerData defaultPlayer(String name, String defaultPrefix) {
        return new PlayerData(name, defaultPrefix, "", false, false);
    }

    public PlayerData withRoots(boolean roots) {
        return new PlayerData(name, prefix, title, roots, isAdmin);
    }

    public PlayerData withAdmin(boolean admin) {
        return new PlayerData(name, prefix, title, hasRoots, admin);
    }

    public String getColoredPrefix() {
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public String getColoredTitle() {
        return ChatColor.translateAlternateColorCodes('&', title);
    }

    public String getDisplayName() {
        var colored = getColoredPrefix();
        if (isAdmin) {
            return colored + "§6 " + name;
        } else if (!hasRoots) {
            return colored + "§8 " + name;
        } else {
            return colored + "§f " + name;
        }
    }

    public String getFormattedChatName() {
        var display = getDisplayName();
        var coloredTitle = getColoredTitle();

        if (!coloredTitle.isEmpty()) {
            display += "§f " + coloredTitle;
        }

        return display + "§8§l:§f ";
    }
}
