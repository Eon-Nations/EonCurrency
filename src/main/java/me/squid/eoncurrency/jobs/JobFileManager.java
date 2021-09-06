package me.squid.eoncurrency.jobs;

import me.squid.eoncurrency.Eoncurrency;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JobFileManager {

    Eoncurrency plugin;
    String basePath;

    public JobFileManager(Eoncurrency plugin) {
        this.plugin = plugin;
        basePath = plugin.getDataFolder().getPath() + File.separator + "jobs";
        createJobDirectory();
        createJobFiles();
    }



    private void createJobDirectory() {
        if (!new File(basePath).exists()) {
            try {
                Path path = Paths.get(basePath);
                Files.createDirectories(path);
                System.out.println("Created jobs directory!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createJobFiles() {
        for (Jobs job : Jobs.values()) {
            try {
                File file = new File(basePath + File.separator + job.name().toLowerCase() + ".yml");
                if (file.createNewFile()) {
                    System.out.println(job.name().toLowerCase() + ".yml has been created");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public double getPriceForAction(String action, Job job, Material material) {
        return 0;
    }

    public double getExperienceForAction(String action, Job job, Material material) {
        return 0;
    }

}
