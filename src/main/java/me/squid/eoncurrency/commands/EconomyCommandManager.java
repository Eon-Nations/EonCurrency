package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.commands.subcommands.currency.EcoGetCommand;
import me.squid.eoncurrency.commands.subcommands.currency.EcoGiveCommand;
import me.squid.eoncurrency.commands.subcommands.currency.EcoResetCommand;
import me.squid.eoncurrency.commands.subcommands.currency.EcoTakeCommand;
import me.squid.eoncurrency.managers.EconManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EconomyCommandManager implements CommandExecutor {

    Eoncurrency plugin;

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public EconomyCommandManager(Eoncurrency plugin, EconManager econManager) {
        this.plugin = plugin;
        subCommands.add(new EcoGetCommand(econManager));
        subCommands.add(new EcoGiveCommand(econManager));
        subCommands.add(new EcoResetCommand(econManager));
        subCommands.add(new EcoTakeCommand(econManager));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0){
            for (SubCommand subCommand : subCommands) {
                if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    subCommand.execute(sender, args);
                    break;
                }
            }
        } else {
            sender.sendMessage(Utils.chat("&7[------------------------------]"));
            sender.sendMessage(Utils.chat("      &7[&5Eon Survival Economy&r&7]"));
            sender.sendMessage(Utils.chat(""));
            sender.sendMessage(Utils.chat("&7&b/economy give <player> <amount> (Adds money)"));
            sender.sendMessage(Utils.chat("&7&b/economy take <player> <amount> (Takes money)"));
            sender.sendMessage(Utils.chat("&7&b/economy reset <player> (Reset balance to 0)"));
            sender.sendMessage(Utils.chat("&7&b/economy get <player> (Gets the amount)"));
            sender.sendMessage(Utils.chat("&7[------------------------------]"));
        }

        return true;
    }
}
