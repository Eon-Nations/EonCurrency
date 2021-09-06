package me.squid.eoncurrency;

import me.squid.eoncurrency.commands.*;
import me.squid.eoncurrency.listeners.JobsEventListener;
import me.squid.eoncurrency.listeners.JoinListener;
import me.squid.eoncurrency.listeners.ShopMenuListener;
import me.squid.eoncurrency.listeners.WorldInteractListener;
import me.squid.eoncurrency.managers.*;
import me.squid.eoncurrency.menus.JobMenu;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Eoncurrency extends JavaPlugin {

    public MySQL mySQL;

    @Override
    public void onEnable() {
        setupSQL();
        registerCommands();
        registerListeners();
        registerManagers();
        hookVault();
    }

    @Override
    public void onDisable() {
        mySQL.disconnect();
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
        new JobsCommand(this);
    }

    public void registerListeners() {
        new JoinListener(this);
        new ShopMenuListener(this);
        new WorldInteractListener(this);
        new JobsEventListener(this);
        new CurrencySQLManager(this);
        new JobMenu(this);
    }

    public void registerManagers() {
        new JobsManager(this);
        new JobSQLManager(this);
    }

    public void setupSQL() {
        mySQL = new MySQL(this);
        try {
            mySQL.connectToDatabase();
        } catch (SQLException e) {
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
