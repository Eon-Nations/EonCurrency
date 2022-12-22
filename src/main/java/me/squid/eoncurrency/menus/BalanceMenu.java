package me.squid.eoncurrency.menus;

import fr.dwightstudio.dsmapi.MenuView;
import fr.dwightstudio.dsmapi.SimpleMenu;
import fr.dwightstudio.dsmapi.pages.PageType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BalanceMenu extends SimpleMenu {

    @Override
    public String getName() {
        return "<green>Balance Menu</green>";
    }

    @Override
    public ItemStack[] getContent() {
        ItemStack[][] contents = PageType.CHEST.getBlank2DArray();

        return PageType.CHEST.flatten(contents);
    }

    @Override
    public PageType getPageType() {
        return PageType.CHEST;
    }

    @Override
    public void onClick(MenuView menu, ClickType type, int slot, ItemStack item) {
        Player player = menu.getPlayer();
        // TODO Implementation
    }
}
