package me.squid.eoncurrency.managers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LocalEconManager implements Economy {

    private final HashMap<UUID, Double> currency = new HashMap<>();
    private final static EconomyResponse NOT_SUPPORTED = new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported");


    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "Testing Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double v) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(v);
    }

    @Override
    public String currencyNamePlural() {
        return "Currencies";
    }

    @Override
    public String currencyNameSingular() {
        return "Currency";
    }

    @Override
    public boolean hasAccount(String s) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(s);
        return player != null && hasAccount(player);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return currency.get(player.getUniqueId()) != null;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return hasAccount(s);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String s) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(s);
        return getBalance(player);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return currency.getOrDefault(player.getUniqueId(), 0.0);
    }

    @Override
    public double getBalance(String s, String s1) {
        return getBalance(s);
    }

    @Override
    public double getBalance(OfflinePlayer player, String s) {
        return getBalance(player);
    }

    @Override
    public boolean has(String s, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(s);
        return has(player, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        double balance = getBalance(player);
        return balance >= amount;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return has(s, v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return has(offlinePlayer, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(s);
        return withdrawPlayer(player, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        double balance = getBalance(player);
        double newBalance = balance - amount;
        if (newBalance < 0) {
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        } else {
            currency.put(player.getUniqueId(), newBalance);
            return new EconomyResponse(amount, newBalance, EconomyResponse.ResponseType.SUCCESS, "");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(s, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdrawPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(s);
        return depositPlayer(player, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        double balance = getBalance(player);
        currency.put(player.getUniqueId(), balance + amount);
        return new EconomyResponse(amount, balance + amount, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(s, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return depositPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return NOT_SUPPORTED;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return NOT_SUPPORTED;
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(s);
        return createPlayerAccount(player);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        if (currency.get(player.getUniqueId()) == null) {
            currency.putIfAbsent(player.getUniqueId(), 0.0);
            return true;
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return createPlayerAccount(s);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return createPlayerAccount(offlinePlayer);
    }
}
