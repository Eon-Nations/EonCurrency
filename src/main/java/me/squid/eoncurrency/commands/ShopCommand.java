package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.menus.EcoMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ShopCommand implements CommandExecutor {

    Eoncurrency plugin;
    EcoMenu ecoMenu = new EcoMenu();

    public ShopCommand(Eoncurrency plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("shop")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(ecoMenu.ShopCategory());
        }

        return true;
    }
}
