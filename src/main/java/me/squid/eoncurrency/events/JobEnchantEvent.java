package me.squid.eoncurrency.events;

import me.squid.eoncurrency.jobs.Job;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class JobEnchantEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Map<Enchantment, Integer> enchantments;
    Job job;
    ItemStack item;

    public JobEnchantEvent(Player p, Map<Enchantment, Integer> enchantments, Job job, ItemStack item) {
        this.p = p;
        this.enchantments = enchantments;
        this.job = job;
        this.item = item;
    }

    public Player getPlayer() {
        return p;
    }

    public Map<Enchantment, Integer> getEnchantment() {
        return enchantments;
    }

    public ItemStack getEnchantItem() {
        return item;
    }

    public Job getJob() {
        return job;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
