package at.leineees.someFeature.Listener;

import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.SomeFeature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class AnvilRecipeListener implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack firstItem = inventory.getItem(0);
        ItemStack secondItem = inventory.getItem(1);

        if (firstItem != null && secondItem != null) {
            if (firstItem.getType() == Material.NETHERITE_CHESTPLATE
                    && secondItem.getType() == Material.ELYTRA) {

                ItemMeta firstItemMeta = firstItem.getItemMeta();
                if (firstItemMeta != null) {
                    PersistentDataContainer container = firstItemMeta.getPersistentDataContainer();

                    if (container.has(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING) && container.get(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING).equals("somefeature:elytra_chestplate")) {
                        event.setResult(null);
                    } else {
                        ItemStack result = CustomItems.createElytraChestplate();
                        ItemMeta resultMeta = result.getItemMeta();
                        if (resultMeta != null) {
                            for (Enchantment enchantment : firstItem.getEnchantments().keySet()) {
                                resultMeta.addEnchant(enchantment, firstItem.getEnchantmentLevel(enchantment), true);
                            }
                            result.setItemMeta(resultMeta);
                        }
                        event.setResult(result);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof AnvilInventory) {
            AnvilInventory inventory = (AnvilInventory) event.getInventory();
            if (event.getSlot() == 2) {
                ItemStack result = event.getCurrentItem();
                if (result != null && result.getType() != Material.AIR) {
                    event.setCursor(result);
                    inventory.setItem(0, null);
                    inventory.setItem(1, null);
                    event.setCurrentItem(null);
                    event.setCancelled(true);
                }
            }
        }
    }
}