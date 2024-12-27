package at.leineees.someFeature.CustomItems;

import at.leineees.someFeature.SomeFeature;
import com.google.common.base.Preconditions;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilRecipe implements Recipe, Keyed, Listener {
    private final NamespacedKey key;
    private final ItemStack result;
    private final RecipeChoice base;
    private final RecipeChoice addition;

    public AnvilRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @Nullable RecipeChoice base, @Nullable RecipeChoice addition) {
        Preconditions.checkArgument(!result.isEmpty(), "Recipe cannot have an empty result.");
        this.key = key;
        this.result = result;
        this.base = base == null ? RecipeChoice.empty() : base.validate(true).clone();
        this.addition = addition == null ? RecipeChoice.empty() : addition.validate(true).clone();
        
        SomeFeature.getInstance().getServer().getPluginManager().registerEvents(this, SomeFeature.getInstance());
    }

    public @NotNull RecipeChoice getBase() {
        return this.base != null ? this.base.clone() : null;
    }

    public @NotNull RecipeChoice getAddition() {
        return this.addition != null ? this.addition.clone() : null;
    }

    public @NotNull ItemStack getResult() {
        return this.result.clone();
    }

    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack firstItem = inventory.getItem(0);
        ItemStack secondItem = inventory.getItem(1);

        if (firstItem != null && secondItem != null) {
            if (firstItem.getType() == getBase().getItemStack().getType()
                    && secondItem.getType() == getAddition().getItemStack().getType()) {

                ItemMeta firstItemMeta = firstItem.getItemMeta();
                if (firstItemMeta != null) {
                    PersistentDataContainer container = firstItemMeta.getPersistentDataContainer();
                    ItemStack result = getResult();
                    ItemMeta resultMeta;
                    if (container.has(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING)) {
                        event.setResult(null);
                    } else {
                        resultMeta = result.getItemMeta();
                        if (resultMeta != null) {
                            for (Enchantment enchantment : firstItem.getEnchantments().keySet()) {
                                resultMeta.addEnchant(enchantment, firstItem.getEnchantmentLevel(enchantment), true);
                            }
                            result.setItemMeta(resultMeta);
                        }
                        event.setResult(result);
                        inventory.setRepairCost(1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getType() == InventoryType.ANVIL) {
            AnvilInventory inventory = (AnvilInventory) event.getInventory();
            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                ItemStack result = event.getCurrentItem();
                if (result != null && result.getType() != Material.AIR) {
                    Player player = (Player) event.getWhoClicked();
                    int repairCost = inventory.getRepairCost();
                    if (player.getLevel() >= repairCost) {
                        player.setLevel(player.getLevel() - repairCost);
                        if(result.getType() != Material.AIR) {
                            event.setCursor(result);
                            inventory.setItem(0, null);
                            inventory.setItem(1, null);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}