package me.squid.eoncurrency.managers;

import me.squid.eoncurrency.Eoncurrency;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class EconomyManager {

    private static HashMap<UUID, Double> currency = new HashMap<>();

    public static void saveMapToFile() throws IOException {
        File file = new File(Bukkit.getPluginManager().getPlugin("EonHomes").getDataFolder(), "currency.ser");
        ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));

        output.writeObject(currency);
        output.flush();
        output.close();
    }

    public static void loadMapFromFile() throws IOException, ClassNotFoundException {
        File file = new File(Bukkit.getPluginManager().getPlugin("EonHomes").getDataFolder(), "currency.ser");
        ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));

        Object readObject = input.readObject();
        input.close();

        if (!(readObject instanceof HashMap)) throw new IOException("Data is not in a hashmap");
        //noinspection unchecked
        currency = (HashMap<UUID, Double>) readObject;
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
