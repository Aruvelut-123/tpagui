package com.baymaxawa.tpagui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class TpaRequestListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();

        List<String> listenedCommands = Tpagui.INSTANCE.cm.getStringList("settings.listening-commands");

        boolean isListenedCommand = false;
        for (String cmd : listenedCommands) {
            if (command.startsWith("/"+cmd.toLowerCase()+" ")) {
                isListenedCommand = true;
                break;
            }
        }

        if (!isListenedCommand) return;

        String[] args = event.getMessage().split(" ");
        if (args.length < 2) return;

        String target = args[1];
        Player player = event.getPlayer().getServer().getPlayer(target);
        String tpaHereCommand = Tpagui.INSTANCE.cm.getString("settings.tpa-command.here");
        boolean isTpaHere = command.startsWith("/"+tpaHereCommand+" ");
        if (player == null) return;

        if (Tpagui.INSTANCE.cm.getBool("settings.forms.java.listen")) {
            Inventory inv = Bukkit.createInventory(null, 27,
                    Tpagui.INSTANCE.lm.getLangString("tpagui.requestTitle"));

            ItemStack playerHead = UIManager.getPlayerSkull(event.getPlayer());
            if (playerHead != null) {
                SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(isTpaHere ? Tpagui.INSTANCE.lm.getLangString("tpagui.tpaHereTitle", event.getPlayer().getName()) : Tpagui.INSTANCE.lm.getLangString("tpagui.tpaTitle", event.getPlayer().getName()));
                if (meta != null) {
                    meta.setLore(lore);
                    playerHead.setItemMeta(meta);
                }
            }
            inv.setItem(4, playerHead);
            ItemStack confirmButton = new ItemStack(Material.TORCH, 1);
            ItemMeta confirmMeta = confirmButton.getItemMeta();
            if (confirmMeta != null) {
                confirmMeta.setDisplayName(Tpagui.INSTANCE.lm.getLangString("tpagui.confirm"));
                confirmButton.setItemMeta(confirmMeta);
            }
            inv.setItem(20, confirmButton);
            ItemStack cancelButton = new ItemStack(Material.REDSTONE_TORCH, 1);
            ItemMeta cancelMeta = confirmButton.getItemMeta();
            if (cancelMeta != null) {
                cancelMeta.setDisplayName(Tpagui.INSTANCE.lm.getLangString("tpagui.cancel"));
                cancelButton.setItemMeta(cancelMeta);
            }
            inv.setItem(24, cancelButton);
            player.openInventory(inv);
        }
        //TODO: Bedrock support
    }
}
