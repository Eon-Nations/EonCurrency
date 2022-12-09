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

public final class Eoncurrency extends JavaPlugin {
    EconManager econManager;
    @Override
    public void onEnable() {
        econManager = hookToVault();
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

    public EconManager hookToVault() {
        EconManager econManager = new EconManager();
        Bukkit.getServicesManager().register(Economy.class, econManager, Eoncurrency.getPlugin(Eoncurrency.class), ServicePriority.Normal);
        Bukkit.getConsoleSender().sendMessage(Utils.chat("&aVault has successfully hooked to Economy"));
        return econManager;
    }

    public void unHookVault() {
        Bukkit.getServicesManager().unregister(Economy.class, econManager);
    }
}
