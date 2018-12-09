package net.prosavage.savagefactionsdefaultpermissions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.annotation.PostConstruct;
import java.io.File;

public class DefaultPermissions extends JavaPlugin {


    public static DefaultPermissions instance;


    @Override
    public void onEnable() {

        // This gets an instance of a plugin called factions.
        Plugin factions = this.getServer().getPluginManager().getPlugin("Factions");
        // Checks if the factions plugin in the server
        if (factions == null) {
            getLogger().info("There is no faction's plugin on the server.");
            disablePlugin();
            return;
        }
        // Checks if the installed factions plugin is SavageFactions.
        if (!factions.getDescription().getAuthors().contains("ProSavage")) {
            getLogger().info("There is a factions plugin installed, but this plugin requires SavageFactions.");
            disablePlugin();
            return;
        }

        instance = this;

        getLogger().info("Enabling SavageFactions Default Permissions Addon.");
        if (!checkIfConfigExists()) {
            getLogger().info("Configuration file was not found!");
            getLogger().info("Creating config.yml...");
            saveResource("config.yml", true);
        } else {
            getLogger().info("Configuration File Found!");
        }

        getLogger().info("Registering Listener");
        getServer().getPluginManager().registerEvents(new FactionCreateEventListener(), this);


    }

    @Override
    public void onDisable() {

    }


    private void disablePlugin() {
        getLogger().info("Disabling Addon.");
        getServer().getPluginManager().disablePlugin(this);
    }

    private boolean checkIfConfigExists() {
        File configFile = new File(getDataFolder(), "config.yml");
        return configFile.exists();

    }








}
