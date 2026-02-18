package com.baymaxawa.tpagui;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigurationManager {
    private File configFile;
    public FileConfiguration config;
    private final Integer currentConfigVer = 2;

    public void initConfig(Tpagui plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        if (getInt("version") <= 0) {
            Tpagui.logger.warning("Unknown version of config file!");
        } else if (getInt("version") < currentConfigVer) updateConfig(getInt("version"));
    }

    private void updateConfig(Integer ver) {
        while (getInt("version") < currentConfigVer) {
            if (ver == 1) {
                config.set("settings.velocity.enabled", true);
                List<String> servers = new ArrayList<>();
                servers.add("lobby");
                config.set("settings.velocity.servers", servers);
                config.set("version", 2);
                saveConfig();
            }
        }
    }

    public boolean reloadConfig() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            if (getInt("version") < 1) {
                Tpagui.logger.warning("Unknown version of config file!");
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig() {
        try {
            config.save(configFile);
            reloadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String entry) {
        if (!config.contains(entry) || Objects.equals(config.getString(entry), "")) return entry;
        else return config.getString(entry);
    }

    public Integer getInt(String entry) {
        if (!config.contains(entry)) return 0;
        else return config.getInt(entry);
    }

    public Boolean getBool(String entry) {
        if (!config.contains(entry)) return false;
        else return config.getBoolean(entry);
    }

    public List<String> getStringList(String entry) {
        if (!config.contains(entry)) return new ArrayList<>();
        else return config.getStringList(entry);
    }
}
