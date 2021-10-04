package me.squid.eoncurrency.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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

    public static void playHarpSoundAtPlayer(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
    }

    public static void playBassSoundAtPlayer(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
    }

    public static Component getPrefix(String name) {
        TextColor gray = TextColor.color(128, 128, 128);
        TextColor color = TextColor.color(102, 178, 255);
        return switch (name) {
            case "admin" -> Component.text("[").color(gray)
                    .append(Component.text("Eon Admin").color(color)
                            .append(Component.text("] ").color(gray)));
            case "nations" -> Component.text("[").color(gray)
                    .append(Component.text("Eon Nations").color(color)
                            .append(Component.text("] ").color(gray)));
            case "moderation" -> Component.text("[").color(gray)
                    .append(Component.text("Eon Moderation").color(color)
                            .append(Component.text("]").color(gray)));
            default -> Component.text("Invalid");
        };
    }

    public static double round(double exp, int places) {
        BigDecimal bd = new BigDecimal(exp, MathContext.DECIMAL32);
        bd = bd.setScale(places, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    public static double getDoubleLevel(double experience) {
        double exp = 0.5 * Math.sqrt(experience);
        if (exp < 1) return 1;
        else return exp;
    }
}
