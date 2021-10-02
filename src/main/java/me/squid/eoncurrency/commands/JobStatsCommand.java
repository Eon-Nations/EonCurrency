package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.menus.JobStatsMenu;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JobStatsCommand implements CommandExecutor {

    Eoncurrency plugin;
    JobStatsMenu jobStatsMenu;

    public JobStatsCommand(Eoncurrency plugin, JobStatsMenu jobStatsMenu) {
        this.plugin = plugin;
        this.jobStatsMenu = jobStatsMenu;
        plugin.getCommand("jobstats").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) commandSender;
        p.openInventory(jobStatsMenu.getInv(p));
        Utils.playHarpSoundAtPlayer(p);
        return true;
    }
}
