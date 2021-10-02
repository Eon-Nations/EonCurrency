package me.squid.eoncurrency.events;

import me.squid.eoncurrency.jobs.Job;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobBreedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Job job;
    Entity breeded;

    public JobBreedEvent(Player p, Job job, Entity breeded) {
        super(true);
        this.p = p;
        this.job = job;
        this.breeded = breeded;
    }

    public Player getPlayer() {
        return p;
    }

    public Job getJob() {
        return job;
    }

    public Entity getBreeded() {
        return breeded;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
