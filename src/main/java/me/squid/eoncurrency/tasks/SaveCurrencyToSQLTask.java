package me.squid.eoncurrency.tasks;

import me.squid.eoncurrency.managers.SQLManager;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class SaveCurrencyToSQLTask implements Runnable {

    HashMap<UUID, Double> currency;

    public SaveCurrencyToSQLTask(HashMap<UUID, Double> currency) {
        this.currency = currency;
    }

    @Override
    public void run() {
        SQLManager sqlManager = SQLManager.getInstance();
        for (UUID uuid : currency.keySet()) {
            if (sqlManager.getBalance(uuid) != currency.get(uuid)) sqlManager.updateBalance(uuid);
        }
        Bukkit.getServer().getLogger().info("Successfully saved players to the SQL Database");
    }
}
