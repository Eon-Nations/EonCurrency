package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.menus.JobMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JobsCommand implements CommandExecutor {

    Eoncurrency plugin;
    JobMenu jobMenu;

    public JobsCommand(Eoncurrency plugin) {
        this.plugin = plugin;
        plugin.getCommand("jobmenu").setExecutor(this);
        jobMenu = new JobMenu(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player) commandSender;
        if (args.length == 0) {
            p.sendMessage(Component.text("Opening jobs menu..."));
            p.openInventory(jobMenu.getInv());
        }

        return true;
    }
}
