package at.leineees.someFeature.Economy;

import at.leineees.someFeature.Data.Coins.CoinManager;
import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.SomeFeature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomItemShop implements Listener {
    private final CoinManager coinManager;
    private final CustomItems customItems;
    private final List<ShopItem> shopItems = new ArrayList<>();
    private final String shopItemsYml = """
            items:
              - itemType: "somefeature:fly_feather"
                cost: 1200
                meta:
                  lore:
                    - "§7Allows you to fly."
              - itemType: "minecraft:diamond_sword"
                cost: 500
                meta:
                  lore:
                    - "§7A powerful sword."
              - itemType: "somefeature:grappling_hook"
                cost: 1000
                meta:
                  lore:
                    - "&7Allows you to grapple to blocks."
            """;

    public CustomItemShop(CoinManager coinManager, CustomItems customItems) {
        this.coinManager = coinManager;
        this.customItems = customItems;
        loadShopItems();
    }

    private void loadShopItems() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new StringReader(shopItemsYml));
        List<?> items = config.getList("items");

        if (items != null) {
            for (Object item : items) {
                if (item instanceof Map) {
                    Map<String, Object> itemConfig = (Map<String, Object>) item;
                    String itemType = (String) itemConfig.get("itemType");
                    int cost = (int) itemConfig.get("cost");
                    int amount = itemConfig.containsKey("amount") ? (int) itemConfig.get("amount") : 1;

                    ShopItem shopItem = new ShopItem(cost, itemType, SomeFeature.getInstance().toString(), amount);

                    Map<String, Object> metaConfig = (Map<String, Object>) itemConfig.get("meta");
                    if (metaConfig != null) {
                        List<String> lore = (List<String>) metaConfig.get("lore");
                        if (lore != null) {
                            shopItem.setMeta(new ShopItem.Meta(lore));
                        }
                    }

                    shopItems.add(shopItem);
                }
            }
        }
    }
    
    

        public void openShop(Player player) {
        Inventory shop = Bukkit.createInventory(null, 36, "Item Shop");

        for (ShopItem shopItem : shopItems) {
            String itemType = shopItem.getItemType();
            ItemStack itemStack;

            if (itemType.startsWith("somefeature:")) {
                itemStack = CustomItems.getCustomItem(itemType);
                ItemMeta meta = itemStack.getItemMeta();
                List<String> lore = new ArrayList<>();
                for (String line : shopItem.getMeta().getLore()) {
                    lore.add(line);
                }
                lore.add("§6Cost: " + shopItem.getCost() + " coins");
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            } else {
                Material material = Material.matchMaterial(itemType);
                if (itemType == null || material == null) {
                    Bukkit.getLogger().warning("Invalid item type: " + itemType);
                    continue;
                }
                itemStack = new ItemStack(material);
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    List<String> lore = new ArrayList<>();
                    for (String line : shopItem.getMeta().getLore()) {
                        lore.add(line);
                        lore.add("§6Cost: " + shopItem.getCost() + " coins");
                    }
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);
                }
            }
            shop.addItem(itemStack);
        }

        player.openInventory(shop);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (event.getView().getTitle().equals("Item Shop")) {
            event.setCancelled(true);
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                ItemMeta meta = clickedItem.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();
                if (meta != null) {
                    String itemKey = null;
                    if(container.has(SomeFeature.getInstance().CUSTOM_ITEM_KEY, PersistentDataType.STRING)){
                        itemKey = "somefeature:" + container.get(SomeFeature.getInstance().CUSTOM_ITEM_KEY, PersistentDataType.STRING);
                    }else{
                        itemKey = "minecraft:" + clickedItem.getType().toString().toLowerCase();
                    }
                    for (ShopItem shopItem : shopItems) {
                        if(shopItem.getItemType().equals(itemKey)) {
                            int cost = shopItem.getCost();
                            if (coinManager.getCoins(player) >= cost) {
                                ItemStack itemToAdd = null;
                                if (shopItem.getNamespace().equals("somefeature")) {
                                    itemToAdd = customItems.getCustomItem(shopItem.getItemType());
                                } else if (shopItem.getNamespace().equals("minecraft")) {
                                    itemToAdd = clickedItem.clone();
                                }
                                if (itemToAdd != null) {
                                    player.getInventory().addItem(itemToAdd);
                                    player.sendMessage("§aYou have purchased " + meta.getDisplayName() + " for " + cost + " coins.");
                                    coinManager.removeCoins(player, cost);
                                } else {
                                    player.sendMessage("§cFailed to add item to inventory.");
                                }
                            } else {
                                player.sendMessage("§cYou do not have enough coins to purchase this item.");
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}