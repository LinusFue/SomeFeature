package at.leineees.someFeature.CustomItems;

import at.leineees.someFeature.SomeFeature;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomItems {

    private static final Map<String, ItemStack> customItems = new HashMap<>();


    // Register custom items
    static {
        registerCustomItem("somefeature:fly_feather", createFlyFeather());
        registerCustomItem("somefeature:aspect_of_the_void", createAOTV());
        registerCustomItem("somefeature:grappling_hook", createGrapplingHook());
        registerCustomItem("somefeature:tree_fella", createTreeFella());
        registerCustomItem("somefeature:super_pickaxe", createSuperPickaxe());
        registerCustomItem("somefeature:flying_fish", createFlyingFish());
    }

    private static void registerCustomItem(String key, ItemStack item) {
        customItems.put(key, item);
    }

    public static ItemStack getCustomItem(String key) {
        return customItems.get(key);
    }

    public static Map<String, ItemStack> getAllCustomItems() {
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

    public static ItemStack createAOTV() {
        ItemStack item = new ItemStack(Material.DIAMOND_SHOVEL); // Choose your material
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§5Aspect of the Void");
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "aspect_of_the_void");
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



}
