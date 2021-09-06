package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.JobBreakEvent;
import me.squid.eoncurrency.managers.EconomyManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsEventListener implements Listener {

    Eoncurrency plugin;

    public JobsEventListener(Eoncurrency plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJobBreakEvent(JobBreakEvent e) {
        try {
            e.getJob().addExp(getExperienceFromBlock(e.getMaterial()));
            EconomyManager.addBalance(e.getPlayer().getUniqueId(), getPriceForBlock(e.getMaterial()));
            Bukkit.getScheduler().runTask(plugin, message(e.getPlayer(), e.getMaterial()));
        } catch (NullPointerException exception) {
            // Don't do anything
        }
    }

    private Runnable message(Player p, Material material) {
        return () -> {
            double price = getPriceForBlock(material);
            if (price != 0) {
                p.sendActionBar(Component.text("Added " + getPriceForBlock(material)));
            }
        };
    }

    private double getPriceForBlock(Material material) {
        String matString = material.name().toLowerCase();
        return plugin.getConfig().getDouble("job-prices." + matString + ".income");
    }

    private double getExperienceFromBlock(Material material) {
        String matString = material.name().toLowerCase();
        return plugin.getConfig().getDouble("job-prices." + matString + ".experience");
    }



    /*
    private double getBasePrice(Material material) {
        switch (material) {
            case
        }
    }
     */
}
