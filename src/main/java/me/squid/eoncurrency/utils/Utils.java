package me.squid.eoncurrency.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static @NotNull String chat (String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Deprecated
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

    public static ItemStack createItem(Inventory inv, Material material, int amount, int invSlot, Component displayName, List<Component> loreComponents){
        ItemStack item;
        item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(displayName);
        item.setItemMeta(meta);
        item.lore(loreComponents);
        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static ItemStack createItem(Inventory inv, Material material, int amount, int invSlot, Component displayName){
        ItemStack item;
        item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(displayName);
        item.setItemMeta(meta);
        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static void makeDummySlots(Inventory inv) {
        ItemStack item;
        item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(""));
        item.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, item);
            }
        }
    }
}
