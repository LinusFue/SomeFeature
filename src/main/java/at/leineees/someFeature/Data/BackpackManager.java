package at.leineees.someFeature.Data;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BackpackManager {

    private static final Map<UUID, ItemStack[]> backpackContents = new HashMap<>();
    private static File file;
    private static FileConfiguration config;

    @Getter
    private static BackpackManager instance;

    public static void init(File dataFolder) {
        file = new File(dataFolder, "backpacks.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        loadBackpacks();
    }

    public static Inventory getBackpack(Player player) {
        UUID playerId = player.getUniqueId();
        Inventory backpack = Bukkit.createInventory(null, 45, "Backpack");

        if (backpackContents.containsKey(playerId)) {
            ItemStack[] contents = backpackContents.get(playerId);
            backpack.setContents(contents);
        }

        return backpack;
    }

    public static void saveBackpack(Player player, Inventory backpack) {
        UUID playerId = player.getUniqueId();
        ItemStack[] contents = backpack.getContents();
        backpackContents.put(playerId, contents);
        saveBackpacks();
    }

    private static void loadBackpacks() {
        for (String key : config.getKeys(false)) {
            UUID playerId = UUID.fromString(key);
            ItemStack[] contents = Objects.requireNonNull(config.getList(key)).toArray(new ItemStack[0]);
            backpackContents.put(playerId, contents);
        }
    }

    private static void saveBackpacks() {
        for (Map.Entry<UUID, ItemStack[]> entry : backpackContents.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}