package com.baymaxawa.tpagui;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigurationManager {
    private File configFile;
    public FileConfiguration config;

    public void initConfig(Tpagui plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.contains("version") || config.getInt("version") < 1) {
            Tpagui.logger.warning("Unknown version of config file!");
        }
    }

    public boolean reloadConfig() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            if (!config.contains("version") || config.getInt("version") < 1) {
                Tpagui.logger.warning("Unknown version of config file!");
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
