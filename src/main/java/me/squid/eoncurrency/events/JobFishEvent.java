package me.squid.eoncurrency.events;

import me.squid.eoncurrency.jobs.Job;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobFishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    String fishName;
    Job job;

    public JobFishEvent(Player p, String fishName, Job job) {
        super(true);
        this.p = p;
        this.fishName = fishName;
        this.job = job;
    }

    public Player getPlayer() {
        return p;
    }

    public String getFishName() {
        return fishName;
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
