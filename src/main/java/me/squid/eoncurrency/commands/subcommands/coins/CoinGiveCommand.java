package me.squid.eoncurrency.commands.subcommands.coins;

import me.squid.eoncurrency.commands.SubCommand;
import me.squid.eoncurrency.managers.CoinManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinGiveCommand extends SubCommand {


    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getSyntax() {
        return "Usage: /eco give <player>";
    }
// /eco give player 40
    @Override
    public String getDescription() {
        return "Gives money to a player";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length > 1){
            if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null){
                    int amount = Integer.parseInt(args[2]);
                    CoinManager.addCoins(target.getUniqueId(), amount);
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &a" + target.getName() + " has received " + amount + " coins"));
                } else {
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &aPlayer is offline"));
                }
            }
        } else if (args.length == 1){
            sender.sendMessage(Utils.chat(ChatColor.BLUE + getSyntax()));
        }
    }
}
