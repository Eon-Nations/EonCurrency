package me.squid.eoncurrency.commands.subcommands.currency;

import me.squid.eoncurrency.commands.SubCommand;
import me.squid.eoncurrency.managers.EconManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class EcoGetCommand extends SubCommand {
    EconManager econManager;

    public EcoGetCommand(EconManager econManager) {
        this.econManager = econManager;
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getSyntax() {
        return "Usage: /economy get <player>";
    }

    @Override
    public String getDescription() {
        return "Gets the amount of money a player has";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1){
            if (args.length == 2) {
                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                if (target != null) {
                    double amount = econManager.getBalance(target);
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &a" + target.getName() + " has $" + amount));
                } else {
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &aPlayer does not exist"));
                }
            }
        } else if (args.length == 1) {
            sender.sendMessage(ChatColor.BLUE + getSyntax());
        }
    }
}
