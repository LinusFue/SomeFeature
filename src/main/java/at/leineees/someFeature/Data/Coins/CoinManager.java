package at.leineees.someFeature.Data.Coins;

import at.leineees.someFeature.Feature.CustomScoreboardManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoinManager {

    private final Map<UUID, Integer> playerCoins = new HashMap<>();
    private final File coinsFile;
    private final FileConfiguration coinsConfig;
    private final CustomScoreboardManager scoreboardManager;

    public CoinManager(JavaPlugin plugin, CustomScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
        coinsFile = new File(plugin.getDataFolder(), "coins.yaml");
        if (!coinsFile.exists()) {
            try {
                coinsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        coinsConfig = YamlConfiguration.loadConfiguration(coinsFile);
        loadCoins();
    }

    public int getCoins(Player player) {
        return playerCoins.getOrDefault(player.getUniqueId(), 0);
    }

    public void addCoins(Player player, int amount) {
        UUID playerUUID = player.getUniqueId();
        int newAmount = getCoins(player) + amount;
        playerCoins.put(playerUUID, newAmount);
        coinsConfig.set(playerUUID.toString(), newAmount);
        saveCoins();
        scoreboardManager.updateCoins(player, getCoins(player), amount);
    }

    public void removeCoins(Player player, int amount) {
        UUID playerUUID = player.getUniqueId();
        int newAmount = getCoins(player) - amount;
        playerCoins.put(playerUUID, newAmount);
        coinsConfig.set(playerUUID.toString(), newAmount);
        saveCoins();
        scoreboardManager.updateCoins(player, getCoins(player), -amount);
    }

    public void setCoins(Player player, int amount) {
        UUID playerUUID = player.getUniqueId();
        playerCoins.put(playerUUID, amount);
        coinsConfig.set(playerUUID.toString(), amount);
        saveCoins();
        scoreboardManager.updateCoins(player, getCoins(player), amount);
    }

    private void loadCoins() {
        for (String key : coinsConfig.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            int coins = coinsConfig.getInt(key);
            playerCoins.put(playerUUID, coins);
        }
    }

    private void saveCoins() {
        try {
            coinsConfig.save(coinsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}