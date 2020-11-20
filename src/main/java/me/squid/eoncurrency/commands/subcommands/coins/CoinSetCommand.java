package me.squid.eoncurrency.commands.subcommands.coins;

import me.squid.eoncurrency.commands.SubCommand;
import me.squid.eoncurrency.managers.CoinManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoinSetCommand extends SubCommand {

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getSyntax() {
        return "Usage: /eco set <player> <amount>";
    }

    @Override
    public String getDescription() {
        return "Set a players balance to a certain amount of currency";
    }

    @Override
    public void execute(Player p, String[] args) {

        if (args.length > 1){
            if (args.length == 3){
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null){
                    int amount = Integer.parseInt(args[2]);
                    CoinManager.setCoins(p.getUniqueId(), amount);
                    p.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &aYou have set " + target.getName() + "'s balance to $" + amount));
                } else {
                    p.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &aPlayer is offline"));
                }
            }
        } else if (args.length == 1){
            p.sendMessage(Utils.chat(ChatColor.BLUE + getSyntax()));
        }
    }
}
