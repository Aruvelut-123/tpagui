package com.baymaxawa.tpagui;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LangManager {
    private File langFile;
    private FileConfiguration currentLang;
    private final Integer currentLangVersion = 2;

    public String getLangString(String langCode, String... args) {
        String message;
        if (!currentLang.contains(langCode)) return langCode;
        else if (currentLang.getString(langCode) == null) return langCode;
        else if (Objects.equals(currentLang.getString(langCode), "")) return langCode;
        else message = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(currentLang.getString(langCode)));
        if (message.isEmpty()) return langCode;
        if (args.length != 0) {
            int i = 0;
            for (String arg : args) {
                message = message.replace("{"+i+"}", arg);
                i ++;
            }
        }
        return message;
    }

    public List<String> getLangStringList(String langCode, String... args) {
        List<String> message = new ArrayList<>();
        if (!currentLang.contains(langCode)) message.add(langCode);
        else if (currentLang.getStringList(langCode).isEmpty()) message.add(langCode);
        else {
            for (String line : currentLang.getStringList(langCode)) {
                String finalText = "";
                if (args.length != 0) {
                    int i = 0;
                    for (String arg : args) {
                        finalText = line.replace("{"+i+"}", arg);
                        i ++;
                    }
                } else finalText = line;
                if (!Objects.equals(finalText, "")) message.add(ChatColor.translateAlternateColorCodes('&', finalText));
            }
        }
        if (message.isEmpty()) message.add(langCode);
        return message;
    }

    public void initLang(Tpagui plugin, FileConfiguration config) {
        if (config.contains("language")) {
            Path langPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
            langFile = new File(langPath.resolve(Paths.get(config.getString("language")+".yml")).toUri());
            if (!langFile.exists()) {
                loadFallbackLanguage(plugin);
            }
            else {
                currentLang = YamlConfiguration.loadConfiguration(langFile);
                if (!currentLang.contains("lang.version")) Tpagui.logger.warning("Unknown version of language file!");
                if (currentLang.getInt("lang.version") < currentLangVersion) {
                    Tpagui.logger.warning(getLangString("tpagui.langOutdated"));
                }
            }
        }
    }

    public void loadFallbackLanguage(Tpagui plugin) {
        Tpagui.INSTANCE.saveResource("lang/zh_CN.yml", true);
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
                    loadFallbackLanguage(plugin);
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
