package me.squid.eoncurrency.utils;

import me.squid.eoncurrency.Eoncurrency;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Utils {
    private Utils() { }
    private static final String PAY_PREFIX = "<gray>[<blue><bold>EonEco</bold></blue>] ";

    public static @NotNull String chat (String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static Component configMessage(Eoncurrency plugin, String path, Map<String, String> config) {
        String rawMessage = Optional.ofNullable(plugin.getConfig().getString(path)).orElse("Path not found");
        for (Map.Entry<String, String> entry : config.entrySet()) {
            rawMessage = rawMessage.replace(entry.getKey(), entry.getValue());
        }
        return MiniMessage.miniMessage().deserialize(PAY_PREFIX + rawMessage);
    }

    @Deprecated(forRemoval = true)
    public static ItemStack createItem(Inventory inv, Material material, int amount, int invSlot, String displayName, String... loreString){
        ItemStack item = new ItemStack(material, amount);
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
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(""));
        item.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, item);
            }
        }
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
}
