package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.menus.EcoMenu;
import me.squid.eoncurrency.menus.ShopMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {
    Eoncurrency plugin;

    public ShopCommand(Eoncurrency plugin) {
        this.plugin = plugin;
        plugin.getCommand("shop").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            ShopMenu menu = new ShopMenu(plugin, p);
            menu.open();
        }
        return true;
    }
}
