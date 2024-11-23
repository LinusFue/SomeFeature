package at.leineees.someFeature.Listener;

import at.leineees.someFeature.Enchantments.EnchantmentManager;
import at.leineees.someFeature.Enchantments.EnchantmentUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AnvilListener implements Listener {
    private final EnchantmentManager manager;
    private final JavaPlugin plugin;

    public AnvilListener(EnchantmentManager manager, JavaPlugin plugin) {
        this.manager = manager;
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        ItemStack item1 = anvil.getItem(0);
        ItemStack item2 = anvil.getItem(1);
        if (item1 != null & item2 != null) {
            ItemStack result = EnchantmentUtils.combineItems(item1, item2);
            anvil.setItem(2, result);
            event.setResult(result);
            anvil.setResult(result);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof AnvilInventory) {
            AnvilInventory anvil = (AnvilInventory) event.getInventory();
            if (event.getSlot() == 2) {
                ItemStack result = anvil.getItem(2);
                if (result != null) {
                    event.setCurrentItem(result);
                }
            }
        }
    }
}