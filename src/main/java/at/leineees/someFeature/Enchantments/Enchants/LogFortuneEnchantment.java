package at.leineees.someFeature.Enchantments.Enchants;

import at.leineees.someFeature.Enchantments.BaseEnchantment;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class LogFortuneEnchantment extends BaseEnchantment {

    public LogFortuneEnchantment(Plugin plugin) {
        super(plugin, "log_fortune");
    }

    @Override
    public String getName() {
        return "Log Fortune";
    }
    public NamespacedKey getKey() {
        return super.getKey();
    }
    @Override
    public int getMaxLevel() {
        return 3;
    }

    
}
