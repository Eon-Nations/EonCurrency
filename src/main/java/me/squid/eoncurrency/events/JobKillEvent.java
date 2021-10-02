package me.squid.eoncurrency.events;

import me.squid.eoncurrency.jobs.Job;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobKillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Job job;
    String entityType;

    public JobKillEvent(Player p, Job job, String entityType) {
        super(true);
        this.p = p;
        this.job = job;
        this.entityType = entityType;
    }

    public Player getPlayer() {
        return p;
    }

    public Job getJob() {
        return job;
    }

    public String getEntityType() {
        return entityType;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
