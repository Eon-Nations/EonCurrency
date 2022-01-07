package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.EconManager;
import me.squid.eoncurrency.utils.Utils;
import net.kyori.adventure.text.Component;
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

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class BaltopCommand implements CommandExecutor {

    Eoncurrency plugin;
    EconManager econManager;

    public BaltopCommand(Eoncurrency plugin, EconManager econManager) {
        this.plugin = plugin;
        this.econManager = econManager;
        plugin.getCommand("baltop").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            p.openInventory(getTopBalances());
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
        }
        return true;
    }

    private Inventory getTopBalances() {
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&b&lTop Balances"));
        int count = 1;

        for (UUID uuid : econManager.getOnlineSortedMap().keySet()) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            DecimalFormat df = new DecimalFormat("#.##");
            double money = econManager.getBalance(p);
            money = Double.parseDouble(df.format(money));

            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(p);
            meta.displayName(Component.text(Utils.chat("&a#" + count + ". &b" + p.getName())));
            if (p.getPlayer() != null) {
                meta.setPlayerProfile(p.getPlayer().getPlayerProfile());
            }

            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(Utils.chat("&aBalance: $" + money)));
            meta.lore(lore);
            item.setItemMeta(meta);

            inv.addItem(item);
            ++count;
        }

        return inv;
    }


}
