package com.baymaxawa.tpagui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MyCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (Objects.equals(args[0], "reload")) {
                    if (!player.hasPermission("tpagui.reload")) {
                        player.sendMessage(Tpagui.INSTANCE.lm.getLangString("tpagui.noPermission"));
                        return true;
                    }
                    if (Tpagui.INSTANCE.reload()) player.sendMessage(Tpagui.INSTANCE.lm.getLangString("tpagui.reloaded"));
                    else player.sendMessage(Tpagui.INSTANCE.lm.getLangString("tpagui.reloadFailed"));
                    return true;
                } else return false;
            }
            if (Tpagui.INSTANCE.cm.getBool("settings.forms.java.basic")) player.openInventory(UIManager.createTpaMenu(player, 0));
            else player.sendMessage(Tpagui.INSTANCE.lm.getLangString("tpagui.uiDisabled"));
        } else {
            if (args.length == 1) {
                if (Objects.equals(args[0], "reload")) {
                    if (Tpagui.INSTANCE.reload()) sender.sendMessage(Tpagui.INSTANCE.lm.getLangString("tpagui.reloaded"));
                    else sender.sendMessage(Tpagui.INSTANCE.lm.getLangString("tpagui.reloadFailed"));
                    return true;
                } else return false;
            }
            sender.sendMessage(Tpagui.INSTANCE.lm.getLangString("tpagui.playerOnly"));
        }
        return true;
    }
}
