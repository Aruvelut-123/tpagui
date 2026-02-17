package com.baymaxawa.tpagui;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Tpagui extends JavaPlugin {
    public static Logger logger;
    public static Tpagui INSTANCE;
    private final ConfigurationManager cm = new ConfigurationManager();
    public final LangManager lm = new LangManager();

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        logger = getLogger();
        saveResource("config.yml", false);
        saveResource("lang/zh_CN.yml", true);
        // saveResource("lang/en_US.yml", false);
        cm.initConfig(this);
        lm.initLang(this, cm.config);
        if (getCommand("tpagui") != null) {
            getCommand("tpagui").setExecutor(new MyCommandExecutor());
            getCommand("tpagui").setTabCompleter(new MyTabCompleter());
            logger.info(lm.getLangString("tpagui.commandRegistered"));
        }
        logger.info(lm.getLangString("tpagui.loaded"));
    }

    @Override
    public void onDisable() {
        logger.info(lm.getLangString("tpagui.disable"));
    }

    public boolean reload() {
        return cm.reloadConfig() && lm.reloadLang(this, cm.config);
    }
}
