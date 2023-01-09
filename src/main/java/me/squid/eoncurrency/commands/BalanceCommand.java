package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.menus.BalanceMenu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    Eoncurrency plugin;

    public BalanceCommand(Eoncurrency plugin) {
        this.plugin = plugin;
        plugin.getCommand("balance").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            Economy eco = plugin.getService(Economy.class);
            BalanceMenu menu = new BalanceMenu(p, eco);
            menu.redraw();
            menu.open();
        }
        return true;
    }
}
