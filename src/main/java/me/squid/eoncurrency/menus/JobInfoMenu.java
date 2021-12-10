package me.squid.eoncurrency.menus;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.jobs.Job;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class JobInfoMenu implements Listener {

    Eoncurrency plugin;

    public JobInfoMenu(Eoncurrency plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private Inventory getInventory(Player p, Job job) {
        Inventory inv = Bukkit.createInventory(null, 54,
                Component.text(p.getName() + "'s Stats").color(TextColor.color(0, 255, 255)));
        // TODO Read from config. Action by action and material material and create inventory from there
        return inv;
    }
}
