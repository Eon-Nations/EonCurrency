package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.commands.subcommands.coins.CoinGetCommand;
import me.squid.eoncurrency.commands.subcommands.coins.CoinGiveCommand;
import me.squid.eoncurrency.commands.subcommands.coins.CoinResetCommand;
import me.squid.eoncurrency.commands.subcommands.coins.CoinTakeCommand;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CoinCommandManager implements CommandExecutor {

    Eoncurrency plugin;

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CoinCommandManager(Eoncurrency plugin) {
        this.plugin = plugin;
        subCommands.add(new CoinGiveCommand());
        subCommands.add(new CoinTakeCommand());
        subCommands.add(new CoinGetCommand());
        subCommands.add(new CoinResetCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;

            if (args.length > 0){
                for (SubCommand subCommand : subCommands) {
                    if (args[0].equalsIgnoreCase(subCommand.getName())) {
                        subCommand.execute(p, args);
                        break;
                    }
                }
            } else {
                p.sendMessage(Utils.chat("&7[------------------------------]"));
                p.sendMessage(Utils.chat("      &7[&bMob Arena Coins&r&7]"));
                p.sendMessage(Utils.chat(""));
                p.sendMessage(Utils.chat("&7&b/coins give <player> <amount> (Adds coins)"));
                p.sendMessage(Utils.chat("&7&b/coins take <player> <amount> (Takes coins)"));
                p.sendMessage(Utils.chat("&7&b/coins reset <player> (Resets coins to 0)"));
                p.sendMessage(Utils.chat("&7&b/coins get <player> (Gets the amount of coins)"));
                p.sendMessage(Utils.chat("&7[------------------------------]"));
            }
        }
        return true;
    }
}
