package me.squid.eoncurrency.managers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class EconManager implements Economy {

    JedisPool pool;
    private static final int DEC_PLACES = 2;
    static final String BALANCE = "#balance";

    public EconManager(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "Eon Currency";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return DEC_PLACES;
    }

    @Override
    public String format(double amount) {
        BigDecimal bd = new BigDecimal(String.valueOf(amount), MathContext.DECIMAL32);
        bd = bd.setScale(DEC_PLACES, RoundingMode.HALF_EVEN);
        return bd.toString();
    }

    @Override
    public String currencyNamePlural() {
        return "dollars";
    }

    @Override
    public String currencyNameSingular() {
        return "dollar";
    }

    @Override
    public boolean hasAccount(String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player != null) {
            return hasAccount(player);
        } else return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer p) {
        try (Jedis jedis = pool.getResource()) {
            return !jedis.get(p.getName() + BALANCE).equals("nil");
        }
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(name);
        return player != null ? getBalance(player) : 0.0;
    }

    @Override
    public double getBalance(OfflinePlayer p) {
        try (Jedis jedis = pool.getResource()) {
            return getBalance(p, jedis);
        }
    }

    public double getBalance(OfflinePlayer p, Jedis jedis) {
        try {
            String balance = jedis.get(p.getName() + BALANCE);
            return Double.parseDouble(balance);
        } catch (NumberFormatException | JedisConnectionException e) {
            return 0.0;
        }
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String name, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(name);
        return player != null && has(player, amount);
    }

    @Override
    public boolean has(OfflinePlayer p, double amount) {
        try (Jedis jedis = pool.getResource()) {
            double balance = getBalance(p, jedis);
            return balance >= amount;
        }
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer p, String worldName, double amount) {
        return has(p, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        Player p = Bukkit.getPlayer(name);
        if (p != null) {
            return withdrawPlayer(p, amount);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(name);
            if (offlinePlayer != null) {
                return withdrawPlayer(offlinePlayer, amount);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player does not exist.");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer p, double amount) {
        try (Jedis jedis = pool.getResource()) {
            return withdrawPlayer(p, amount, jedis);
        }
    }

    public EconomyResponse withdrawPlayer(OfflinePlayer p, double amount, Jedis jedis) {
        String rawBalance = jedis.get(p.getName() + BALANCE);
        double balance = Double.parseDouble(rawBalance);
        double newBalance = balance - amount;
        if (newBalance < 0) {
            return new EconomyResponse(amount, balance, ResponseType.FAILURE, "Insufficient funds");
        } else {
            jedis.set(p.getName() + BALANCE, format(newBalance));
            return new EconomyResponse(amount, newBalance, ResponseType.SUCCESS, "");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer p, String worldName, double amount) {
        return withdrawPlayer(p, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Player p = Bukkit.getPlayer(playerName);
        if (p != null) {
            return depositPlayer(p, amount);
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
            if (player != null) {
                return depositPlayer(player, amount);
            } else return new EconomyResponse(0, 0.0,
                    EconomyResponse.ResponseType.FAILURE, "Player does not exist.");
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer p, double amount) {
        try (Jedis jedis = pool.getResource()) {
            double balance = Double.parseDouble(jedis.get(p.getName() + BALANCE));
            jedis.set(p.getName() + BALANCE, format(balance + amount));
            return new EconomyResponse(amount, balance + amount, ResponseType.SUCCESS, "");
        } catch (NumberFormatException e) {
            if (createPlayerAccount(p)) return depositPlayer(p, amount);
            else return new EconomyResponse(amount, 0.0, ResponseType.FAILURE, "Corrupt record");
        }
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer p, String worldName, double amount) {
        return depositPlayer(p, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player != null) {
            return createPlayerAccount(player);
        } else return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer p) {
        try (Jedis jedis = pool.getResource()) {
            String balance = jedis.get(p.getName() + BALANCE);
            try {
                // Throw away value to see if the parsing the double was successful
                Double.parseDouble(balance);
                return false;
            } catch (NumberFormatException e) {
                jedis.set(p.getName() + BALANCE, "0.0");
                return true;
            }
        }
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
}
