package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.managers.JobsManager;
import me.squid.eoncurrency.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JobStatsCommand implements CommandExecutor {

    Eoncurrency plugin;

    public JobStatsCommand(Eoncurrency plugin) {
        this.plugin = plugin;
        plugin.getCommand("jobstats").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) commandSender; Job job;
        try {
            job = JobsManager.getPlayerJob(p.getUniqueId());
        } catch (NullPointerException e) {
            p.sendMessage(Utils.getPrefix("nations")
                    .append(Component.text("Enter /jobsmenu to join a job")
                            .color(TextColor.color(160, 160, 160))));
            return true;
        }
        TextColor color = TextColor.color(160, 160, 160);

        p.sendMessage(Utils.getPrefix("nations"));
        p.sendMessage(Component.text("Job: " + StringUtils.capitalize(job.getEnumJob().name().toLowerCase()))
                .color(color));
        p.sendMessage(Component.text("Level: " + job.getLevel()).color(color));
        p.sendMessage(Component.text("Total Experience: " + Utils.round(job.getExp(), 2))
                .color(color));
        p.sendMessage(Component.text("Experience Needed for Next Level: " +
                        Utils.round(getNeededExperience(job), 2)).color(color));

        Utils.playHarpSoundAtPlayer(p);
        return true;
    }

    private double getNeededExperience(Job job) {
        return Math.pow(job.getLevel() + 1, 2) / 0.0625;
    }
}
