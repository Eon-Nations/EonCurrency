package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.JobBreakEvent;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.managers.JobsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class WorldInteractListener implements Listener {

    Eoncurrency plugin;

    public WorldInteractListener(Eoncurrency plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Material material = e.getBlock().getType();
        Player p = e.getPlayer();
        World world = e.getPlayer().getWorld();
        Job job = JobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobBreakEvent(p, material, world, job)));
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        Material material = e.getBlock().getType();
        Player p = e.getPlayer();
        World world = e.getPlayer().getWorld();
        Job job = JobsManager.getPlayerJob(p.getUniqueId());


    }
}
