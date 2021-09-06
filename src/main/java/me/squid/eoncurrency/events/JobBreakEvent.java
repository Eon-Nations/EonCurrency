package me.squid.eoncurrency.events;

import me.squid.eoncurrency.jobs.Job;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobBreakEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Block block;
    World world;
    Job job;

    public JobBreakEvent(Player p, Block block, World world, Job job) {
        super(true);
        this.p = p;
        this.block = block;
        this.world = world;
        this.job = job;
    }

    public Player getPlayer() {
        return p;
    }

    public Block getBlock() {
        return block;
    }

    public World getWorld() {
        return world;
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
