package me.squid.eoncurrency.managers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class EconManager implements Economy {

    HashMap<UUID, Double> currency;
    LuckPerms luckPerms;

    public EconManager() {
        currency = new HashMap<>();
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }
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

    public double loadPlayer(OfflinePlayer player) {
        if (luckPerms.getUserManager().isLoaded(player.getUniqueId())) {
            Bukkit.getLogger().info("Player is loaded on startup");
        } else {
            Bukkit.getLogger().info("Player is not loaded on startup");
        }
        return 0.0;
    }

    public void savePlayer(OfflinePlayer player) {
        luckPerms.getUserManager().modifyUser(player.getUniqueId(), user -> {
            MetaNode currencyNode = MetaNode.builder("balance", currency.get(player.getUniqueId()).toString()).build();
            user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("balance")));
            user.data().add(currencyNode);
        });
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
        if (p.isOnline()) {
            return currency.containsKey(p.getUniqueId());
        } else {
            // This is a blocking method. However, by most plugins, this will not be called unless it is on an async thread.
            // So it should be fine to exist here.
            CompletableFuture<Boolean> boolFuture = luckPerms.getUserManager().loadUser(p.getUniqueId()).thenApplyAsync(user -> {
                Optional<MetaNode> optionalNode = user.getNodes(NodeType.META).stream()
                        .filter(node -> node.getMetaKey().equals("balance"))
                        .findFirst();
                return optionalNode.isPresent();
            });
            return boolFuture.join();
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
        return currency.getOrDefault(p.getUniqueId(), 0.0);
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
        if (p.isOnline()) {
            return currency.get(p.getUniqueId()) < amount;
        } else {
            // This is only to be called during async tasks. Other plugins shouldn't be messing with this if possible.
            CompletableFuture<Boolean> boolFuture = luckPerms.getUserManager().loadUser(p.getUniqueId()).thenApplyAsync(user -> {
                MetaNode currencyNode = user.getNodes(NodeType.META).stream().filter(node -> node.getMetaKey().equals("balance"))
                        .findFirst().orElseThrow();
                double balance = Double.parseDouble(currencyNode.getMetaKey().split(":")[1]);
                return balance < amount;
            });
            return boolFuture.join();
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
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Invalid Player.");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer p, double amount) {
        currency.put(p.getUniqueId(), getBalance(p) - amount);
        return new EconomyResponse(-amount, currency.get(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
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
        if (p.isOnline()) {
            currency.put(p.getUniqueId(), getBalance(p) + amount);
        } else {
            luckPerms.getUserManager().loadUser(p.getUniqueId()).thenAcceptAsync(user -> {
                MetaNode currencyNode = user.getNodes(NodeType.META).stream()
                                .filter(node -> node.getMetaKey().equals("balance")).findFirst()
                                .orElseThrow();
                double balance = Double.parseDouble(currencyNode.getMetaValue());
                currency.put(p.getUniqueId(), balance + amount);
            });
        }
        return new EconomyResponse(amount, currency.get(p.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer p, String worldName, double amount) {
        return depositPlayer(p, amount);
    }

    public @NotNull HashMap<UUID, Double> getOnlineSortedMap() {
        LinkedHashMap<UUID, Double> sortedMap = new LinkedHashMap<>();

        currency.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        return sortedMap;
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
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player != null) {
            return createPlayerAccount(player);
        } else return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer p) {
        if (!currency.containsKey(p.getUniqueId())) {
            currency.put(p.getUniqueId(), 0.0);
            savePlayer(p);
            return true;
        } else return false;
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
