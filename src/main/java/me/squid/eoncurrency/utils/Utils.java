package me.squid.eoncurrency.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Utils {

    public static String chat (String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static ItemStack createItem(Inventory inv, Material material, int amount, int invSlot, String displayName, String... loreString){
        ItemStack item;
        item = new ItemStack(material, amount);
        List<String> lore = new ArrayList<>(Arrays.asList(loreString));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Utils.chat(displayName));
        item.setItemMeta(meta);
        item.setLore(lore);
        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static void makeDummySlots(Inventory inv) {
        ItemStack item;
        item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("");
        item.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, item);
            }
        }
    }

}
