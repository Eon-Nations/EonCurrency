package me.squid.eoncurrency.events;

import me.squid.eoncurrency.jobs.Job;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobShearEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Job job;
    Sheep sheep;

    public JobShearEvent(Player p, Job job, Sheep sheep) {
        super(true);
        this.p = p;
        this.job = job;
        this.sheep = sheep;
    }

    public Player getPlayer() {
        return p;
    }

    public Job getJob() {
        return job;
    }

    public Sheep getSheep() {
        return sheep;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
