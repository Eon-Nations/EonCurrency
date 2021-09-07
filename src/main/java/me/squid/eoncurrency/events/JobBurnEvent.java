package me.squid.eoncurrency.events;

import me.squid.eoncurrency.jobs.Job;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobBurnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Material material;
    int amount;
    Job job;

    public JobBurnEvent(Player p, Material material, int amount, Job job) {
        super(true);
        this.p = p;
        this.material = material;
        this.amount = amount;
        this.job = job;
    }

    public Player getPlayer() {
        return p;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
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
