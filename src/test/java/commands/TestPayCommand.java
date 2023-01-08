package commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.managers.LocalEconManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static me.squid.eoncurrency.utils.Utils.configMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TestPayCommand {
    ServerMock server;
    Eoncurrency plugin;
    Economy economy;
    PlayerMock payer;
    PlayerMock payee;

    @BeforeEach
    void setup() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Eoncurrency.class);
        Bukkit.getServicesManager().unregister(Economy.class);
        LocalEconManager econ = new LocalEconManager();
        Bukkit.getServicesManager().register(Economy.class, econ, plugin, ServicePriority.Normal);
        economy = econ;
        payer = server.addPlayer("Payer");
        payee = server.addPlayer("Payee");
        resetBalances(payer);
        resetBalances(payee);
    }

    private void resetBalances(Player player) {
        economy.withdrawPlayer(player, economy.getBalance(player));
    }

    @Test
    @DisplayName("Happy case where both arguments are valid")
    void testPayPlayer() {
        PlayerMock payer = server.addPlayer("Payer");
        economy.depositPlayer(payer, 10.0);
        PlayerMock payee = server.addPlayer("Payee");
        payer.performCommand("pay Payee 10");
        assertEquals(0.0, economy.getBalance(payer));
        assertEquals(10.0, economy.getBalance(payee));
    }

    @Test
    @DisplayName("Incorrect number of arguments get recognized")
    void testIncorrectArgs() {
        PlayerMock player = server.addPlayer("Payer");
        economy.withdrawPlayer(player, economy.getBalance(player));
        economy.depositPlayer(player, 100);
        player.performCommand("pay Invalid 100 arguments");
        assertEquals(100, economy.getBalance(player));
        player.assertSaid(LegacyComponentSerializer.legacy('§').serialize(configMessage(plugin, "usage-pay", Map.of())));
    }

    @Test
    @DisplayName("Cannot pay player negative amounts of money")
    public void testNegativeAmount() {
        economy.depositPlayer(payee, 10.0);
        payer.performCommand("pay Payee -10.0");
        assertEquals(10.0, economy.getBalance(payee));
        assertEquals(0.0, economy.getBalance(payer));
    }

    @Test
    @DisplayName("Cannot pay player without enough money")
    public void testPayPlayer_InsufficientFunds() {
        economy.depositPlayer(payer, 10.0);
        payer.performCommand("pay Payee 15.0");
        assertEquals(10.0, economy.getBalance(payer));
        payer.assertSaid(LegacyComponentSerializer.legacy('§').serialize(configMessage(plugin, "insufficient-funds", Map.of())));
        assertEquals(0.0, economy.getBalance(payee));
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }
}
