package managers;

import me.squid.eoncurrency.managers.LocalEconManager;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestLocalEconManager {
    LocalEconManager eco;
    @Mock
    OfflinePlayer player;

    @BeforeEach
    void setup() {
        UUID uuid = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(uuid);
        eco = new LocalEconManager();
    }

    @Test
    @DisplayName("If player does not have an account, then return false")
    void testNoAccount() {
        assertFalse(eco.hasAccount(player));
    }

    @Test
    @DisplayName("If player has an account, return true")
    void testHasAccount() {
        eco.createPlayerAccount(player);
        eco.depositPlayer(player, 10.0);
        assertTrue(eco.hasAccount(player));
    }

    @Test
    @DisplayName("Getting the balance of a non-existent player is 0")
    void testNoBalanceGet() {
        assertEquals(0.0, eco.getBalance(player));
    }

    @Test
    @DisplayName("Getting the balance when the player does exist returns balance")
    void testExistingBalance() {
        eco.depositPlayer(player, 10.0);
        assertEquals(10.0, eco.getBalance(player));
    }

    @Test
    @DisplayName("Has correctly indentifies that a player does not have enough")
    void testHasMethod() {
        eco.depositPlayer(player, 10.0);
        assertFalse(eco.has(player, 11.1));
        assertTrue(eco.has(player, 9.9));
    }

    @Test
    @DisplayName("Withdrawing too much from a player balance returns a Failure response")
    void testWithdrawInsufficient() {
        eco.depositPlayer(player, 10.0);
        EconomyResponse response = eco.withdrawPlayer(player, 11.0);
        assertEquals(EconomyResponse.ResponseType.FAILURE, response.type);
        assertEquals(10.0, eco.getBalance(player));
    }

    @Test
    @DisplayName("Withdrawing just enough from a player balance returns a SUCCESS response")
    void testWithdrawSufficient() {
        eco.depositPlayer(player, 10.0);
        EconomyResponse response = eco.withdrawPlayer(player, 9.0);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(1.0, response.balance);
        assertEquals(1.0, eco.getBalance(player));
    }
}
