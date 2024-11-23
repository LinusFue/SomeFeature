package at.leineees.someFeature.Economy;

import at.leineees.someFeature.CustomItems.CustomEnchantmentShard;
import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.Data.Coins.CoinManager;
import at.leineees.someFeature.Enchantments.EnchantmentManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemShop implements Listener {
    private final CoinManager coinManager;
    private final CustomItems customItems;

    public CustomItemShop(CoinManager coinManager, CustomItems customItems) {
        this.coinManager = coinManager;
        this.customItems = customItems;
    }

    public void openShop(Player player) {
        Inventory shop = Bukkit.createInventory(null, 36, "Item Shop");

        ItemStack flyFeather = customItems.createFlyFeather();
        ItemStack aotv = customItems.createAOTV();
        ItemStack grapplingHook = customItems.createGrapplingHook();
        ItemStack treeFella = customItems.createTreeFella();
        ItemStack superPickaxe = customItems.createSuperPickaxe();
        ItemStack enchantmentShards = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta meta = enchantmentShards.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "Enchantment Shards");
            enchantmentShards.setItemMeta(meta);
        }

        shop.setItem(10, flyFeather);
        shop.setItem(11, aotv);
        shop.setItem(12, grapplingHook);
        shop.setItem(13, treeFella);
        shop.setItem(14, superPickaxe);
        shop.setItem(15, enchantmentShards);

        player.openInventory(shop);
    }

    public void openEnchantmentShardShop(Player player) {
        Inventory shardShop = Bukkit.createInventory(null, 27, ChatColor.AQUA + "Enchantment Shards");
        
        ItemStack shardLevel1 = new CustomEnchantmentShard(EnchantmentManager.getEnchantment("log_fortune"), 1).createEnchantedPrismarineShard();
        ItemStack shardLevel2 = new CustomEnchantmentShard(EnchantmentManager.getEnchantment("log_fortune"), 2).createEnchantedPrismarineShard();
        ItemStack shardLevel3 = new CustomEnchantmentShard(EnchantmentManager.getEnchantment("log_fortune"), 3).createEnchantedPrismarineShard();

        shardShop.setItem(10, shardLevel1);
        shardShop.setItem(11, shardLevel2);
        shardShop.setItem(12, shardLevel3);

        player.openInventory(shardShop);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (event.getView().getTitle().equals("Item Shop")) {
            event.setCancelled(true);
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                ItemMeta meta = clickedItem.getItemMeta();
                if (meta != null && meta.getDisplayName().equals(ChatColor.AQUA + "Enchantment Shards")) {
                    Bukkit.broadcastMessage("Opening Enchantment Shard Shop");
                    openEnchantmentShardShop(player);
                } else {
                    int cost = getItemCost(clickedItem);
                    if (coinManager.getCoins(player) >= cost) {
                        coinManager.removeCoins(player, cost);
                        player.getInventory().addItem(clickedItem);
                        player.sendMessage(ChatColor.GREEN + "You have purchased " + meta.getDisplayName() + " for " + cost + " coins.");
                    } else {
                        player.sendMessage(ChatColor.RED + "You do not have enough coins to purchase this item.");
                    }
                }
            }
        } else if (event.getView().getTitle().equals(ChatColor.AQUA + "Enchantment Shards")) {
            Bukkit.broadcastMessage("You are in the Enchantment Shard Shop");
            event.setCancelled(true);
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                int cost = getShardCost(clickedItem);
                if (coinManager.getCoins(player) >= cost) {
                    coinManager.removeCoins(player, cost);
                    player.getInventory().addItem(clickedItem);
                    player.sendMessage(ChatColor.GREEN + "You have purchased " + clickedItem.getItemMeta().getDisplayName() + " for " + cost + " coins.");
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough coins to purchase this item.");
                }
            }
        }
    }

    private int getItemCost(ItemStack item) {
        String displayName = item.getItemMeta().getDisplayName();
        if (displayName.equals(ChatColor.GOLD + "Flight Feather")) {
            return 1200;
        } else if (displayName.equals(ChatColor.DARK_PURPLE + "Aspect of the Void")) {
            return 69696969;
        } else if (displayName.equals(ChatColor.GRAY + "Grappling Hook")) {
            return 800;
        } else if (displayName.equals(ChatColor.GOLD + "Tree Fella")) {
            return 1000;
        } else if (displayName.equals(ChatColor.DARK_PURPLE + "Super Pickaxe")) {
            return 2000;
        }
        return 987654321;
    }

    private int getShardCost(ItemStack item) {
        String displayName = item.getItemMeta().getDisplayName();
        if (displayName.equals(ChatColor.LIGHT_PURPLE + "Enchanted Shard") && item.getItemMeta().getLore().get(0).contains("log_fortune_1")) {
            return 500;
        } else if (displayName.equals(ChatColor.LIGHT_PURPLE + "Enchanted Shard") && item.getItemMeta().getLore().get(0).contains("log_fortune_2")) {
            return 1000;
        } else if (displayName.equals(ChatColor.LIGHT_PURPLE + "Enchanted Prismarine Shard Level 3") && item.getItemMeta().getLore().get(0).contains("log_fortune_3")) {
            return 1500;
        }
        return 987654321;
    }
}