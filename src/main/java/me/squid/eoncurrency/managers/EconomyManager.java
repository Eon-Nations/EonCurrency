package me.squid.eoncurrency.managers;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.tasks.SaveCurrencyToSQLTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class EconomyManager {

    private static HashMap<UUID, Double> currency;

    public EconomyManager(Eoncurrency plugin) {
        currency = new HashMap<>();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new SaveCurrencyToSQLTask(currency), 0L, 60L * 1000L * 5L); // Every 5 minutes
    }

    public static void loadPlayerIntoMap(Player player) {
        SQLManager sqlManager = SQLManager.getInstance();
        if (currency == null) currency = new HashMap<>();

        double balance = sqlManager.getBalance(player.getUniqueId());
        currency.put(player.getUniqueId(), balance);
    }

    public static void savePlayerToSQL(Player player) {
        SQLManager sqlManager = SQLManager.getInstance();
        sqlManager.updateBalance(player.getUniqueId());
    }

    public static Double getBalance(UUID uuid) {
        return currency.get(uuid);
    }

    public static void setBalance(UUID uuid, double amount){
        currency.put(uuid, amount);
    }

    public static void addBalance(UUID uuid, double amount){
        if (!currency.containsKey(uuid)) currency.put(uuid, amount);
        else setBalance(uuid, getBalance(uuid) + amount);
    }

    public static void removeCurrency(UUID uuid, double amount){
        currency.put(uuid, getBalance(uuid) - amount);
    }

    public static boolean exists(UUID uuid) {
        return currency.containsKey(uuid);
    }

    public static boolean hasEnough(UUID uuid, double amount){
        return !(currency.get(uuid) < amount);
    }

    public static HashMap<UUID, Double> getSortedMap() {
        LinkedHashMap<UUID, Double> sortedMap = new LinkedHashMap<>();

        currency.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        return sortedMap;
    }
}
