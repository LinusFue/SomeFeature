package at.leineees.someFeature.Enchantments;

import at.leineees.someFeature.Enchantments.Enchants.LogFortuneEnchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentManager {
    private static final Map<String, CustomEnchantment> enchantments = new HashMap<>();

    public EnchantmentManager(Plugin plugin) {
        registerEnchantment(new LogFortuneEnchantment(plugin));
        // register more enchantments here
    }

    private void registerEnchantment(CustomEnchantment enchantment) {
        enchantments.put(enchantment.getKey().getKey(), enchantment);
    }

    public static CustomEnchantment getEnchantment(String key) {
        return enchantments.get(key.toLowerCase());
    }

    /*** Applies an enchantment to an item. */
    public static void applyEnchantment(ItemStack item, CustomEnchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(enchantment.getKey(), PersistentDataType.INTEGER, level);

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add("§9" + enchantment.getKey().getKey() + "_" + level);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static boolean hasEnchantment(ItemStack item, CustomEnchantment enchantment) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(enchantment.getKey(), PersistentDataType.INTEGER);
    }

    public static int getLevel(ItemStack item, CustomEnchantment enchantment) {
        if (item == null || !item.hasItemMeta()) return 0;
        return item.getItemMeta().getPersistentDataContainer()
                .getOrDefault(enchantment.getKey(), PersistentDataType.INTEGER, 0);
    }

    public static List<CustomEnchantment> getAppliedCustomEnchantments(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return new ArrayList<>();
        List<CustomEnchantment> itemEnchants = new ArrayList<>();
        for (CustomEnchantment e : getAllEnchantments()) {
            if (hasEnchantment(item, e)) {
                itemEnchants.add(e);
            }
        }
        return itemEnchants;
    }

    public static Iterable<CustomEnchantment> getAllEnchantments() {
        return enchantments.values();
    }
}