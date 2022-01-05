package me.squid.eoncurrency.commands.subcommands.currency;

import me.squid.eoncurrency.commands.SubCommand;
import me.squid.eoncurrency.managers.VaultEconManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EcoResetCommand extends SubCommand {

    VaultEconManager econManager = new VaultEconManager();

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getSyntax() {
        return "Usage: /economy reset <player>";
    }

    @Override
    public String getDescription() {
        return "Resets the balance of a player to $0";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1){
            if (args.length == 2){
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null){
                    econManager.withdrawPlayer(target, econManager.getBalance(target));
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &a" + target.getName() + "'s balance has been reset to $0"));
                } else {
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &aPlayer is offline"));
                }
            }
        } else if (args.length == 1){
            sender.sendMessage(ChatColor.BLUE + getSyntax());
        }
    }
}
