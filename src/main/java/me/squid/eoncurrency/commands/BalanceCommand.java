package me.squid.eoncurrency.commands;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.menus.BalanceMenu;
import me.squid.eoncurrency.menus.EcoMenu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    Economy economy;

    public BalanceCommand(Eoncurrency plugin, Economy economy) {
        this.economy = economy;
        plugin.getCommand("balance").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            sender.sendMessage("Balance: " + economy.getBalance(((Player) sender).getPlayer()));
            BalanceMenu menu = new BalanceMenu(p, economy);
            if (menu.isValid()) {
                menu.open();
                sender.sendMessage("Opening Menu...");
            }
            sender.sendMessage("Menu Valid: " + menu.isValid());
        }
        return true;
    }
}
