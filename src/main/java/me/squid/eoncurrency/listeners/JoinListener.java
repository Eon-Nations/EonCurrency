package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.EconManager;
import me.squid.eoncurrency.managers.JobsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        if (!econManager.hasAccount(p)) {
            econManager.createPlayerAccount(e.getPlayer());
        } else {
            econManager.loadPlayer(p);
        }
        JobsManager.loadPlayerFromSQL(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        econManager.savePlayer(e.getPlayer());
        JobsManager.loadPlayerToSQL(e.getPlayer());
    }
}
