package at.leineees.someFeature.CustomItems;

import at.leineees.someFeature.SomeFeature;
import io.lumine.mythic.bukkit.utils.lib.jooq.Meta;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class CustomItems {
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
            meta.setDisplayName(ChatColor.GOLD + "Flight Feather");
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
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Aspect of the Void");
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
            meta.setDisplayName(ChatColor.GRAY + "Grappling Hook");
            meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Cooldown: 3 seconds"));
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
            meta.setDisplayName(ChatColor.GOLD + "Tree Fella");
            meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Instantly chop's down trees!", ChatColor.DARK_GRAY + "Cooldown: 2 seconds"));
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
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Super Pickaxe");
            meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Diggs out a 3x3 Area!"));
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
            meta.setDisplayName(ChatColor.GOLD + "Flying Fish");
            meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Allows you to craft a Custom Item!"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "flying_fish");
            item.setItemMeta(meta);
        }
        return item;
    }



}
