package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.*;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.jobs.JobFileManager;
import me.squid.eoncurrency.managers.EconomyManager;
import me.squid.eoncurrency.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsEventListener implements Listener {

    Eoncurrency plugin;
    JobFileManager jobFileManager;
    double multiplierTerm;

    public JobsEventListener(Eoncurrency plugin, JobFileManager jobFileManager) {
        multiplierTerm = 0.15;
        this.plugin = plugin;
        this.jobFileManager = jobFileManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJobBreakEvent(JobBreakEvent e) {
        if (e.getWorld().getName().equals("spawn_void")) return;
        sendJobReward(e.getPlayer(), "break", e.getJob(), e.getMaterial().name(), 1);
    }

    @EventHandler
    public void onJobPlaceEvent(JobPlaceEvent e) {
        sendJobReward(e.getPlayer(), "place", e.getJob(), e.getMaterial().name(), 1);
    }

    @EventHandler
    public void onJobMilkEvent(JobMilkEvent e) {
        e.getPlayer().sendMessage(Component.text(e.getCow().getName()));
    }

    @EventHandler
    public void onJobFishEvent(JobFishEvent e) {
        sendJobReward(e.getPlayer(), "fish", e.getJob(), e.getFishName(), 1);
    }

    @EventHandler
    public void onShearSheepEvent(JobShearEvent e) {
        sendJobReward(e.getPlayer(), "shear", e.getJob(), e.getSheep().getName(), 1);
    }

    @EventHandler
    public void onEnchantItem(JobEnchantEvent e) {
        sendJobReward(e.getPlayer(), "enchant", e.getJob(), e.getEnchantItem().getType().name(), 1);
    }

    @EventHandler
    public void onFurnaceExtractEvent(JobSmeltEvent e) {
        sendJobReward(e.getPlayer(), "smelt", e.getJob(), e.getMaterial().name(), e.getAmount());
    }

    @EventHandler
    public void onEntityKillEvent(JobKillEvent e) {
        sendJobReward(e.getPlayer(), "kill", e.getJob(), e.getEntityType(), 1);
    }

    @EventHandler
    public void onJobBreedEvent(JobBreedEvent e) {
        sendJobReward(e.getPlayer(), "breed", e.getJob(), e.getBreeded().getType().name(), 1);
    }

    @EventHandler
    public void onJobCraftEvent(JobCraftEvent e) {
        sendJobReward(e.getPlayer(), "craft", e.getJob(), e.getType().name(), e.getAmount());
    }

    private Runnable message(Player p, double reward) {
        return () -> {
            if (reward != 0) {
                p.sendActionBar(Component.text("Jobs: ")
                        .color(TextColor.color(0, 255, 0))
                        .append(Component.text("Earned $" + reward)
                                .color(TextColor.color(160, 160, 160))));
            }
        };
    }

    private void sendJobReward(Player p, String action, Job job, String type, int amount) {
        try {
            double reward = giveMoneyToPlayer(p, action, job, type.toLowerCase(), amount);
            giveExperience(job, action, type.toLowerCase());
            Bukkit.getScheduler().runTask(plugin, message(p, Utils.round(reward, 2)));
        } catch (NullPointerException exception) {
            p.sendMessage(Utils.getPrefix("nations")
                    .append(Component.text("Looks like you haven't joined a job. " +
                                    "Enter /jobsmenu to join a job")
                            .color(TextColor.color(160, 160, 160))));
        }
    }

    private double giveMoneyToPlayer(Player p, String action, Job job, String type, int amount) {
        double baseReward = jobFileManager.getPriceForAction(action, job, type) * amount;
        double multiplier = getIntLevel(job.getExp()) * multiplierTerm;
        EconomyManager.addBalance(p.getUniqueId(), baseReward * multiplier);
        return baseReward * multiplier;
    }

    private void giveExperience(Job job, String action, String type) {
        double baseExp = jobFileManager.getExperienceForAction(action, job, type);
        double multiplier = getIntLevel(job.getExp()) * multiplierTerm;
        job.addExp(multiplier * baseExp);
    }

    private long getIntLevel(double experience) {
        return Math.round(Utils.getDoubleLevel(experience));
    }
}
