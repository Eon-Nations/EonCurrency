package me.squid.eoncurrency;

import me.squid.eoncurrency.commands.*;
import me.squid.eoncurrency.listeners.JoinListener;
import me.squid.eoncurrency.listeners.ShopMenuListener;
import me.squid.eoncurrency.managers.EconManager;
import me.squid.eoncurrency.menus.EcoMenu;
import me.squid.eoncurrency.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;

public final class Eoncurrency extends JavaPlugin {
    EconManager econManager;
    JedisPool pool;

    @Override
    public void onEnable() {
        econManager = hookToVault();
        pool = setupPool();
        saveDefaultConfig();
        EcoMenu ecoMenu = new EcoMenu(econManager);
        registerCommands(ecoMenu);
        registerListeners(ecoMenu);
    }

    @Override
    public void onDisable() {
        unHookVault();
    }

    @SuppressWarnings("ConstantConditions")
    public void registerCommands(EcoMenu ecoMenu) {
        getCommand("economy").setExecutor(new EconomyCommandManager(this, econManager));
        new PayCommand(this, econManager);
        new BalanceCommand(this, ecoMenu);
        new ShopCommand(this, ecoMenu);
    }

    public void registerListeners(EcoMenu ecoMenu) {
        new JoinListener(this, econManager);
        new ShopMenuListener(this, ecoMenu, econManager);
    }

    public JedisPool setupPool() {
        String serverURL = System.getProperty("REDIS_URL");
        return new JedisPool(serverURL);
    }

    public EconManager hookToVault() {
        EconManager econ = new EconManager(pool);
        Bukkit.getServicesManager().register(Economy.class, econ, JavaPlugin.getPlugin(Eoncurrency.class), ServicePriority.Normal);
        Bukkit.getConsoleSender().sendMessage(Utils.chat("&aVault has successfully hooked to Economy"));
        return econManager;
    }

    public void unHookVault() {
        Bukkit.getServicesManager().unregister(Economy.class, econManager);
    }
}
