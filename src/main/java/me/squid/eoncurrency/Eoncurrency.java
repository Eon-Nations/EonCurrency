package me.squid.eoncurrency;

import me.lucko.helper.Services;
import me.lucko.helper.config.ConfigFactory;
import me.lucko.helper.config.ConfigurationNode;
import me.lucko.helper.plugin.HelperPlugin;
import me.lucko.helper.terminable.composite.CompositeTerminable;
import me.lucko.helper.utils.CommandMapUtil;
import me.squid.eoncurrency.commands.BalanceCommand;
import me.squid.eoncurrency.commands.EconomyCommandManager;
import me.squid.eoncurrency.commands.PayCommand;
import me.squid.eoncurrency.commands.ShopCommand;
import me.squid.eoncurrency.listeners.ShopMenuListener;
import me.squid.eoncurrency.managers.EconManager;
import me.squid.eoncurrency.menus.EcoMenu;
import me.squid.eoncurrency.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.JedisPool;

import java.io.File;

public final class Eoncurrency extends JavaPlugin implements HelperPlugin {
    EconManager econManager;
    JedisPool pool;
    CompositeTerminable registry;

    public Eoncurrency() {
        super();
        this.registry = CompositeTerminable.create();
        this.pool = setupPool();
        this.econManager = hookToVault();
    }

    public Eoncurrency(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        this.registry = CompositeTerminable.create();
        this.pool = setupPool();
        this.econManager = hookToVault();
    }

    @Override
    public void onEnable() {
        this.pool = setupPool();
        this.econManager = hookToVault();
        saveDefaultConfig();
        EcoMenu ecoMenu = new EcoMenu(econManager);
        registerCommands(ecoMenu);
        registerListeners(ecoMenu);
    }

    @Override
    public void onDisable() {
        unHookVault();
    }

    @SuppressWarnings("ConstantConditions")
    public void registerCommands(EcoMenu ecoMenu) {
        getCommand("economy").setExecutor(new EconomyCommandManager(this, econManager));
        new PayCommand(this, econManager);
        new BalanceCommand(this, econManager);
        new ShopCommand(this, ecoMenu);
    }

    public void registerListeners(EcoMenu ecoMenu) {
        // TODO Fix Join Listener
        new ShopMenuListener(this, ecoMenu, econManager);
    }

    public JedisPool setupPool() {
        String serverURL = System.getProperty("REDIS_URL");
        if (serverURL == null) {
            serverURL = "redis://172.20.0.3";
        }
        return new JedisPool(serverURL, 6379);
    }

    public EconManager hookToVault() {
        EconManager econ = new EconManager(pool);
        Bukkit.getServicesManager().register(Economy.class, econ, this, ServicePriority.Normal);
        Bukkit.getConsoleSender().sendMessage(Utils.chat("&aVault has successfully hooked to Economy"));
        return econ;
    }

    public void unHookVault() {
        Bukkit.getServicesManager().unregister(Economy.class, econManager);
    }

    @NotNull
    @Override
    public <T extends Listener> T registerListener(@NotNull T listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
        return listener;
    }

    @NotNull
    @Override
    public <T extends CommandExecutor> T registerCommand(@NotNull T command, String permission, String permissionMessage, String description, @NotNull String... aliases) {
        return CommandMapUtil.registerCommand(this, command, permission, permissionMessage, description, aliases);
    }

    @NotNull
    @Override
    public <T> T getService(@NotNull Class<T> service) {
        return Services.load(service);
    }

    @NotNull
    @Override
    public <T> T provideService(@NotNull Class<T> service, @NotNull T t, @NotNull ServicePriority servicePriority) {
        return Services.provide(service, t, servicePriority);
    }

    @NotNull
    @Override
    public <T> T provideService(@NotNull Class<T> service, @NotNull T t) {
        return Services.provide(service, t);
    }

    @Override
    public boolean isPluginPresent(@NotNull String s) {
        return getServer().getPluginManager().getPlugin(s) != null;
    }

    @Nullable
    @Override
    public <T> T getPlugin(@NotNull String pluginName, @NotNull Class<T> plugin) {
        return (T) getServer().getPluginManager().getPlugin(pluginName);
    }

    private File getRelativeFile(String path) {
        this.getDataFolder().mkdirs();
        return new File(this.getDataFolder(), path);
    }

    @NotNull
    @Override
    public File getBundledFile(@NotNull String name) {
        File file = getRelativeFile(name);
        if (!file.exists()) {
            saveResource(name, false);
        }
        return file;
    }

    @NotNull
    @Override
    public YamlConfiguration loadConfig(@NotNull String file) {
        return YamlConfiguration.loadConfiguration(getBundledFile(file));
    }

    @NotNull
    @Override
    public ConfigurationNode loadConfigNode(@NotNull String file) {
        return ConfigFactory.yaml().load(getBundledFile(file));
    }

    @NotNull
    @Override
    public <T> T setupConfig(@NotNull String f, @NotNull T configObj) {
        File file = getRelativeFile(f);
        ConfigFactory.yaml().load(file, configObj);
        return configObj;
    }

    @NotNull
    @Override
    public ClassLoader getClassloader() {
        return super.getClassLoader();
    }

    @NotNull
    @Override
    public <T extends AutoCloseable> T bind(@NotNull T terminable) {
        return this.registry.bind(terminable);
    }
}
