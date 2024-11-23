package at.leineees.someFeature.Enchantments;

import at.leineees.someFeature.CustomItems.CustomEnchantmentShard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentUtils {

    //FIX METHOD!!!
    public static ItemStack combineItems(ItemStack item1, ItemStack item2) {
        if (CustomEnchantmentShard.isItemShard(item1) && CustomEnchantmentShard.isItemShard(item2)) {
            CustomEnchantmentShard shard1 = CustomEnchantmentShard.fromItemStack(item1);
            CustomEnchantmentShard shard2 = CustomEnchantmentShard.fromItemStack(item2);

            if (shard1 != null && shard2 != null && shard1.getEnchantment().equals(shard2.getEnchantment())) {
                int newLevel = shard1.getLevel() + 1;
                return new CustomEnchantmentShard(shard1.getEnchantment(), newLevel).createEnchantedPrismarineShard();
            }
        } else {
            ItemStack tool = item1;
            ItemStack shard = CustomEnchantmentShard.isItemShard(item2) ? item2 : null;

            if (shard == null) {
                return null;
            }

            ItemMeta toolMeta = tool.getItemMeta();
            PersistentDataContainer toolContainer = toolMeta.getPersistentDataContainer();
            CustomEnchantmentShard shardEnchantment = CustomEnchantmentShard.fromItemStack(shard);

            if (shardEnchantment != null) {
                NamespacedKey shardKey = shardEnchantment.getEnchantment().getKey();
                Integer toolLevel = toolContainer.get(shardKey, PersistentDataType.INTEGER);
                List<String> lore = toolMeta.getLore();
                if(toolMeta == null) {
                    lore = new ArrayList<>();
                }
                int shardLevel = shardEnchantment.getLevel();

                if (toolLevel != null && toolLevel.equals(shardLevel)) {
                    lore.add(ChatColor.BLUE + "" + shardEnchantment.getEnchantment().getKey().getKey() + "_" + shardLevel);
                    toolMeta.setLore(lore);
                    toolContainer.set(shardKey, PersistentDataType.INTEGER, toolLevel + 1);
                } else {
                    lore.add(ChatColor.BLUE + "" + shardEnchantment.getEnchantment().getKey().getKey() + "_" + shardLevel);
                    toolMeta.setLore(lore);
                    toolContainer.set(shardKey, PersistentDataType.INTEGER, shardLevel);
                }

                tool.setItemMeta(toolMeta);
                return tool;
            }
        }

        return null;
    }
}