package com.baymaxawa.tpagui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private static final int ROWS = 6;

    public static Inventory createTpaMenu(Player player, int page) {
        Tpagui plugin = Tpagui.INSTANCE;
        int playersPerPage = plugin.cm.getInt("settings.players-per-page");
        if (playersPerPage > 45) {
            playersPerPage = 45;
            Tpagui.logger.warning(plugin.lm.getLangString("tpagui.playersPerPageLarge"));
        }
        Inventory inv = Bukkit.createInventory(null, ROWS * 9,
                plugin.lm.getLangString(
                        "tpagui.title", String.valueOf(page + 1)));
        List<Object> players = new ArrayList<>();
        if (plugin.cm.getBool("settings.integrate.huskhomes")) {

        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getUniqueId().equals(player.getUniqueId())) players.add(p);
            }
        }
        int totalPlayers = players.size();
        int totalPage = totalPlayers > 0 ? ((totalPlayers - 1) / playersPerPage + 1) : 1;

        int startIndex = page * playersPerPage;
        int slotIndex = 0;
        for (int i = startIndex; i < players.size() && slotIndex < playersPerPage; i++) {
            Object target = players.get(i);
            ItemStack skull = null;
            if (target instanceof Player) skull = getPlayerSkull((Player) target);
            if (skull != null && skull.getItemMeta() != null) {
                inv.setItem(slotIndex, skull);
                slotIndex++;
            }
        }
        if (page > 0) inv.setItem(45, createNavigationButton(
                plugin.lm.getLangString("tpagui.previousPage")
        ));
        if (totalPlayers > 0 && page < totalPage - 1) inv.setItem(53, createNavigationButton(
                plugin.lm.getLangString("tpagui.nextPage")
        ));
        return inv;
    }

    private static ItemStack createNavigationButton(String name) {
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack getPlayerSkull(Player player) {
        Tpagui plugin = Tpagui.INSTANCE;
        if (player == null || !player.isOnline()) {
            Tpagui.logger.warning(plugin.lm.getLangString("tpagui.offlineSkull"));
            return null;
        }

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta == null) {
            Tpagui.logger.warning(plugin.lm.getLangString("tpagui.skullMetaFailed"));
            return null;
        }

        try {
            meta.setOwningPlayer(player);
        } catch (Exception e) {
            Tpagui.logger.warning(
                    plugin.lm.getLangString("tpagui.skullOwnerFailed",
                        player.getName(), e.getMessage()
                    )
            );
            return null;
        }

        meta.setDisplayName(player.getName());

        List<String> lore = new ArrayList<>(plugin.lm.getLangStringList("tpagui.skullLore"));
        meta.setLore(lore);

        skull.setItemMeta(meta);
        return skull;
    }
}
