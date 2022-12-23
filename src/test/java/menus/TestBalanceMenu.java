package menus;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.squid.eoncurrency.Eoncurrency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class TestBalanceMenu {
    private ServerMock server;
    private Eoncurrency plugin;

    @BeforeEach
    void setup() {
        this.server = MockBukkit.mock();
        this.plugin = MockBukkit.load(Eoncurrency.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }
}
