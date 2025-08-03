package me.namioll.survivalHelper;

import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalHelper extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getCommand("sv").setExecutor(new SurvivalHelperCMD(this));
        getServer().getPluginManager().registerEvents(new ChatMessages(this), this);
        getServer().getPluginManager().registerEvents(new NewPlayerPermission(this), this);

    }
}
