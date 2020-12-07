package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.EconomyManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class BaltopCommand implements CommandExecutor {

    Eoncurrency plugin;

    public BaltopCommand(Eoncurrency plugin) {
        this.plugin = plugin;
        plugin.getCommand("baltop").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(getTopBalances());
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
        }
        return true;
    }

    private Inventory getTopBalances() {
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&b&lTop Balances"));
        int count = 1;

        for (UUID uuid : EconomyManager.getSortedMap().keySet()) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            Double money = EconomyManager.getBalance(p.getUniqueId());

            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(p);
            meta.setDisplayName("#" + count + ". " + p.getName());
            if (p.getPlayer() != null) {
                meta.setPlayerProfile(p.getPlayer().getPlayerProfile());
            }

            List<String> lore = new ArrayList<>();
            lore.add(Utils.chat("&aBalance: $" + money));
            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.addItem(item);
            count++;
        }

        return inv;
    }


}
