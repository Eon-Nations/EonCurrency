package me.squid.eoncurrency.menus;

import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class BalanceMenu extends Gui {

    Economy economy;

    public BalanceMenu(Player player, Economy economy) {
        super(player, 1, "&aBalance Menu");
        this.economy = economy;
    }

    private static final MenuScheme DISPLAY = new MenuScheme().mask("100010000");

    @Override
    public void redraw() {
        MenuPopulator populator = DISPLAY.newPopulator(this);
        populator.accept(ItemStackBuilder.of(Material.PLAYER_HEAD)
                .transformMeta(meta -> ((SkullMeta) meta).setPlayerProfile(getPlayer().getPlayerProfile()))
                .name("&bName: " + getPlayer().getName())
                .buildItem().build()
        );
        populator.accept(ItemStackBuilder.of(Material.CLOCK)
                .name("&aBalance: $" + economy.getBalance(getPlayer()))
                .buildItem().build()
        );
        getPlayer().getInventory().addItem(new ItemStack(Material.DIRT, 64));
    }
}
