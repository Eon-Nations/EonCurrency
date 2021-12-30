package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.menus.JobChoiceMenu;
import me.squid.eoncurrency.menus.JobInfoMenu;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JobsCommand implements CommandExecutor {

    Eoncurrency plugin;
    JobChoiceMenu jobChoiceMenu;

    public JobsCommand(Eoncurrency plugin, JobInfoMenu jobInfoMenu) {
        jobChoiceMenu = new JobChoiceMenu(plugin, jobInfoMenu);
        this.plugin = plugin;
        plugin.getCommand("jobmenu").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player p = (Player) commandSender;
        p.openInventory(jobChoiceMenu.getInv());
        Utils.playHarpSoundAtPlayer(p);
        return true;
    }
}
