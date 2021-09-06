package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.JobBreakEvent;
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
