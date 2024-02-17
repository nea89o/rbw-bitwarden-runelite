package moe.nea.rbwrl;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.UsernameChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Unofficial Bitwarden CLI"
)
public class UnofficialBitwardenCliPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private UnofficialBitwardenCliConfig config;

    @Override
    protected void startUp() throws Exception {
        log.info("Unofficial Bitwarden CLI started!");
        tryInjectPassword();
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Unofficial Bitwarden CLI stopped!");
    }

    private void tryInjectPassword() {
        log.info("Trying to inject password!");
        if (passwordFetchingThread != null) passwordFetchingThread.interrupt();
        passwordFetchingThread = new Thread(() -> {
            try {
                String password = getPasswordForName(client.getUsername());
                client.setPassword(password);
            } catch (Exception e) {
                log.error("Exception while fetching password", e);
            }
        });
        passwordFetchingThread.setName("RBW Fetching thread");
        passwordFetchingThread.start();
    }

    @Subscribe
    public void onUsernameChanged(UsernameChanged event) {
        if (client.getGameState() != GameState.LOGIN_SCREEN) {
            return;
        }
        tryInjectPassword();
    }

    private String getPasswordForName(String username) {
        try {
            String binaryPath = config.getBinaryPath();
            if (binaryPath == null || binaryPath.isBlank())
                binaryPath = "rbw";
            ProcessBuilder pb = new ProcessBuilder(binaryPath, "get", "Runescape", username);
            pb.inheritIO().redirectOutput(ProcessBuilder.Redirect.PIPE);
            Process proc = pb.start();
            if (proc.waitFor() == 0) {
                return new String(proc.getInputStream().readAllBytes()).strip();
            }
            throw new RuntimeException(new String(proc.getInputStream().readAllBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Thread passwordFetchingThread;

    @Provides
    UnofficialBitwardenCliConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(UnofficialBitwardenCliConfig.class);
    }
}
