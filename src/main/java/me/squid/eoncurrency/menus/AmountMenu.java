package me.squid.eoncurrency.menus;

import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.squid.eoncurrency.Eoncurrency;
import org.bukkit.entity.Player;

public class AmountMenu extends Gui {
    private static MenuScheme DISPLAY = new MenuScheme().mask("");

    public AmountMenu(Player player, Eoncurrency plugin) {
        super(player, 5, "Amount Menu");
    }

    @Override
    public void redraw() {
        MenuPopulator populator = DISPLAY.newPopulator(this);
    }
}
