package me.squid.eoncurrency.commands.subcommands.coins;

import me.squid.eoncurrency.commands.SubCommand;
import me.squid.eoncurrency.managers.CoinManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinGetCommand extends SubCommand {

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getSyntax() {
        return "Usage: /eco get <player>";
    }

    @Override
    public String getDescription() {
        return "Gets the amount of money a person has";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length > 1){
            if (args.length == 2){
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    double amount = CoinManager.getCoins(target.getUniqueId());
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &a" + target.getName() + " has $" + amount));
                } else {
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &aPlayer is offline"));
                }
            }
        } else if (args.length == 1){
            sender.sendMessage(Utils.chat(ChatColor.BLUE + getSyntax()));
        }
    }
}
