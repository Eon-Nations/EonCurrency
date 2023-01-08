package me.squid.eoncurrency.menus;

import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class ShopMenu extends Gui {

    private static final MenuScheme DISPLAY = new MenuScheme().mask("000111000");
    private Economy economy;

    public ShopMenu(Player player, Economy economy) {
        super(player, 1, "&5&lShop Categories");
    }

    @Override
    public void redraw() {
        MenuPopulator populator = DISPLAY.newPopulator(this);
    }
}
