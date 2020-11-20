package me.squid.eoncurrency;

import me.squid.eoncurrency.commands.*;
import me.squid.eoncurrency.listeners.JoinListener;
import me.squid.eoncurrency.listeners.ShopMenuListener;
import me.squid.eoncurrency.managers.CoinManager;
import me.squid.eoncurrency.managers.EconomyManager;
import me.squid.eoncurrency.managers.VaultHook;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Eoncurrency extends JavaPlugin {

    @Override
    public void onEnable() {
        setupFiles();
        registerCommands();
        registerListeners();
        hookVault();
    }

    @Override
    public void onDisable() {
        saveCurrencyFiles();
        unHookVault();
    }

    @SuppressWarnings("ConstantConditions")
    public void registerCommands() {
        getCommand("coins").setExecutor(new CoinCommandManager(this));
        getCommand("economy").setExecutor(new EconomyCommandManager(this));
        new PayCommand(this);
        new BalanceCommand(this);
        new ShopCommand(this);
        new BaltopCommand(this);
    }

    public void registerListeners() {
        new JoinListener(this);
        new ShopMenuListener(this);
    }

    public void setupFiles(){
        saveDefaultConfig();
        try {
            EconomyManager.loadMapFromFile();
            CoinManager.loadMapFromFile();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveCurrencyFiles() {
        try {
            EconomyManager.saveMapToFile();
            CoinManager.saveMapToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void hookVault(){
        VaultHook vaultHook = new VaultHook();
        vaultHook.hook();
    }

    public void unHookVault(){
        VaultHook vaultHook = new VaultHook();
        vaultHook.unhook();
    }
}
