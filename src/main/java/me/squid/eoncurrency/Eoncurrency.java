package me.squid.eoncurrency;

import me.squid.eoncurrency.commands.*;
import me.squid.eoncurrency.listeners.JoinListener;
import me.squid.eoncurrency.listeners.ShopMenuListener;
import me.squid.eoncurrency.managers.EconomyManager;
import me.squid.eoncurrency.managers.VaultHook;
import org.bukkit.plugin.java.JavaPlugin;

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
        saveCurrencyFile();
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
        createCurrencySection();
        EconomyManager.loadCurrencyFile(this);
        System.out.println("[EonCurrency] Data has been put into memory");
    }

    public void saveCurrencyFile() {
        EconomyManager.saveCurrencyFile(this);
        System.out.println("[EonCurrency] Currency file have been saved");
    }

    public void hookVault(){
        VaultHook vaultHook = new VaultHook();
        vaultHook.hook();
    }

    public void unHookVault(){
        VaultHook vaultHook = new VaultHook();
        vaultHook.unhook();
    }

    private void createCurrencySection() {
        if (getConfig().getConfigurationSection("Money") == null) {
            getConfig().createSection("Money");
            saveConfig();
        }
    }
}
