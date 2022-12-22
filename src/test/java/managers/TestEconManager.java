package managers;

import me.squid.eoncurrency.managers.EconManager;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TestEconManager {
    EconManager manager;
    Jedis jedis;

    private static OfflinePlayer player() {
        OfflinePlayer player = mock(OfflinePlayer.class);
        when(player.getName()).thenReturn("Name");
        return player;
    }

    @BeforeEach
    void setupEach() {
        JedisPool pool = mock(JedisPool.class);
        jedis = mock(Jedis.class);
        when(pool.getResource()).thenReturn(jedis);
        manager = new EconManager(pool);
    }

    @Test
    @DisplayName("Given a two decimal number, output the same number in a string format")
    void testValidFormat() {
        double value = 10.12;
        String actualFormat = manager.format(value);
        String expectedFormat = "10.12";
        Assertions.assertEquals(expectedFormat, actualFormat);
    }

    @Test
    @DisplayName("Given zero, pad the decimal places with zeros")
    void testZeroBalance() {
        double zero = 0;
        String actualFormat = manager.format(zero);
        String expectedFormat = "0.00";
        Assertions.assertEquals(expectedFormat, actualFormat);
    }

    @Test
    @DisplayName("Given a number, pad the decimal places with zeros")
    void testAddingZeros() {
        double wholeNumber = 12;
        String actualFormat = manager.format(wholeNumber);
        String expectedFormat = "12.00";
        Assertions.assertEquals(expectedFormat, actualFormat);
    }

    @Test
    @DisplayName("Given a number with more than 2 decimal places, round to the nearest two")
    void testRounding() {
        double weirdBalance = 12.5678;
        String actualFormat = manager.format(weirdBalance);
        String expectedFormat = "12.57";
        Assertions.assertEquals(expectedFormat, actualFormat);
    }

    @Test
    @DisplayName("If a player exists, return true")
    void testSuccessfulLookup() {
        when(jedis.get(anyString())).thenReturn("40.59");
        OfflinePlayer player = player();
        assertTrue(manager.hasAccount(player));
    }

    @Test
    @DisplayName("If a player does not exist, return false")
    void testPlayerNotExist() {
        when(jedis.get(anyString())).thenReturn("nil");
        OfflinePlayer player = player();
        assertFalse(manager.hasAccount(player));
    }

    @Test
    @DisplayName("Expected value is returned given a normal value")
    void testNormalGetBalance() {
        when(jedis.get(anyString())).thenReturn("40506.45");
        OfflinePlayer player = player();
        double balance = manager.getBalance(player);
        assertEquals(40506.45, balance);
    }

    @Test
    @DisplayName("If a balance is corrupted, return 0")
    void testCorruptBalance() {
        when(jedis.get(anyString())).thenReturn("CorruptBalance");
        OfflinePlayer player = player();
        double balance = manager.getBalance(player);
        assertEquals(0, balance);
    }

    @Test
    @DisplayName("If a connection is invalid, return 0")
    void testInvalidJedisConnection() {
        doThrow(new JedisConnectionException("Connection error")).when(jedis).get(anyString());
        OfflinePlayer player = player();
        assertEquals(0, manager.getBalance(player));
    }

    @Test
    @DisplayName("If a player does not exist, return 0")
    void testNotExist() {
        when(jedis.get(anyString())).thenReturn("nil");
        OfflinePlayer player = player();
        double balance = manager.getBalance(player);
        assertEquals(0, balance);
    }

    @Test
    @DisplayName("If a player has insufficient funds, then return false")
    void testInsufficientFunds() {
        when(jedis.get(anyString())).thenReturn("1.0");
        OfflinePlayer player = player();
        double needed = 2.0;
        assertFalse(manager.has(player, needed));
    }

    @Test
    @DisplayName("If a player has the exact amount, then return true")
    void testExactFunds() {
        when(jedis.get(anyString())).thenReturn("2.0");
        OfflinePlayer player = player();
        double needed = 2.0;
        assertTrue(manager.has(player, needed));
    }

    @Test
    @DisplayName("If a player has more than the amount, then return true")
    void testMoreFunds() {
        when(jedis.get(anyString())).thenReturn("10.0");
        OfflinePlayer player = player();
        double needed = 2.0;
        assertTrue(manager.has(player, needed));
    }

    @Test
    @DisplayName("Withdrawing to the negatives is an invalid transaction")
    void testInsufficientWithdrawal() {
        when(jedis.get(anyString())).thenReturn("20.0");
        OfflinePlayer player = player();
        double withdrawAmount = 30.0;
        EconomyResponse response = manager.withdrawPlayer(player, withdrawAmount);
        assertEquals(EconomyResponse.ResponseType.FAILURE, response.type);
        assertEquals(20.0, response.balance);
    }

    @Test
    @DisplayName("Withdrawing with an exact amount takes balance to 0")
    void testExactWithdrawal() {
        when(jedis.get(anyString())).thenReturn("100.0");
        OfflinePlayer player = player();
        double withdrawAmount = 100.0;
        EconomyResponse response = manager.withdrawPlayer(player, withdrawAmount);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(0.0, response.balance);
    }

    @Test
    @DisplayName("Withdrawing with plenty of funds is successful")
    void testSuccessfulWithdrawal() {
        when(jedis.get(anyString())).thenReturn("100.0");
        OfflinePlayer player = player();
        double amount = 50.0;
        EconomyResponse response = manager.withdrawPlayer(player, amount);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(50.0, response.balance);
    }

    @Test
    @DisplayName("Depositing into an empty account creates a new one")
    void testNoAccountDeposit() {
        when(jedis.get(anyString())).thenReturn("nil");
        when(jedis.set(anyString(), anyString())).then(mock -> when(jedis.get(anyString())).thenReturn("0.0").getMock().toString());
        OfflinePlayer player = player();
        double amount = 10.0;
        EconomyResponse response = manager.depositPlayer(player, amount);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(10.0, response.balance);
    }

    @Test
    @DisplayName("Depositing into an existing account adds to the current balance")
    void testWithAccountDeposit() {
        when(jedis.get(anyString())).thenReturn("10.0");
        OfflinePlayer player = player();
        double amount = 20.0;
        EconomyResponse response = manager.depositPlayer(player, amount);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(30.0, response.balance);
        verify(jedis, times(1)).set(anyString(), anyString());
    }

    @Test
    @DisplayName("Creating an already existing player account changes nothing")
    void testExistingPlayer() {
        when(jedis.get(anyString())).thenReturn("100.0");
        OfflinePlayer player = player();
        boolean actual = manager.createPlayerAccount(player);
        assertFalse(actual);
        verify(jedis, times(0)).set(anyString(), anyString());
    }

    @Test
    @DisplayName("Create a player account when they don't exist")
    void testCreateNewAccount() {
        when(jedis.get(anyString())).thenReturn("nil");
        when(jedis.set(anyString(), anyString())).then(mock -> when(jedis.get(anyString())).thenReturn("0.0").getMock().toString());
        OfflinePlayer player = player();
        boolean actual = manager.createPlayerAccount(player);
        assertTrue(actual);
        assertEquals(0.0, manager.getBalance(player));
    }

    @Test
    @DisplayName("Create a player account when the value is corrupt")
    void testCreateAccountCorrupt() {
        when(jedis.get(anyString())).thenReturn("Corrupt");
        when(jedis.set(anyString(), anyString())).then(mock -> when(jedis.get(anyString())).thenReturn("0.0").getMock().toString());
        OfflinePlayer player = player();
        boolean actual = manager.createPlayerAccount(player);
        assertTrue(actual);
        assertEquals(0.0, manager.getBalance(player));
    }

    @Test
    @DisplayName("Exception thrown is handled and returns false")
    void testExceptionCreateAccount() {
        when(jedis.get(anyString())).thenThrow(new JedisException("Connection error"));
        OfflinePlayer player = player();
        boolean actual = manager.createPlayerAccount(player);
        assertFalse(actual);
        assertEquals(0.0, manager.getBalance(player));
    }
}
