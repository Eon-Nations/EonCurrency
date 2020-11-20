package me.squid.eoncurrency.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CoinManager {

    private static HashMap<UUID, Integer> coins = new HashMap<>();

    public static Integer getCoins(UUID uuid){
        return coins.get(uuid);
    }

    public static void setCoins(UUID uuid, int amount){
        coins.put(uuid, amount);
    }

    public static void addCoins(UUID uuid, int amount){
        coins.put(uuid, coins.get(uuid) + amount);
    }

    public static void removeCoins(UUID uuid, int amount){
        coins.put(uuid, coins.get(uuid) - amount);
    }

    public static boolean exists(UUID uuid){
        return coins.containsKey(uuid);
    }

    public static boolean hasEnough(UUID uuid, int amount){
        return !(coins.get(uuid) < amount);
    }

}
