package at.leineees.someFeature.Enchantments;

import org.bukkit.NamespacedKey;

public interface CustomEnchantment {
    String getName();
    NamespacedKey getKey();
    int getMaxLevel();
}
