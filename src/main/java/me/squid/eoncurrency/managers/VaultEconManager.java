package me.squid.eoncurrency.managers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

public class VaultEconManager implements Economy {

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
        return 0;
    }

    @Override
    public String format(double amount) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(amount);
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
        Player p = Bukkit.getPlayer(playerName);
        assert p != null;
        return EconomyManager.exists(p.getUniqueId());
    }

    @Override
    public boolean hasAccount(OfflinePlayer p) {
        return EconomyManager.exists(p.getUniqueId());
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
        Player p = Bukkit.getPlayer(name);
        assert p != null;
        return EconomyManager.getBalance(p.getUniqueId());
    }

    @Override
    public double getBalance(OfflinePlayer p) {
        return EconomyManager.getBalance(p.getUniqueId());
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
        Player p = Bukkit.getPlayer(name);
        assert p != null;
        return EconomyManager.hasEnough(p.getUniqueId(), amount);
    }

    @Override
    public boolean has(OfflinePlayer p, double amount) {
        return EconomyManager.hasEnough(p.getUniqueId(), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        Player p = Bukkit.getPlayer(playerName);
        assert p != null;
        return EconomyManager.hasEnough(p.getUniqueId(), amount);
    }

    @Override
    public boolean has(OfflinePlayer p, String worldName, double amount) {
        return EconomyManager.hasEnough(p.getUniqueId(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        Player p = Bukkit.getPlayer(name);
        assert p != null;
        EconomyManager.removeCurrency(p.getUniqueId(), amount);
        return new EconomyResponse(-amount, EconomyManager.getBalance(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer p, double amount) {
        EconomyManager.removeCurrency(p.getUniqueId(), amount);
        return new EconomyResponse(-amount, EconomyManager.getBalance(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        Player p = Bukkit.getPlayer(playerName);
        assert p != null;
        EconomyManager.removeCurrency(p.getUniqueId(), amount);
        return new EconomyResponse(-amount, EconomyManager.getBalance(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer p, String worldName, double amount) {
        EconomyManager.removeCurrency(p.getUniqueId(), amount);
        return new EconomyResponse(-amount, EconomyManager.getBalance(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Player p = Bukkit.getPlayer(playerName);
        assert p != null;
        EconomyManager.addBalance(p.getUniqueId(), amount);
        return new EconomyResponse(amount, EconomyManager.getBalance(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer p, double amount) {
        EconomyManager.addBalance(p.getUniqueId(), amount);
        return new EconomyResponse(amount, EconomyManager.getBalance(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        Player p = Bukkit.getPlayer(playerName);
        assert p != null;
        EconomyManager.addBalance(p.getUniqueId(), amount);
        return new EconomyResponse(amount, EconomyManager.getBalance(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer p, String worldName, double amount) {
        EconomyManager.addBalance(p.getUniqueId(), amount);
        return new EconomyResponse(amount, EconomyManager.getBalance(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
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
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer p) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
