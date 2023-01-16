package me.squid.eoncurrency.menus;

import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.squid.eoncurrency.Eoncurrency;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class ShopMenu extends Gui {

    private static final MenuScheme DISPLAY = new MenuScheme().mask("000111000");
    private Eoncurrency plugin;

    public ShopMenu(Eoncurrency plugin, Player player) {
        super(player, 1, "&5&lShop Categories");
        this.plugin = plugin;
    }

    @Override
    public void redraw() {
        Economy eco = plugin.getService(Economy.class);
        MenuPopulator populator = DISPLAY.newPopulator(this);
    }
}
