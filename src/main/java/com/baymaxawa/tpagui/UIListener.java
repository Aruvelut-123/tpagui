package com.baymaxawa.tpagui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class UIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Tpagui plugin = Tpagui.INSTANCE;
        if (event.getClickedInventory() == null ||
            !event.getClickedInventory().equals(event.getView().getTopInventory())) return;

        String title = plugin.lm.getLangString("tpagui.title");
        int placeholderIndex = title.indexOf("{0}");
        if (placeholderIndex == -1) return;
        String prefix = title.substring(0, placeholderIndex);

        if (event.getView().getTitle().startsWith(prefix)) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player player)) return;

            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            int currentPage = getPageFromTitle(event.getView().getTitle());
            if (currentPage <= 0) return;

            int pageIndex = currentPage - 1;
            if (clicked.getType() == Material.ARROW) {
                ItemMeta meta = clicked.getItemMeta();
                if (meta == null || !meta.hasDisplayName()) return;
                String name = meta.getDisplayName();
                String nextPageString = plugin.lm.getLangString("tpagui.nextPage");
                String prevPageString = plugin.lm.getLangString("tpagui.previousPage");
                if (name.equals(nextPageString)) player.openInventory(UIManager.createTpaMenu(player, pageIndex + 1));
                else if (name.equals(prevPageString)) if (pageIndex > 0) player.openInventory(UIManager.createTpaMenu(player, pageIndex - 1));
            } else if (clicked.getType() == Material.PLAYER_HEAD) {
                ItemMeta meta = clicked.getItemMeta();
                if (!(meta instanceof SkullMeta)) return;
                SkullMeta skullMeta = (SkullMeta) meta;
                String target = null;
                if (skullMeta.getOwningPlayer() != null) target = skullMeta.getOwningPlayer().getName();
                else if(skullMeta.hasDisplayName()) {
                    String displayName = ChatColor.stripColor(skullMeta.getDisplayName());
                    target = displayName.trim();
                }
                if (target == null || target.isEmpty()) {
                    player.sendMessage(plugin.lm.getLangString("tpagui.playerOffline"));
                    return;
                }
                String tpaCommand = plugin.cm.getString("settings.tpa-command.to");
                String tpaHereCommand = plugin.cm.getString("settings.tpa-command.here");
                String command = event.isLeftClick() ? tpaCommand + " " + target : tpaHereCommand + " " + target;
                Tpagui.logger.info(plugin.lm.getLangString("tpagui.executed", player.getName(), command));
                player.chat("/"+command);
                player.closeInventory();
            }
        } else if (event.getView().getTitle().startsWith(plugin.lm.getLangString("tpagui.requestTitle"))) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player player)) return;

            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR || clicked.getType() == Material.PLAYER_HEAD) return;

            if (clicked.getType() == Material.TORCH) player.chat("/"+plugin.cm.getString("settings.tpa-command.accept"));
            else if (clicked.getType() == Material.REDSTONE_TORCH) player.chat("/"+plugin.cm.getString("settings.tpa-command.deny"));
            player.closeInventory();
        }
    }

    private int getPageFromTitle(String title) {
        try {
            String template = Tpagui.INSTANCE.lm.getLangString("tpagui.title");
            int placeholderIndex = template.indexOf("{0}");
            if (placeholderIndex == -1) return 0;
            String prefix = template.substring(0, placeholderIndex);
            String suffix = template.substring(placeholderIndex + 3);

            if (title.startsWith(prefix) && title.endsWith(suffix)) {
                String pageStr = title.substring(prefix.length(), title.length() - suffix.length());
                int page = Integer.parseInt(pageStr.trim());
                return Math.max(page, 0);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            Tpagui.logger.warning(Tpagui.INSTANCE.lm.getLangString(
                    "tpagui.extractFailed", title
            ));
        }
        return 0;
    }
}
