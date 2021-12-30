package me.squid.eoncurrency.menus;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.jobs.JobFileManager;
import me.squid.eoncurrency.jobs.Jobs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class JobInfoMenu implements Listener {

    Eoncurrency plugin;
    List<String> actions;
    Map<String, Material> actionToMaterialMap;
    JobFileManager jobFileManager;

    public JobInfoMenu(Eoncurrency plugin, JobFileManager jobFileManager) {
        this.plugin = plugin;
        this.actions = plugin.getConfig().getStringList("actions");
        this.actionToMaterialMap = initializeActionToMaterialMap();
        this.jobFileManager = jobFileManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJobInfoMenuClick(InventoryClickEvent e) {
        if (e.getView().title().equals(getTitle((Player) e.getWhoClicked()))) {
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(Component.text("Nice"));
        }
    }

    public Inventory getInventory(Player p, Object job) {
        Inventory inv = Bukkit.createInventory(null, 54, getTitle(p));
        Job blankJob = null;
        if (job instanceof Jobs) {
            blankJob = new Job((Jobs) job);
        } else if (job instanceof Job) {
            blankJob = (Job) job;
        }
        assert blankJob != null;

        for (String action : actions) {
            Map<String, Double> materialMap = jobFileManager.getPricesFromAction(action, blankJob.getEnumJob());
            ItemStack item = new ItemStack(actionToMaterialMap.get(action), 1);
            ItemMeta meta = item.getItemMeta();
            List<Component> lore = new ArrayList<>();

            for (String matString : materialMap.keySet()) {
                double price = materialMap.get(matString);
                lore.add(Component.text(matString + ": $" + (price * blankJob.getExp())));
            }
            meta.lore(lore);
            meta.displayName(Component.text(action).color(TextColor.color(0, 255, 0)));
            item.setItemMeta(meta);
            inv.addItem(item);
        }
        return inv;
    }

    private HashMap<String, Material> initializeActionToMaterialMap() {
        HashMap<String, Material> retVal = new HashMap<>();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("action-material-mapping");
        Map<String, ?> map = section.getValues(false);

        for (String action : map.keySet()) {
            String matString = (String) map.get(action);
            Material material = Material.valueOf(matString.toUpperCase(Locale.ENGLISH));
            retVal.put(action, material);
        }
        return retVal;
    }

    private Component getTitle(Player p) {
        return Component.text(p.getName() + "'s Stats").color(TextColor.color(0, 255, 255));
    }
}
