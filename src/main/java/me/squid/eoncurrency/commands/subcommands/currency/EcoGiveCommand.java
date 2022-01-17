package me.squid.eoncurrency.commands.subcommands.currency;

import me.squid.eoncurrency.commands.SubCommand;
import me.squid.eoncurrency.managers.EconManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EcoGiveCommand extends SubCommand {

    EconManager econManager;

    public EcoGiveCommand(EconManager econManager) {
        this.econManager = econManager;
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getSyntax() {
        return "&bUsage: /economy give <player> <amount>";
    }

    @Override
    public String getDescription() {
        return "Gives the player any amount of money";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if (args.length == 3) {
                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                if (target != null) {
                    double amount = Double.parseDouble(args[2]);
                    econManager.depositPlayer(target, amount);
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &a" + target.getName() + " has received $" + amount));
                } else {
                    sender.sendMessage(Utils.chat("&7[&b&lEonEco&r&7] &aPlayer does not exist"));
                }
            }
        } else if (args.length == 1) {
            sender.sendMessage(ChatColor.BLUE + getSyntax());
        }
    }
}
