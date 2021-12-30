package me.squid.eoncurrency;

import me.squid.eoncurrency.commands.*;
import me.squid.eoncurrency.jobs.JobFileManager;
import me.squid.eoncurrency.listeners.JobsEventListener;
import me.squid.eoncurrency.listeners.JoinListener;
import me.squid.eoncurrency.listeners.ShopMenuListener;
import me.squid.eoncurrency.listeners.WorldInteractListener;
import me.squid.eoncurrency.managers.JobsManager;
import me.squid.eoncurrency.managers.SQLManager;
import me.squid.eoncurrency.managers.VaultHook;
import me.squid.eoncurrency.menus.JobInfoMenu;
import org.bukkit.plugin.java.JavaPlugin;

public final class Eoncurrency extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommands();
        registerListeners();
        registerJobs();
        registerManagers();
        hookVault();
    }

    @Override
    public void onDisable() {
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
        new WorldInteractListener(this);
    }

    public void registerJobs() {
        new JobStatsCommand(this);
        JobFileManager jobFileManager = new JobFileManager(this);
        JobInfoMenu jobInfoMenu = new JobInfoMenu(this, jobFileManager);
        new JobsCommand(this, jobInfoMenu);
        new JobsEventListener(this, jobFileManager);
        new JobsCommand(this, jobInfoMenu);
    }

    public void registerManagers() {
        new JobsManager(this);
        new SQLManager(this);
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
