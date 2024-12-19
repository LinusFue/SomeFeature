package at.leineees.someFeature.CustomItems;

import at.leineees.someFeature.SomeFeature;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomItems {

    private static final Map<String, Supplier<ItemStack>> customItems = new HashMap<>();


    // Register custom items
    static {
        registerCustomItem("fly_feather", CustomItems::createFlyFeather);
        registerCustomItem("grappling_hook", CustomItems::createGrapplingHook);
        registerCustomItem("tree_fella", CustomItems::createTreeFella);
        registerCustomItem("super_pickaxe", CustomItems::createSuperPickaxe);
        registerCustomItem("flying_fish", CustomItems::createFlyingFish);
        registerCustomItem("healing_spell_1", () -> CustomItems.createHealingSpell(1));
        registerCustomItem("healing_spell_2", () -> CustomItems.createHealingSpell(2));
        registerCustomItem("healing_spell_3", () -> CustomItems.createHealingSpell(3));
    }

    private static void registerCustomItem(String key, Supplier<ItemStack> itemSupplier) {
        customItems.put(key, itemSupplier);
    }

    public static ItemStack getCustomItem(String key) {
        Supplier<ItemStack> itemSupplier = customItems.get(key);
        return itemSupplier != null ? itemSupplier.get() : null;
    }
    
    public static Map<String, Supplier<ItemStack>> getAllCustomItems() {
        return new HashMap<>(customItems);
    }
    
    //customItemLayout
    public ItemStack createCustomItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(lore));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "custom_item");
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createFlyFeather() {
        ItemStack item = new ItemStack(Material.FEATHER); // Choose your material
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Flight Feather");
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "fly_feather");
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createGrapplingHook() {
        ItemStack item = new ItemStack(Material.FISHING_ROD); // Choose your material
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§7Grappling Hook");
            meta.setLore(Arrays.asList("§8Cooldown: 3 seconds"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "grappling_hook");
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createTreeFella() {
        ItemStack item = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Tree Fella");
            meta.setLore(Arrays.asList("§8Instantly chop's down trees!", "§8Cooldown: 2 seconds"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "tree_fella");
            item.setItemMeta(meta);
        }
        return item;
    }
    
    public static ItemStack createSuperPickaxe() {
        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§5Super Pickaxe");
            meta.setLore(Arrays.asList("§8Diggs out a 3x3 Area!"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "super_pickaxe");
            item.setItemMeta(meta);
        }
        return item;
    }
    
    public static ItemStack createFlyingFish(){
        ItemStack item = new ItemStack(Material.COD);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            meta.setDisplayName("§6Flying Fish");
            meta.setLore(Arrays.asList("§8Allows you to craft a Custom Item!"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "flying_fish");
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createHealingSpell(int level) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aHealing Spell Level " + level);
            int cooldown;
            double healAmount;
            switch (level) {
                case 1:
                    cooldown = 20000;
                    healAmount = 4.0;
                    break;
                case 2:
                    cooldown = 10000;
                    healAmount = 4.0;
                    break;
                case 3:
                    cooldown = 7000;
                    healAmount = 6.0;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid level: " + level);
            }
            meta.setLore(Arrays.asList("§8Heals you for " + (healAmount / 2) + " hearts!", "§8Cooldown: " + (cooldown / 1000) + " seconds"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "healing_spell");
            container.set(SomeFeature.CUSTOM_ITEM_LEVEL_KEY, PersistentDataType.INTEGER, level);
            item.setItemMeta(meta);
        }
        return item;
    }

}
