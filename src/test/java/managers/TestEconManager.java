package managers;

import me.squid.eoncurrency.managers.EconManager;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.*;
import org.mockito.verification.VerificationMode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TestEconManager {
    EconManager manager;
    Jedis jedis;

    private static OfflinePlayer player(String name) {
        OfflinePlayer player = mock(OfflinePlayer.class);
        when(player.getName()).thenReturn(name);
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
        OfflinePlayer player = player("Player");
        assertTrue(manager.hasAccount(player));
    }

    @Test
    @DisplayName("If a player does not exist, return false")
    void testPlayerNotExist() {
        when(jedis.get(anyString())).thenReturn("nil");
        OfflinePlayer player = player("Name");
        assertFalse(manager.hasAccount(player));
    }

    @Test
    @DisplayName("Expected value is returned given a normal value")
    void testNormalGetBalance() {
        when(jedis.get(anyString())).thenReturn("40506.45");
        OfflinePlayer player = player("Name");
        double balance = manager.getBalance(player);
        assertEquals(40506.45, balance);
    }

    @Test
    @DisplayName("If a balance is corrupted, return 0")
    void testCorruptBalance() {
        when(jedis.get(anyString())).thenReturn("CorruptBalance");
        OfflinePlayer player = player("Corrupted");
        double balance = manager.getBalance(player);
        assertEquals(0, balance);
    }

    @Test
    @DisplayName("If a connection is invalid, return 0")
    void testInvalidJedisConnection() {
        doThrow(new JedisConnectionException("Connection error")).when(jedis).get(anyString());
        OfflinePlayer player = player("No connection");
        assertEquals(0, manager.getBalance(player));
    }

    @Test
    @DisplayName("If a player does not exist, return 0")
    void testNotExist() {
        when(jedis.get(anyString())).thenReturn("nil");
        OfflinePlayer player = player("New Player");
        double balance = manager.getBalance(player);
        assertEquals(0, balance);
    }

    @Test
    @DisplayName("If a player has insufficient funds, then return false")
    void testInsufficientFunds() {
        when(jedis.get(anyString())).thenReturn("1.0");
        OfflinePlayer player = player("Broke");
        double needed = 2.0;
        assertFalse(manager.has(player, needed));
    }

    @Test
    @DisplayName("If a player has the exact amount, then return true")
    void testExactFunds() {
        when(jedis.get(anyString())).thenReturn("2.0");
        OfflinePlayer player = player("Equal");
        double needed = 2.0;
        assertTrue(manager.has(player, needed));
    }

    @Test
    @DisplayName("If a player has more than the amount, then return true")
    void testMoreFunds() {
        when(jedis.get(anyString())).thenReturn("10.0");
        OfflinePlayer player = player("More");
        double needed = 2.0;
        assertTrue(manager.has(player, needed));
    }

    @Test
    @DisplayName("Withdrawing to the negatives is an invalid transaction")
    void testInsufficientWithdrawal() {
        when(jedis.get(anyString())).thenReturn("20.0");
        OfflinePlayer player = player("Broke Player");
        double withdrawAmount = 30.0;
        EconomyResponse response = manager.withdrawPlayer(player, withdrawAmount);
        assertEquals(EconomyResponse.ResponseType.FAILURE, response.type);
        assertEquals(20.0, response.balance);
    }

    @Test
    @DisplayName("Withdrawing with an exact amount takes balance to 0")
    void testExactWithdrawal() {
        when(jedis.get(anyString())).thenReturn("100.0");
        OfflinePlayer player = player("Not Broke");
        double withdrawAmount = 100.0;
        EconomyResponse response = manager.withdrawPlayer(player, withdrawAmount);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(0.0, response.balance);
    }

    @Test
    @DisplayName("Withdrawing with plenty of funds is successful")
    void testSuccessfulWithdrawal() {
        when(jedis.get(anyString())).thenReturn("100.0");
        OfflinePlayer player = player("Rich");
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
        OfflinePlayer player = player("New Player");
        double amount = 10.0;
        EconomyResponse response = manager.depositPlayer(player, amount);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(10.0, response.balance);
    }

    @Test
    @DisplayName("Depositing into an existing account adds to the current balance")
    void testWithAccountDeposit() {
        when(jedis.get(anyString())).thenReturn("10.0");
        OfflinePlayer player = player("MVP");
        double amount = 20.0;
        EconomyResponse response = manager.depositPlayer(player, amount);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertEquals(30.0, response.balance);
        verify(jedis, times(1)).set(anyString(), anyString());
    }
}
