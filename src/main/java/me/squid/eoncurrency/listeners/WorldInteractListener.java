package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.JobBreakEvent;
import me.squid.eoncurrency.events.JobFishEvent;
import me.squid.eoncurrency.events.JobPlaceEvent;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.managers.JobsManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerFishEvent;

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
        if (e.getPlayer().getWorld().getName().equals("spawn_void") && !e.getPlayer().isOp()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Component.text("Doesn't work"));
        }

        Material material = e.getBlock().getType();
        Player p = e.getPlayer();
        Job job = JobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobPlaceEvent(p, material, job)));
    }

    @EventHandler
    public void onFishEvent(PlayerFishEvent e) {
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) && e.getCaught() instanceof Fish) {
            String name = e.getCaught().getName();
            Player p = e.getPlayer();
            Job job = JobsManager.getPlayerJob(p.getUniqueId());

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                    Bukkit.getPluginManager().callEvent(new JobFishEvent(p, name, job)));
        }
    }
}
