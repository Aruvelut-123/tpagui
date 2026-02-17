package com.baymaxawa.tpagui;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class LangManager {
    private File langFile;
    private FileConfiguration currentLang;

    public String getLangString(String langCode) {
        if (!currentLang.contains(langCode)) return langCode;
        else if (currentLang.getString(langCode) == null) return langCode;
        else if (Objects.equals(currentLang.getString(langCode), "")) return langCode;
        else return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(currentLang.getString(langCode)));
    }

    public void initLang(Tpagui plugin, FileConfiguration config) {
        if (config.contains("language")) {
            Path langPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
            langFile = new File(langPath.resolve(Paths.get(config.getString("language")+".yml")).toUri());
            if (!langFile.exists()) {
                loadFallbackLanguage(plugin, config);
            }
            else {
                currentLang = YamlConfiguration.loadConfiguration(langFile);
                if (!currentLang.contains("lang.version") || currentLang.getInt("lang.version") < 1) Tpagui.logger.warning("Unknown version of language file!");
            }
        }
    }

    public void loadFallbackLanguage(Tpagui plugin, FileConfiguration config) {
        Path langPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
        langFile = new File(langPath.resolve(Paths.get("zh_CN.yml")).toUri());
        if (!langFile.exists()) {
            Tpagui.logger.warning("Language files missing!");
            currentLang = null;
        }
        else {
            currentLang = YamlConfiguration.loadConfiguration(langFile);
        }
    }

    public boolean reloadLang(Tpagui plugin, FileConfiguration config) {
        try {
            if (config.contains("language")) {
                Path langPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
                langFile = new File(langPath.resolve(Paths.get(config.getString("language")+".yml")).toUri());
                if (!langFile.exists()) {
                    loadFallbackLanguage(plugin, config);
                }
                else {
                    currentLang = YamlConfiguration.loadConfiguration(langFile);
                    if (!currentLang.contains("lang.version") || currentLang.getInt("lang.version") < 1) Tpagui.logger.warning("Unknown version of language file!");
                }
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
