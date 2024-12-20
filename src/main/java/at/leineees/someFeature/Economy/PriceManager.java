package at.leineees.someFeature.Economy;

import at.leineees.someFeature.SomeFeature;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PriceManager {
    private final File priceFile;
    private final FileConfiguration priceConfig;
    private final Map<NamespacedKey, Integer> prices = new HashMap<>();

    public PriceManager(File file) {
        priceFile = new File(file, "prices.yml");
        priceConfig = YamlConfiguration.loadConfiguration(priceFile);
        loadPrices();
    }

    public void loadPrices() {
        for (String key : priceConfig.getKeys(false)) {
            String keyNamespace = key.split(":")[0];
            String keyName = key.split(":")[1];
            prices.put(new NamespacedKey(keyNamespace, keyName), priceConfig.getInt(key));
        }
    }

    public void savePrices() {
        for (Map.Entry<NamespacedKey, Integer> entry : prices.entrySet()) {
            priceConfig.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            priceConfig.save(priceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPrice(NamespacedKey itemType) {
        return prices.getOrDefault(itemType, 0);
    }

    public void setPrice(NamespacedKey itemType, int price) {
        prices.put(itemType, price);
        savePrices();
    }

    public Map<NamespacedKey, Integer> getPrices() {
        return prices;
    }
}