package at.leineees.someFeature.Tools;

import at.leineees.someFeature.SomeFeature;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemChecker {

    public static String getCustomItemKey(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(SomeFeature.getInstance().CUSTOM_ITEM_KEY, PersistentDataType.STRING)) {
            return container.get(SomeFeature.getInstance().CUSTOM_ITEM_KEY, PersistentDataType.STRING);
        }

        return null;
    }
}