package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.EconManager;
import me.squid.eoncurrency.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    Eoncurrency plugin;
    EconManager econManager;

    public PayCommand(Eoncurrency plugin, EconManager econManager) {
        this.plugin = plugin;
        this.econManager = econManager;
        plugin.getCommand("pay").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player p) {
            if (args.length == 2){
                Player target = Bukkit.getPlayer(args[0]);
                double amount = Double.parseDouble(args[1]);
                amount = Double.parseDouble(econManager.format(amount));

                if (target != null && econManager.has(p, amount) && amount > 0) {
                    econManager.withdrawPlayer(p, amount);
                    econManager.depositPlayer(target, amount);
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
