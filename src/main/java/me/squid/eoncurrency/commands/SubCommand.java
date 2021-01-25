package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.managers.EconomyManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract String getName();
    public abstract String getSyntax();
    public abstract String getDescription();

    public abstract void execute(CommandSender sender, String[] args);

}
