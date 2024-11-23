package at.leineees.someFeature.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().endsWith("'s Inventory")) {
            Player viewer = (Player) event.getWhoClicked();
            String targetName = event.getView().getTitle().replace("'s Inventory", "");
            Player target = Bukkit.getPlayer(targetName);

            if (target != null) {
                Inventory targetInventory = target.getInventory();
                Inventory clickedInventory = event.getClickedInventory();

                if (clickedInventory != null && clickedInventory.equals(event.getView().getTopInventory())) {
                    int slot = event.getSlot();
                    ItemStack currentItem = event.getCurrentItem();
                    targetInventory.setItem(slot, currentItem);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().endsWith("'s Inventory")) {
            Player viewer = (Player) event.getPlayer();
            String targetName = event.getView().getTitle().replace("'s Inventory", "");
            Player target = Bukkit.getPlayer(targetName);

            if (target != null) {
                Inventory targetInventory = target.getInventory();
                Inventory viewerInventory = event.getInventory();

                for (int i = 0; i < targetInventory.getSize(); i++) {
                    targetInventory.setItem(i, viewerInventory.getItem(i));
                }

                ItemStack[] armorContents = new ItemStack[4];
                for (int i = 0; i < 4; i++) {
                    armorContents[i] = viewerInventory.getItem(36 + i);
                }
                target.getInventory().setArmorContents(armorContents);

                target.getInventory().setItemInOffHand(viewerInventory.getItem(40));
            }
        }
    }
}