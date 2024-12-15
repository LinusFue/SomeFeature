package at.leineees.someFeature.Economy;

import at.leineees.someFeature.SomeFeature;
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
    private final Map<String, Integer> prices = new HashMap<>();

    public PriceManager(File file) {
        priceFile = new File(file, "prices.yml");
        priceConfig = YamlConfiguration.loadConfiguration(priceFile);
        loadPrices();
    }

    public void loadPrices() {
        for (String key : priceConfig.getKeys(false)) {
            prices.put(key, priceConfig.getInt(key));
        }
    }

    public void savePrices() {
        for (Map.Entry<String, Integer> entry : prices.entrySet()) {
            priceConfig.set(entry.getKey(), entry.getValue());
        }
        try {
            priceConfig.save(priceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPrice(String itemType) {
        return prices.getOrDefault(itemType, 0);
    }

    public void setPrice(String itemType, int price) {
        prices.put(itemType, price);
        savePrices();
    }

    public Map<String, Integer> getPrices() {
        return prices;
    }
}