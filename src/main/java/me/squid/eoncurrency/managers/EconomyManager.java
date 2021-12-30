package me.squid.eoncurrency.managers;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EconomyManager {

    private static HashMap<UUID, Double> currency;

    public static void loadPlayerIntoMap(Player player) {
        if (currency == null) currency = new HashMap<>();

        double balance = SQLManager.getBalance(player.getUniqueId());
        currency.put(player.getUniqueId(), balance);
    }

    public static void savePlayerToSQL(Player player) {
        SQLManager.updateBalance(player.getUniqueId());
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

    public static @NotNull HashMap<UUID, Double> getOnlineSortedMap() {
        LinkedHashMap<UUID, Double> sortedMap = new LinkedHashMap<>();

        currency.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        return sortedMap;
    }
}
