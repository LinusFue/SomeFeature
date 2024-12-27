package at.leineees.someFeature.Economy;

import at.leineees.someFeature.Data.Coins.CoinManager;
import at.leineees.someFeature.SomeFeature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class SellItems implements Listener {
    private final PriceManager priceManager;
    private final CoinManager coinManager;

    public SellItems(PriceManager priceManager, CoinManager coinManager) {
        this.priceManager = priceManager;
        this.coinManager = coinManager;
    }

    public void openSellInventory(Player player) {
        Inventory sellInventory = Bukkit.createInventory(null, 27, "Sell");

        // Add confirm button to the inventory
        updateConfirmButton(sellInventory, 0);

        player.openInventory(sellInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (event.getView().getTitle().equals("Sell")) {
            event.setCancelled(true);

            if (clickedItem != null && clickedItem.getType() == Material.EMERALD_BLOCK && clickedItem.getItemMeta().getDisplayName().equals("§aConfirm")) {
                sellItemsInInventory(player, event.getInventory());
                player.sendMessage("§aItems sold.");
            } else if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                if (event.getClickedInventory() != player.getInventory()) {
                    player.getInventory().addItem(clickedItem);
                    event.getInventory().setItem(event.getSlot(), null);
                    int totalCoins = calculateTotalCoins(event.getInventory());
                    updateConfirmButton(event.getInventory(), totalCoins);
                    player.sendMessage("§aItem returned to inventory.");
                } else {
                    String itemType;
                    ItemMeta meta = clickedItem.getItemMeta();
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    if (container.has(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING)) {
                        itemType = container.get(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING);
                    } else if (clickedItem.getType().getKey().toString().startsWith("minecraft:")) {
                        itemType = clickedItem.getType().getKey().toString();
                    } else {
                        itemType = null;
                    }
                    assert itemType != null;
                    int price = priceManager.getPrice(new NamespacedKey(itemType.split(":")[0], itemType.split(":")[1]));

                    if (price > 0) {
                        if (event.getInventory().firstEmpty() != -1) {
                            event.getInventory().addItem(clickedItem);
                            player.getInventory().setItem(event.getSlot(), null);
                            int totalCoins = calculateTotalCoins(event.getInventory());
                            updateConfirmButton(event.getInventory(), totalCoins);
                        } else {
                            player.sendMessage("§cThe sell inventory is full.");
                        }
                    } else {
                        player.sendMessage("§cThis item cannot be sold.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("Sell")) {
            Inventory inventory = event.getInventory();
            for (int i = 0; i < 26; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    event.getPlayer().getInventory().addItem(item);
                }
            }
        }
    }

    private void sellItemsInInventory(Player player, Inventory inventory) {
        int totalCoins = 0;

        for (int i = 0; i < 26; i++) { // Exclude the confirm button slot
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                String itemType;
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();
                if (container.has(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING)) {
                    itemType = container.get(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING);
                } else if (item.getType().getKey().toString().startsWith("minecraft:")) {
                    itemType = item.getType().getKey().toString();
                } else {
                    itemType = null;
                }
                assert itemType != null;
                int price = priceManager.getPrice(new NamespacedKey(itemType.split(":")[0], itemType.split(":")[1]));

                if (price > 0) {
                    int amount = item.getAmount();
                    totalCoins += price * amount;
                    inventory.setItem(i, null); // Remove item from inventory
                } else {
                    player.getInventory().addItem(item); // Return unsellable items to player
                }
            }
        }

        if (totalCoins > 0) {
            coinManager.addCoins(player, totalCoins);
            player.sendMessage("§aYou sold items for §6" + totalCoins + " coins.");
        } else {
            player.sendMessage("§cNo sellable items found.");
        }

        player.closeInventory();
    }

    private int calculateTotalCoins(Inventory inventory) {
        int totalCoins = 0;

        for (int i = 0; i < 26; i++) { // Exclude the confirm button slot
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                String itemType;
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();
                if (container.has(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING)) {
                    itemType = container.get(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING);
                } else if (item.getType().getKey().toString().startsWith("minecraft:")) {
                    itemType = item.getType().getKey().toString();
                } else {
                    itemType = null;
                }
                assert itemType != null;
                int price = priceManager.getPrice(new NamespacedKey(itemType.split(":")[0], itemType.split(":")[1]));

                if (price > 0) {
                    int amount = item.getAmount();
                    totalCoins += price * amount;
                }
            }
        }

        return totalCoins;
    }

    private void updateConfirmButton(Inventory inventory, int totalCoins) {
        ItemStack confirmButton = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirmButton.getItemMeta();
        if (confirmMeta != null) {
            confirmMeta.setDisplayName("§aConfirm");
            confirmMeta.setLore(List.of("§6" + totalCoins + " coins"));
            confirmButton.setItemMeta(confirmMeta);
        }
        inventory.setItem(26, confirmButton);
    }
}