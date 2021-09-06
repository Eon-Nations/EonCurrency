package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.CoinManager;
import me.squid.eoncurrency.managers.EconomyManager;
import me.squid.eoncurrency.managers.JobsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    Eoncurrency plugin;

    public JoinListener(Eoncurrency plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        if (!CoinManager.exists(p.getUniqueId())) CoinManager.setCoins(p.getUniqueId(), 0);
        if (!EconomyManager.exists(p.getUniqueId())) EconomyManager.setBalance(p.getUniqueId(), 0);

        JobsManager.testFunction(p);
    }
}
