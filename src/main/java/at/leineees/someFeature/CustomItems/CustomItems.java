package at.leineees.someFeature.CustomItems;

import at.leineees.someFeature.SomeFeature;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    public static final NamespacedKey RECIPE_BOOK = new NamespacedKey(SomeFeature.getInstance(), "recipe_book");
    public static final NamespacedKey MULTI_TOOL = new NamespacedKey(SomeFeature.getInstance(), "multi_tool");
    public static final NamespacedKey MULTI_TOOL_BASE = new NamespacedKey(SomeFeature.getInstance(), "multi_tool_base");
    public static final NamespacedKey LIFE_RING_1 = new NamespacedKey(SomeFeature.getInstance(), "life_ring_1");
    public static final NamespacedKey LIFE_RING_2 = new NamespacedKey(SomeFeature.getInstance(), "life_ring_2");
    public static final NamespacedKey LIFE_RING_3 = new NamespacedKey(SomeFeature.getInstance(), "life_ring_3");
    public static final NamespacedKey BACKPACK = new NamespacedKey(SomeFeature.getInstance(), "backpack");


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
        registerCustomItem(RECIPE_BOOK, CustomItems::createRecipeBook);
        registerCustomItem(MULTI_TOOL, CustomItems::createMultiTool);
        registerCustomItem(MULTI_TOOL_BASE, CustomItems::createMultiToolBase);
        registerCustomItem(LIFE_RING_1, () -> CustomItems.createLifeRing(1));
        registerCustomItem(LIFE_RING_2, () -> CustomItems.createLifeRing(2));
        registerCustomItem(LIFE_RING_3, () -> CustomItems.createLifeRing(3));
        registerCustomItem(BACKPACK, CustomItems::createBackpack);
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
            meta.setLore(List.of("§8Cooldown: 3 seconds"));
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
            meta.setLore(List.of("§8Diggs out a 3x3 Area!"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, SUPER_PICKAXE.toString());
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createFlyingFish() {
        ItemStack item = new ItemStack(Material.COD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Flying Fish");
            meta.setLore(List.of("§8Allows you to craft a Custom Item!"));
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
            meta.setLore(List.of("§8Combines the power of a Netherite Chestplate and an Elytra"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, ELYTRA_CHESTPLATE.toString());
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createRecipeBook() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§dRecipe Book");
            meta.setLore(List.of("§8Zeigt custom Item Rezepte an!"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, RECIPE_BOOK.toString());
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createMultiTool() {
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Multi Tool");
            meta.setLore(Arrays.asList("§8Allows you to bundle multiple tools into one!", "§4Experimental!"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, MULTI_TOOL.toString());
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createMultiToolBase() {
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Multi Tool Base");
            meta.setLore(Arrays.asList("§8Allows you to craft a powerful multitool!", "§4Experimental!"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, MULTI_TOOL_BASE.toString());
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createLifeRing(int level) {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aLife Ring Level " + level);
            double healthAmount = 0.0;
            NamespacedKey key;
            switch (level) {
                case 1:
                    healthAmount = 5.0;
                    key = LIFE_RING_1;
                    break;
                case 2:
                    healthAmount = 10.0;
                    key = LIFE_RING_2;
                    break;
                case 3:
                    healthAmount = 20.0;
                    key = LIFE_RING_3;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid level: " + level);
            }
            meta.setLore(Arrays.asList("§8You Gain " + healthAmount / 2 + " hearts when held in your inventory!", "§4Experimental!"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, key.toString());
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createBackpack() {
        ItemStack item = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§fBackpack");
            meta.setLore(List.of("§8Allows you to store more Items!"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING, BACKPACK.toString());
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
