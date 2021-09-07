package me.squid.eoncurrency.listeners;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.events.*;
import me.squid.eoncurrency.jobs.Job;
import me.squid.eoncurrency.managers.JobsManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class WorldInteractListener implements Listener {

    Eoncurrency plugin;

    public WorldInteractListener(Eoncurrency plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Material material = e.getBlock().getType();
        Player p = e.getPlayer();
        World world = e.getPlayer().getWorld();
        Job job = JobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobBreakEvent(p, material, world, job)));
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        if (e.getPlayer().getWorld().getName().equals("spawn_void") && !e.getPlayer().isOp()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Component.text("Doesn't work"));
        }

        Material material = e.getBlock().getType();
        Player p = e.getPlayer();
        Job job = JobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobPlaceEvent(p, material, job)));
    }

    @EventHandler
    public void onFishEvent(PlayerFishEvent e) {
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            if (e.getCaught() == null) return;
            if (e.getCaught() instanceof Item item) {
                String name = item.getItemStack().getType().name().toLowerCase();
                Player p = e.getPlayer();
                Job job = JobsManager.getPlayerJob(p.getUniqueId());

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                        Bukkit.getPluginManager().callEvent(new JobFishEvent(p, name, job)));
            }
        }
    }

    @EventHandler
    public void onCowMilk(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof Cow && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BUCKET)) {
            e.getRightClicked().setCustomName("Milked lol");
            e.getRightClicked().setCustomNameVisible(true);
            e.getPlayer().sendMessage(Component.text("Milked lol"));
        }
    }

    @EventHandler
    public void onShearSheepEvent(PlayerShearEntityEvent e) {
        if (e.getEntity() instanceof Sheep sheep) {
            Player p = e.getPlayer();
            Job job = JobsManager.getPlayerJob(p.getUniqueId());

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                    Bukkit.getPluginManager().callEvent(new JobShearEvent(p, job, sheep)));
        }
    }

    @EventHandler
    public void onEnchantEvent(EnchantItemEvent e) {
        Player p = e.getEnchanter();
        Map<Enchantment, Integer> enchantMap = e.getEnchantsToAdd();
        ItemStack itemToEnchant = e.getItem();
        Job job = JobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobEnchantEvent(p, enchantMap, job, itemToEnchant)));
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent e) {
        Player p = e.getPlayer();
        int amount = e.getItemAmount();
        Material material = e.getItemType();
        Job job = JobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobBurnEvent(p, material, amount, job)));
    }

    @EventHandler
    public void onCraftEvent(InventoryClickEvent e) {
        if (e.getClickedInventory().getType().equals(InventoryType.WORKBENCH)) {
            if (e.getSlot() == 0) {
                ItemStack item = e.getCurrentItem();
                int amount = item.getAmount();
                Material type = item.getType();

                e.getWhoClicked().sendMessage(Component.text("Amount: " + amount));
                e.getWhoClicked().sendMessage(Component.text("Type: " + type.name().toLowerCase()));
            }
            e.getWhoClicked().sendMessage(Component.text("That is indeed a workbench"));
        }
    }
}
