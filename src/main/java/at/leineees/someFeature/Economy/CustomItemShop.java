package at.leineees.someFeature.Economy;

import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.Data.Coins.CoinManager;
import at.leineees.someFeature.SomeFeature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CustomItemShop implements Listener {
    private final CoinManager coinManager;
    private final CustomItems customItems;
    private final Map<String, List<ShopItem>> shops = new HashMap<>();
    private final File shopFile;

    public CustomItemShop(CoinManager coinManager, CustomItems customItems) {
        this.coinManager = coinManager;
        this.customItems = customItems;
        this.shopFile = new File(SomeFeature.getInstance().getDataFolder(), "shops.yml");
        if (!shopFile.exists()) {
            SomeFeature.getInstance().saveResource("shops.yml", false);
        }
        loadShops();
    }

    private void loadShops() {
        if (!shopFile.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(shopFile);
        for (String shopName : config.getKeys(false)) {
            List<?> items = config.getList(shopName + ".items");
            List<ShopItem> shopItems = new ArrayList<>();

            if (items != null) {
                for (Object item : items) {
                    if (item instanceof Map) {
                        Map<String, Object> itemConfig = (Map<String, Object>) item;
                        String itemType = (String) itemConfig.get("itemType");
                        int cost = (int) itemConfig.get("cost");
                        int amount = itemConfig.containsKey("amount") ? (int) itemConfig.get("amount") : 1;

                        ShopItem shopItem = new ShopItem(cost, itemType, SomeFeature.getInstance().toString(), amount);
                        shopItems.add(shopItem);
                    }
                }
            }
            shops.put(shopName, shopItems);
        }
    }

    public void saveShops() {
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<String, List<ShopItem>> entry : shops.entrySet()) {
            List<Map<String, Object>> items = new ArrayList<>();
            for (ShopItem shopItem : entry.getValue()) {
                Map<String, Object> itemConfig = new HashMap<>();
                itemConfig.put("itemType", shopItem.getItemType());
                itemConfig.put("cost", shopItem.getCost());
                itemConfig.put("amount", shopItem.getAmount());
                items.add(itemConfig);
            }
            config.set(entry.getKey() + ".items", items);
        }

        try {
            config.save(shopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createShop(String shopName) {
        if (!shops.containsKey(shopName)) {
            shops.put(shopName, new ArrayList<>());
        }
    }

    public void removeShop(String shopName) {
        shops.remove(shopName);
    }

    public void addShopItem(String shopName, String itemType, int cost, int amount) {
        shops.computeIfAbsent(shopName, k -> new ArrayList<>()).add(new ShopItem(cost, itemType, SomeFeature.getInstance().toString(), amount));
    }

    public void removeShopItem(String shopName, String itemType) {
        List<ShopItem> shopItems = shops.get(shopName);
        if (shopItems != null) {
            shopItems.removeIf(item -> item.getItemType().equals(itemType));
        }
    }

    public Set<String> getShopNames() {
        return shops.keySet();
    }

    public List<String> getShopItemTypes(String shopName) {
        List<String> itemTypes = new ArrayList<>();
        List<ShopItem> shopItems = shops.get(shopName);
        if (shopItems != null) {
            for (ShopItem shopItem : shopItems) {
                itemTypes.add(shopItem.getItemType());
            }
        }
        return itemTypes;
    }

    public List<ShopItem> getShopItems(String shopName) {
        return shops.get(shopName);
    }

    public void openShop(Player player, String shopName) {
        List<ShopItem> shopItems = shops.get(shopName);
        if (shopItems == null) {
            player.sendMessage("§cShop not found!");
            return;
        }

        Inventory shop = Bukkit.createInventory(null, 54, shopName);

        for (ShopItem shopItem : shopItems) {
            String onlyItemType = shopItem.getItemType().split(":")[1];
            NamespacedKey itemType = new NamespacedKey(shopItem.getNamespace(), onlyItemType);

            ItemStack itemStack;

            if (itemType.getNamespace().equals("somefeature")) {
                itemStack = CustomItems.getCustomItem(itemType);
                if (itemStack == null) {
                    Bukkit.getLogger().warning("Custom item not found: " + itemType.getKey());
                    continue;
                }
                ItemMeta meta = itemStack.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add("§6Cost: " + shopItem.getCost() + " coins");
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                itemStack.setAmount(shopItem.getAmount());
            } else {
                Material material = Material.matchMaterial(itemType.getKey());
                if (material == null) {
                    Bukkit.getLogger().warning("Invalid material: " + itemType.getKey());
                    continue;
                }
                itemStack = new ItemStack(material);
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    List<String> lore = new ArrayList<>();
                    lore.add("§6Cost: " + shopItem.getCost() + " coins");
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);
                    itemStack.setAmount(shopItem.getAmount());
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

        if (shops.keySet().stream().anyMatch(shopName -> event.getView().getTitle().startsWith(shopName)) && event.getClickedInventory() != player.getInventory()) {
            event.setCancelled(true);
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                ItemMeta meta = clickedItem.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();
                if (meta != null) {
                    String itemKey = null;
                    if (container.has(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING)) {
                        itemKey = container.get(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING);
                    } else {
                        itemKey = "minecraft:" + clickedItem.getType().toString().toLowerCase();
                    }
                    for (List<ShopItem> shopItems : shops.values()) {
                        for (ShopItem shopItem : shopItems) {
                            if (shopItem.getItemType().equals(itemKey)) {
                                int cost = shopItem.getCost();
                                if (coinManager.getCoins(player) >= cost) {
                                    ItemStack itemToAdd = clickedItem.clone();
                                    itemToAdd.setAmount(shopItem.getAmount()); // Set the amount
                                    ItemMeta itemMeta = itemToAdd.getItemMeta();
                                    if (itemMeta != null) {
                                        itemMeta.setLore(null); // Remove the cost lore
                                        itemToAdd.setItemMeta(itemMeta);
                                    }
                                    if (itemToAdd != null) {
                                        player.getInventory().addItem(itemToAdd);
                                        player.sendMessage("§aYou have purchased " + meta.getDisplayName() + " §afor §6" + cost + " coins.");
                                        coinManager.removeCoins(player, cost);
                                    } else {
                                        player.sendMessage("§cFailed to add item to inventory.");
                                    }
                                } else {
                                    player.sendMessage("§cYou do not have enough coins to purchase this item.");
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}