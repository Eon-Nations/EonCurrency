package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.VaultEconManager;
import me.squid.eoncurrency.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    Eoncurrency plugin;
    VaultEconManager vaultEconManager = new VaultEconManager();
    Economy eco = vaultEconManager;

    public PayCommand(Eoncurrency plugin) {
        this.plugin = plugin;
        plugin.getCommand("pay").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player p) {
            if (args.length == 2){
                Player target = Bukkit.getPlayer(args[0]);
                double amount = Double.parseDouble(args[1]);
                amount = Double.parseDouble(eco.format(amount));

                if (target != null && eco.has(p, amount) && amount > 0) {
                    eco.withdrawPlayer(p, amount);
                    eco.depositPlayer(target, amount);
                    target.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &b" + p.getName() + " has sent you $" + amount));
                    p.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &bYou have sent " + target.getName() + " $" + amount));
                } else {
                    p.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &4Transaction unsuccessful"));
                }
            } else {
                p.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &bCorrect usage: /pay <player> <amount>"));
            }
        }
        return true;
    }
}
