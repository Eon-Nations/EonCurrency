package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    Eoncurrency plugin;

    public JoinListener(Eoncurrency plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

<<<<<<< Updated upstream
        if (!CoinManager.exists(p.getUniqueId())) CoinManager.setCoins(p.getUniqueId(), 0);
        if (!EconomyManager.exists(p.getUniqueId())) EconomyManager.setBalance(p.getUniqueId(), 0);
=======
        EconomyManager.loadPlayerIntoMap(e.getPlayer());
        JobsManager.loadPlayerFromSQL(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        EconomyManager.savePlayerToSQL(e.getPlayer());
        JobsManager.loadPlayerToSQL(e.getPlayer());
>>>>>>> Stashed changes
    }
}
