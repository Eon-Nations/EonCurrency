package me.squid.eoncurrency.managers;

import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {

    private final Economy provider = new VaultEconManager();

    public void hook(){
        Bukkit.getServicesManager().register(Economy.class, provider, Eoncurrency.getPlugin(Eoncurrency.class), ServicePriority.Normal);
        Bukkit.getConsoleSender().sendMessage(Utils.chat("&aVault has successfully hooked to Economy"));
    }

    public void unhook(){
        Bukkit.getServicesManager().unregister(Economy.class, provider);
    }
}
