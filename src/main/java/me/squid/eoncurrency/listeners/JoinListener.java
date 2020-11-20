package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.CoinManager;
import me.squid.eoncurrency.managers.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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
        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("Money");

        if (cs.get(p.getUniqueId().toString() + ".balance") == null) {
            cs.set(p.getUniqueId().toString() + ".balance", 0);
            cs.set(p.getUniqueId().toString() + ".coins", 0);
            EconomyManager.setBalance(p.getUniqueId(), 0);
            CoinManager.setCoins(p.getUniqueId(), 0);
        }
    }
}
