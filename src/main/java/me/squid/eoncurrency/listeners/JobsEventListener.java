package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.*;
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
            double reward = jobFileManager.getPriceForAction("break", e.getJob(), e.getMaterial().name());
            e.getJob().addExp(jobFileManager.getExperienceForAction("break", e.getJob(), e.getMaterial().name()));
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
            double reward = jobFileManager.getPriceForAction("place", e.getJob(), e.getMaterial().name());
            e.getJob().addExp(jobFileManager.getExperienceForAction("place", e.getJob(), e.getMaterial().name()));
            e.getPlayer().sendMessage(e.getMaterial().name());
            EconomyManager.addBalance(e.getPlayer().getUniqueId(), reward);
            Bukkit.getScheduler().runTask(plugin, message(e.getPlayer(), reward));
        } catch (NullPointerException exception) {
            // Player doesn't have a job
            // Don't do anything
        }
    }

    @EventHandler
    public void onJobFishEvent(JobFishEvent e) {
        try {
            double reward = jobFileManager.getPriceForAction("fish", e.getJob(), e.getFishName());
            e.getJob().addExp(jobFileManager.getExperienceForAction("fish", e.getJob(), e.getFishName()));
            EconomyManager.addBalance(e.getPlayer().getUniqueId(), reward);
            Bukkit.getScheduler().runTask(plugin, message(e.getPlayer(), reward));
        } catch (NullPointerException exception) {
            // Player doesn't have a job
            // Don't do anything
        }
    }

    @EventHandler
    public void onShearSheepEvent(JobShearEvent e) {
        try {
            double reward = jobFileManager.getPriceForAction("shear", e.getJob(), e.getSheep().getName());
            e.getJob().addExp(jobFileManager.getExperienceForAction("shear", e.getJob(), e.getSheep().getName()));
            EconomyManager.addBalance(e.getPlayer().getUniqueId(), reward);
            Bukkit.getScheduler().runTask(plugin, message(e.getPlayer(), reward));
        } catch (NullPointerException exception) {
            // Player doesn't have a job
            // It's alright
        }
    }

    @EventHandler
    public void onEnchantItem(JobEnchantEvent e) {
        try {
            double reward = jobFileManager.getPriceForAction("enchant", e.getJob(), e.getEnchantItem().getType().name());
            e.getJob().addExp(jobFileManager.getExperienceForAction("enchant", e.getJob(), e.getEnchantItem().getType().name()));
            EconomyManager.addBalance(e.getPlayer().getUniqueId(), reward);
            Bukkit.getScheduler().runTask(plugin, message(e.getPlayer(), reward));
        } catch (NullPointerException exception) {
            // Player doesn't have a job
        }
    }

    @EventHandler
    public void onFurnaceBurnEvent(JobBurnEvent e) {
        try {
            double reward = e.getAmount() *
                    jobFileManager.getPriceForAction("smelt", e.getJob(), e.getMaterial().name());
            e.getJob().addExp(jobFileManager.getExperienceForAction("smelt", e.getJob(), e.getMaterial().name()));
            EconomyManager.addBalance(e.getPlayer().getUniqueId(), reward);
            Bukkit.getScheduler().runTask(plugin, message(e.getPlayer(), reward));
        } catch (NullPointerException exception) {
            // Player doesn't have a job
        }
    }

    private Runnable message(Player p, double reward) {
        return () -> {
            if (reward != 0)
                p.sendActionBar(Component.text("Added " + reward));
        };
    }
}
