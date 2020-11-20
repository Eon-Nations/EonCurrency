package me.squid.eoncurrency.managers;

import me.squid.eoncurrency.Eoncurrency;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class EconomyManager {

    private static HashMap<UUID, Double> currency = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    public static void saveCurrencyFile(Eoncurrency plugin) {
        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("Money");
        for (String key : cs.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            double money = plugin.getConfig().getDouble("Money." + key + ".balance");
            int coins = plugin.getConfig().getInt("Money." + key + ".coins");

            if (money != currency.get(uuid)) {
                cs.set(key + ".balance", currency.get(uuid));
            }

            if (coins != CoinManager.getCoins(uuid)) {
                cs.set(key + ".coins", CoinManager.getCoins(uuid));
            }
            plugin.saveConfig();
        }
    }

    public static void loadCurrencyFile(Eoncurrency plugin) {
        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("Money");
        for (String key : cs.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            double money = cs.getDouble(key + ".balance");
            int coins = cs.getInt(key + ".coins");
            currency.put(uuid, money);
            CoinManager.setCoins(uuid, coins);
        }
        for (UUID u : currency.keySet()) {
            System.out.println("[EonCurrency] Put " + u.toString() + " with $" + currency.get(u));
        }
    }

    public static Double getBalance(UUID uuid) {
        return currency.get(uuid);
    }

    public static void setBalance(UUID uuid, double amount){
        currency.put(uuid, amount);
    }

    public static void addBalance(UUID uuid, double amount){
        if (!currency.containsKey(uuid)) currency.put(uuid, amount);
        else currency.put(uuid, getBalance(uuid) + amount);
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
