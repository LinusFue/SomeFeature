package at.leineees.someFeature.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public abstract class BaseEnchantment implements CustomEnchantment {
    protected final String key;
    protected final Plugin plugin;

    public BaseEnchantment(Plugin plugin, String key) {
        this.plugin = plugin;
        this.key = key;
    }

    public NamespacedKey getKey() {
        return new NamespacedKey(plugin, key);
    }
}
