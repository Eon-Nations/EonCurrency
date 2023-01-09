package menus;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.profile.PlayerProfileMock;
import com.destroystokyo.paper.profile.PlayerProfile;
import me.squid.eoncurrency.Eoncurrency;
import me.squid.eoncurrency.menus.BalanceMenu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestBalanceMenu {
    private ServerMock server;
    private Eoncurrency plugin;
    private Economy economy;

    static class ProfilePlayer extends PlayerMock {
        PlayerProfile profile;
        public ProfilePlayer(ServerMock server, String name) {
            super(server, name);
            this.profile = new PlayerProfileMock(this);
        }

        @Override
        public @NotNull PlayerProfile getPlayerProfile() {
            return profile;
        }

        @Override
        public void setPlayerProfile(@NotNull PlayerProfile newProfile) {
            this.profile = newProfile;
        }
    }

    @BeforeEach
    void setup() {
        this.server = MockBukkit.mock();
        this.plugin = MockBukkit.load(Eoncurrency.class);
        this.economy = mock(Economy.class);
        when(economy.getBalance(any(OfflinePlayer.class))).thenReturn(0.0);
    }

    @Test
    @Disabled("Need a fix for SkullMeta not being implemented")
    void test() {
        ProfilePlayer player = new ProfilePlayer(server, "Player");
        server.addPlayer(player);
        BalanceMenu menu = new BalanceMenu(player, economy);
        menu.redraw();
        menu.open();
        player.assertInventoryView(InventoryType.CHEST);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }
}
