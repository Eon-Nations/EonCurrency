package me.squid.eoncurrency.events;

import me.squid.eoncurrency.jobs.Job;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobPlaceEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Material material;
    Job job;

    public JobPlaceEvent(Player p, Material material, Job job) {
        super(true);
        this.p = p;
        this.material = material;
        this.job = job;
    }

    public Player getPlayer() {
        return p;
    }

    public Material getMaterial() {
        return material;
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
