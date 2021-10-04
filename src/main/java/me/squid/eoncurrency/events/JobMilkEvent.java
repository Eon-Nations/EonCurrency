package me.squid.eoncurrency.events;

import me.squid.eoncurrency.jobs.Job;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobMilkEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Cow cow;
    Job job;

    public JobMilkEvent(Player p, Cow cow, Job job) {
        super(true);
        this.p = p;
        this.cow = cow;
        this.job = job;
    }

    public Player getPlayer() {
        return p;
    }

    public Cow getCow() {
        return cow;
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
