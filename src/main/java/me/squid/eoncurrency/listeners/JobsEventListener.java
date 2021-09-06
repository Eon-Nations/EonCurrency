package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.JobBreakEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsEventListener implements Listener {

    Eoncurrency plugin;

    public JobsEventListener(Eoncurrency plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJobBreakEvent(JobBreakEvent e) {
        System.out.println("Made it to the Job Break Event!");
        Bukkit.getScheduler().runTask(plugin, message(e.getPlayer()));
    }

    private Runnable message(Player p) {
        return () -> {
            System.out.println("Made it to the runnable");
            p.sendActionBar(Component.text("Nice block break lol"));
            p.sendMessage(Component.text("Nice block break lol"));
        };
    }

    /*
    private double getBasePrice(Material material) {
        switch (material) {
            case
        }
    }
     */
}
