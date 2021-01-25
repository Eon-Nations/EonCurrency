package me.squid.eoncurrency.menus;

import me.squid.eoncurrency.managers.CoinManager;
import me.squid.eoncurrency.managers.VaultEconManager;
import me.squid.eoncurrency.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class EcoMenu {

    VaultEconManager econManager = new VaultEconManager();

    public Inventory Main (Player p){
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&5&lCurrency Menu"));

        Utils.createItem(inv, Material.EMERALD_BLOCK, 1, 12, "&5&lEon Shop");
        Utils.createItem(inv, Material.CLOCK, 1, 14, "&a&lEco Info"
        , Utils.chat("&aBalance: $" + econManager.format(econManager.getBalance(p))));
        Utils.createItem(inv, Material.ZOMBIE_HEAD, 1, 16, "&b&lMob Arena Shop");
        Utils.makeDummySlots(inv);

        return inv;
    }

    public Inventory Others(OfflinePlayer p) {
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&5&lCurrency Menu"));

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

        if (p.getPlayer() != null) {
            skullMeta.setPlayerProfile(p.getPlayer().getPlayerProfile());
        }
        skullMeta.setDisplayName(Utils.chat("&b" + p.getName()));
        item.setItemMeta(skullMeta);

        inv.setItem(4, item);
        Utils.createItem(inv, Material.CLOCK, 1, 14, "&a&lEco Info"
                , Utils.chat("&aBalance: $" + econManager.format(econManager.getBalance(p))));
        Utils.makeDummySlots(inv);
        return inv;
    }

    public Inventory ShopCategory(){
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&5&lCategories"));

        Utils.createItem(inv, Material.STONE_BRICKS, 1, 12, "&f&lBlocks");
        Utils.createItem(inv, Material.OAK_BOAT, 1, 14, "&f&lMisc");
        Utils.createItem(inv, Material.HOPPER, 1, 16, "&7&lHopper Shop");
        Utils.makeDummySlots(inv);

        return inv;
    }

    public Inventory BlockShop(){
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&f&lBlocks"));

        Utils.createItem(inv, Material.SAND, 1, 12, "&f&lSand", Utils.chat("&5Price: $2"));
        Utils.createItem(inv, Material.COBBLESTONE, 1, 13, "&f&lCobblestone", Utils.chat("&5Price: $1"));
        Utils.createItem(inv, Material.GRAVEL, 1, 14, "&f&lGravel", Utils.chat("&5Price: $1"));
        Utils.createItem(inv, Material.DIRT, 1, 15, "&f&lDirt", Utils.chat("&5Price: $1"));
        Utils.createItem(inv, Material.STONE, 1, 16, "&f&lStone", Utils.chat("&5Price: $1.50"));
        Utils.makeDummySlots(inv);

        return inv;
    }

    public Inventory MiscShop() {
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&f&lMisc"));

        Utils.createItem(inv, Material.PHANTOM_MEMBRANE, 1, 11, "&f&lPhantom Membrane", Utils.chat("&5Price: $100"));
        Utils.createItem(inv, Material.ITEM_FRAME, 1, 13, "&f&lItem Frames", Utils.chat("&5Price: $750"));
        Utils.createItem(inv, Material.END_CRYSTAL, 1, 15, "&f&lEnd Crystal", Utils.chat("&5Price: $1000"));
        Utils.createItem(inv, Material.SHULKER_SHELL, 1, 17, "&f&lShulker Shell", Utils.chat("&5Price: $750"));
        Utils.makeDummySlots(inv);

        return inv;
    }

    public Inventory HopperShop(){
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&a&lHopper Shop"));

        Utils.createItem(inv, Material.HOPPER, 1, 14, "&7&lHopper", Utils.chat("&bPrice: $500"));
        Utils.makeDummySlots(inv);

        return inv;
    }

    public Inventory HopperAmount() {
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&a&lHopper Amount"));

        Utils.createItem(inv, Material.HOPPER, 1,2, "&f&l1");
        Utils.createItem(inv, Material.HOPPER, 2, 3, "&f&l2");
        Utils.createItem(inv, Material.HOPPER, 4, 4, "&f&l4");
        Utils.createItem(inv, Material.HOPPER, 8, 5, "&f&l8");
        Utils.createItem(inv, Material.HOPPER, 16, 6, "&f&l16");
        Utils.createItem(inv, Material.HOPPER, 32, 7, "&f&l32");
        Utils.createItem(inv, Material.HOPPER, 64, 8, "&f&l64");
        Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 23, "&aBuy More");
        Utils.makeDummySlots(inv);

        return inv;
    }

    public Inventory QuantityMenu(Material clicked){
        Inventory inv = Bukkit.createInventory(null, 27, Utils.chat("&a&lAmount"));

        Utils.createItem(inv, clicked, 1, 11, "&f&l1");
        Utils.createItem(inv, clicked, 2, 12, "&f&l2");
        Utils.createItem(inv, clicked, 4, 13, "&f&l4");
        Utils.createItem(inv, clicked, 8, 14, "&f&l8");
        Utils.createItem(inv, clicked, 16, 15, "&f&l16");
        Utils.createItem(inv, clicked, 32, 16, "&f&l32");
        Utils.createItem(inv, clicked, 64, 17, "&f&l64");
        Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 23, "&aBuy More");
        Utils.makeDummySlots(inv);

        return inv;
    }

    public Inventory StackMenu(Material clicked) {
        Inventory inv = Bukkit.createInventory(null, 9, Utils.chat("&a&lStacks"));

        Utils.createItem(inv, clicked, 64, 1, "&f&l1 Stack of Blocks");
        for (int i = 2; i <= 9; i++){
            Utils.createItem(inv, clicked, 64, i, "&f&l" + i + " Stacks of Blocks");
        }
        Utils.makeDummySlots(inv);

        return inv;
    }
}
