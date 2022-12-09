package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.EconManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    Eoncurrency plugin;
    EconManager econManager;

    public JoinListener(Eoncurrency plugin, EconManager econManager) {
        this.plugin = plugin;
        this.econManager = econManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        econManager.createPlayerAccount(p);
    }
}
