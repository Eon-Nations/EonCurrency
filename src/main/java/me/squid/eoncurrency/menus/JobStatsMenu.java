package me.squid.eoncurrency.menus;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.managers.JobsManager;
import me.squid.eoncurrency.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class JobStatsMenu implements Listener {

    Eoncurrency plugin;

    public JobStatsMenu(Eoncurrency plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJobStatsClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().equals(getInventory(p))) {
            p.sendMessage(Component.text("This check will fail"));
            Utils.playBassSoundAtPlayer(p);
        }
    }

    public Inventory getInv(Player p) {
        return getInventory(p);
    }

    private Inventory getInventory(Player p) {
        Job job = JobsManager.getPlayerJob(p.getUniqueId());
        String formattedJobName = StringUtils.capitalize(job.getEnumJob().name().toLowerCase());
        Inventory inventory = Bukkit.createInventory(null, 27,
                Component.text("Job Menu").color(TextColor.color(0, 255, 0)));

        Utils.createItem(inventory, Material.EMERALD, 1, 12,
                Component.text("Current Job: " + formattedJobName).color(TextColor.color(0, 128, 255)));
        Utils.createItem(inventory, Material.EXPERIENCE_BOTTLE, 1, 16,
                Component.text("Experience Level: " + job.getExp()).color(TextColor.color(0, 255, 0)));
        Utils.makeDummySlots(inventory);
        return inventory;
    }
}
