package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.JobBreakEvent;
import me.squid.eoncurrency.events.JobPlaceEvent;
import me.squid.eoncurrency.jobs.JobFileManager;
import me.squid.eoncurrency.managers.EconomyManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsEventListener implements Listener {

    Eoncurrency plugin;
    JobFileManager jobFileManager;

    public JobsEventListener(Eoncurrency plugin, JobFileManager jobFileManager) {
        this.plugin = plugin;
        this.jobFileManager = jobFileManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJobBreakEvent(JobBreakEvent e) {
        if (e.getWorld().getName().equals("spawn_void")) return;
        try {
            double reward = jobFileManager.getPriceForAction("break", e.getJob(), e.getMaterial());
            e.getJob().addExp(jobFileManager.getExperienceForAction("break", e.getJob(), e.getMaterial()));
            EconomyManager.addBalance(e.getPlayer().getUniqueId(), reward);
            Bukkit.getScheduler().runTask(plugin, message(e.getPlayer(), reward));
        } catch (NullPointerException exception) {
            // Don't do anything here
            // Player doesn't have a job
        }
    }

    @EventHandler
    public void onJobPlaceEvent(JobPlaceEvent e) {
        try {
            double reward = jobFileManager.getPriceForAction("place", e.getJob(), e.getMaterial());
            e.getJob().addExp(jobFileManager.getExperienceForAction("place", e.getJob(), e.getMaterial()));
            EconomyManager.addBalance(e.getPlayer().getUniqueId(), reward);
            Bukkit.getScheduler().runTask(plugin, message(e.getPlayer(), reward));
        } catch (NullPointerException exception) {
            // Player doesn't have a job
            // Don't do anything
        }
    }



    private Runnable message(Player p, double reward) {
        return () -> {
            if (reward != 0)
                p.sendActionBar(Component.text("Added " + reward));
        };
    }



    /*
    private double getBasePrice(Material material) {
        switch (material) {
            case
        }
    }
     */
}
