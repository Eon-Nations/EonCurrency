package me.squid.eoncurrency.commands.subcommands.coins;

import me.squid.eoncurrency.commands.SubCommand;
import me.squid.eoncurrency.managers.CoinManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinResetCommand extends SubCommand {

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getSyntax() {
        return "Usage: /eco reset <player>";
    }

    @Override
    public String getDescription() {
        return "Resets the players balance to 0";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length > 1){
            if (args.length == 2){
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null){
                    CoinManager.setCoins(target.getUniqueId(), 0);
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &aYou have reset " + target.getName() + "'s balance to $0"));
                } else {
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &aPlayer is offline"));
                }
            }
        } else if (args.length == 1){
            sender.sendMessage(Utils.chat(ChatColor.BLUE + getSyntax()));
        }
    }
}
