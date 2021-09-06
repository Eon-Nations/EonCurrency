package me.squid.eoncurrency.managers;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.jobs.Jobs;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JobsManager {

    private static HashMap<UUID, Job> playerJobs;
    Eoncurrency plugin;

    public JobsManager(Eoncurrency plugin) {
        playerJobs = new HashMap<>();
        this.plugin = plugin;
    }

    public static Job getPlayerJob(UUID uuid) {
        return playerJobs.get(uuid);
    }

    public static List<UUID> getPlayersInJob(Jobs job) {
        List<UUID> toReturn = new ArrayList<>();
        for (UUID uuid : playerJobs.keySet()) {
            if (playerJobs.get(uuid).getEnumJob().equals(job)) {
                toReturn.add(uuid);
            }
        }
        return toReturn;
    }

    public static void addPlayerToJob(UUID uuid, Job job) {
        playerJobs.put(uuid, job);
    }

    public static void removePlayerFromJob(UUID uuid) {
        playerJobs.remove(uuid);
    }

    public static void loadPlayerFromSQL(Player p) {
        Job job = SQLManager.getPlayerJob(p.getUniqueId());
        addPlayerToJob(p.getUniqueId(), job);
    }

    public static void loadPlayerToSQL(Player p) {
        Job job = playerJobs.get(p.getUniqueId());

        if (SQLManager.jobPlayerExists(p.getUniqueId())) SQLManager.updatePlayerJob(p.getUniqueId(), job);
        else SQLManager.uploadPlayerJob(p.getUniqueId(), job);
        removePlayerFromJob(p.getUniqueId());
    }

    public static boolean playerExists(UUID uuid) {
        return playerJobs.containsKey(uuid);
    }
}
