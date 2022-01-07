package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.*;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.jobs.JobFileManager;
import me.squid.eoncurrency.jobs.Jobs;
import me.squid.eoncurrency.managers.EconManager;
import me.squid.eoncurrency.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsEventListener implements Listener {

    Eoncurrency plugin;
    JobFileManager jobFileManager;
    EconManager econManager;

    public JobsEventListener(Eoncurrency plugin, JobFileManager jobFileManager, EconManager econManager) {
        this.plugin = plugin;
        this.jobFileManager = jobFileManager;
        this.econManager = econManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJobBreakEvent(JobBreakEvent e) {
        if (e.getWorld().getName().equals("spawn_void")) return;
        if (e.getJob().getEnumJob().equals(Jobs.MINER) &&
        e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
            return;
        }
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
        double amount = 1;
        if (e.getLevel() != 30) {
            amount = e.getLevel() * 0.01;
        }
        sendJobReward(e.getPlayer(), "enchant", e.getJob(), e.getEnchantItem().getType().name(), amount);
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

    private void sendJobReward(Player p, String action, Job job, String type, double amount) {
        try {
            double reward = giveMoneyToPlayer(p, action, job, type.toLowerCase(), amount);
            giveExperience(job, action, type.toLowerCase());
            Bukkit.getScheduler().runTask(plugin, message(p, Utils.round(reward, 2)));
        } catch (NullPointerException exception) {
            // Player doesn't have a job
        }
    }

    private double giveMoneyToPlayer(Player p, String action, Job job, String type, double amount) {
        double baseReward = jobFileManager.getPriceForAction(action, job.getEnumJob(), type) * amount;
        econManager.depositPlayer(p, baseReward * getMultiplier(job.getExp()));
        return baseReward * getMultiplier(job.getExp());
    }

    private void giveExperience(Job job, String action, String type) {
        double baseExp = jobFileManager.getExperienceForAction(action, job.getEnumJob(), type);
        job.addExp(baseExp * getMultiplier(job.getExp()));
    }

    private long getIntLevel(double experience) {
        return Math.round(Utils.getDoubleLevel(experience));
    }

    private double getMultiplier(double exp) {
        return (getIntLevel(exp) * 0.01) + 1;
    }
}
