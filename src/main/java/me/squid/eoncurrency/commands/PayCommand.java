package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

import static me.squid.eoncurrency.utils.Utils.configMessage;

public class PayCommand implements CommandExecutor {

    public enum PayStatus {
        SUCCESS,
        INSUFFICIENT,
        INVALID
    }
    Eoncurrency plugin;

    public PayCommand(Eoncurrency plugin) {
        this.plugin = plugin;
        plugin.getCommand("pay").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            return false;
        }
        if (args.length != 2) {
            p.sendMessage(configMessage(plugin, "usage-pay", Map.of()));
            return false;
        }
        Economy eco = plugin.getService(Economy.class);
        Player target = Bukkit.getPlayer(args[0]);
        double amount = Double.parseDouble(args[1]);
        amount = Double.parseDouble(eco.format(amount));

        if (target == null) {
            p.sendMessage(configMessage(plugin, "invalid-target", Map.of("<player>", args[0])));
            return false;
        }
        PayStatus status = payPlayer(p, target, amount);
        return switch (status) {
            case SUCCESS -> true;
            case INSUFFICIENT -> {
                p.sendMessage(configMessage(plugin, "insufficient-funds", Map.of()));
                yield false;
            }
            case INVALID -> false;
        };
    }

    public PayStatus payPlayer(Player sender, Player target, double amount) {
        Economy eco = plugin.getService(Economy.class);
        if (amount <= 0) return PayStatus.INVALID;
        EconomyResponse withdrawResponse = eco.withdrawPlayer(sender, amount);
        if (!withdrawResponse.transactionSuccess()) {
            return PayStatus.INSUFFICIENT;
        }
        eco.depositPlayer(target, amount);
        String formattedAmount = eco.format(amount);
        target.sendMessage(configMessage(plugin, "received-pay", Map.of("<player>", sender.getName(),
                "<amount>", formattedAmount)));
        sender.sendMessage(configMessage(plugin, "sender-pay", Map.of("<player>", target.getName(), "<amount>", formattedAmount)));
        return PayStatus.SUCCESS;
    }
}
