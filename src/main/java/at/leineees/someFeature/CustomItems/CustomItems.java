package at.leineees.someFeature.CustomItems;

import at.leineees.someFeature.SomeFeature;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomItems {
    //KEYS
    public static final NamespacedKey FLY_FEATHER = new NamespacedKey(SomeFeature.getInstance(), "fly_feather");
    public static final NamespacedKey GRAPPLING_HOOK = new NamespacedKey(SomeFeature.getInstance(), "grappling_hook");
    public static final NamespacedKey TREE_FELLA = new NamespacedKey(SomeFeature.getInstance(), "tree_fella");
    public static final NamespacedKey SUPER_PICKAXE = new NamespacedKey(SomeFeature.getInstance(), "super_pickaxe");
    public static final NamespacedKey FLYING_FISH = new NamespacedKey(SomeFeature.getInstance(), "flying_fish");
    public static final NamespacedKey HEALING_SPELL_1 = new NamespacedKey(SomeFeature.getInstance(), "healing_spell_1");
    public static final NamespacedKey HEALING_SPELL_2 = new NamespacedKey(SomeFeature.getInstance(), "healing_spell_2");
    public static final NamespacedKey HEALING_SPELL_3 = new NamespacedKey(SomeFeature.getInstance(), "healing_spell_3");
    public static final NamespacedKey ELYTRA_CHESTPLATE = new NamespacedKey(SomeFeature.getInstance(), "elytra_chestplate");
    

    private static final Map<NamespacedKey, Supplier<ItemStack>> customItems = new HashMap<>();


    // Register custom items
    static {
        registerCustomItem(FLY_FEATHER, CustomItems::createFlyFeather);
        registerCustomItem(GRAPPLING_HOOK, CustomItems::createGrapplingHook);
        registerCustomItem(TREE_FELLA, CustomItems::createTreeFella);
        registerCustomItem(SUPER_PICKAXE, CustomItems::createSuperPickaxe);
        registerCustomItem(FLYING_FISH, CustomItems::createFlyingFish);
        registerCustomItem(HEALING_SPELL_1, () -> CustomItems.createHealingSpell(1));
        registerCustomItem(HEALING_SPELL_2, () -> CustomItems.createHealingSpell(2));
        registerCustomItem(HEALING_SPELL_3, () -> CustomItems.createHealingSpell(3));
        registerCustomItem(ELYTRA_CHESTPLATE, CustomItems::createElytraChestplate);
    }

    private static void registerCustomItem(NamespacedKey key, Supplier<ItemStack> itemSupplier) {
        customItems.put(key, itemSupplier);
    }

    public static ItemStack getCustomItem(NamespacedKey key) {
        Supplier<ItemStack> itemSupplier = customItems.get(key);
        return itemSupplier != null ? itemSupplier.get() : null;
    }
    
    public static Map<NamespacedKey, Supplier<ItemStack>> getAllCustomItems() {
        return customItems;
    }
    
    //customItemLayout
    public ItemStack createCustomItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(lore));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, "custom_item");
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
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, FLY_FEATHER.toString());
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
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, GRAPPLING_HOOK.toString());
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
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, TREE_FELLA.toString());
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
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, SUPER_PICKAXE.toString());
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
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, FLYING_FISH.toString());
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
            NamespacedKey key;
            switch (level) {
                case 1:
                    cooldown = 20000;
                    healAmount = 4.0;
                    key = HEALING_SPELL_1;
                    break;
                case 2:
                    cooldown = 10000;
                    healAmount = 4.0;
                    key = HEALING_SPELL_2;
                    break;
                case 3:
                    cooldown = 7000;
                    healAmount = 6.0;
                    key = HEALING_SPELL_3;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid level: " + level);
            }
            meta.setLore(Arrays.asList("§8Heals you for " + (healAmount / 2) + " hearts!", "§8Cooldown: " + (cooldown / 1000) + " seconds"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, key.toString());
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createElytraChestplate() {
        ItemStack item = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Elytra Chestplate");
            meta.setLore(Arrays.asList("§8Combines the power of a Netherite Chestplate and an Elytra"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, ELYTRA_CHESTPLATE.toString());
            item.setItemMeta(meta);
        }
        return item;
    }
    
    public static boolean isCustomItem(ItemStack item) {
        if (item == null || item.getItemMeta() == null) {
            return false;
        }
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.has(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING);
    }

}
