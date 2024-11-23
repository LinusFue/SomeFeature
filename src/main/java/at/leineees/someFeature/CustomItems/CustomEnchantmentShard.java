package at.leineees.someFeature.CustomItems;

import at.leineees.someFeature.Enchantments.CustomEnchantment;
import at.leineees.someFeature.Enchantments.EnchantmentManager;
import at.leineees.someFeature.SomeFeature;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CustomEnchantmentShard {
    private final CustomEnchantment enchantment;
    private final int level;

    public CustomEnchantmentShard(CustomEnchantment enchantment, int lvl) {
        this.enchantment = enchantment;
        this.level = lvl;
    }

    public ItemStack createEnchantedPrismarineShard() {
        ItemStack item = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey keyEnch = new NamespacedKey(SomeFeature.getInstance(), "enchantment");
            NamespacedKey keyLevel = new NamespacedKey(SomeFeature.getInstance(), "level");
            container.set(keyEnch, PersistentDataType.STRING, enchantment.getKey().getKey());
            container.set(keyLevel, PersistentDataType.INTEGER, level);

            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Enchanted Shard");
            meta.setLore(List.of(ChatColor.BLUE + enchantment.getKey().getKey() + "_" + level));
            meta.setEnchantmentGlintOverride(true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static CustomEnchantmentShard fromItemStack(ItemStack item) {
        if (item == null || item.getType() != Material.PRISMARINE_SHARD || !item.hasItemMeta()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String enchantmentKey = container.get(new NamespacedKey(SomeFeature.getInstance(), "enchantment"), PersistentDataType.STRING);
        Integer level = container.get(new NamespacedKey(SomeFeature.getInstance(), "level"), PersistentDataType.INTEGER);

        if (enchantmentKey == null || level == null) {
            return null;
        }

        CustomEnchantment enchantment = EnchantmentManager.getEnchantment(enchantmentKey);
        if (enchantment == null) {
            return null;
        }

        return new CustomEnchantmentShard(enchantment, level);
    }

    public CustomEnchantment getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

    public String getEnchantmentKey() {
        return enchantment.getKey().getKey();
    }

    public static boolean isItemShard(ItemStack item) {
        if (item == null || item.getType() != Material.PRISMARINE_SHARD || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(new NamespacedKey(SomeFeature.getInstance(), "enchantment"), PersistentDataType.STRING) &&
                container.has(new NamespacedKey(SomeFeature.getInstance(), "level"), PersistentDataType.INTEGER);
    }
}