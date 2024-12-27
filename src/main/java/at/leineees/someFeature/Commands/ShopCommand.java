package at.leineees.someFeature.Commands;

import at.leineees.someFeature.Economy.CustomItemShop;
import at.leineees.someFeature.Tools.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ShopCommand implements CommandExecutor {
    private final CustomItemShop customItemShop;

    public ShopCommand(CustomItemShop customItemShop) {
        this.customItemShop = customItemShop;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("shop")) {
            List<String> items = TabCompleteHelper.getAllItems();
            if (sender instanceof Player player) {
                if (args.length == 1 && !args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("list")) {
                    // Open the shop
                    String shopName = args[0];
                    customItemShop.openShop(player, shopName);
                    return true;
                } else if (args.length == 2 && args[0].equalsIgnoreCase("create") && player.hasPermission("somefeature.shop.edit")) {
                    // Create a new shop
                    String shopName = args[1];
                    customItemShop.createShop(shopName);
                    player.sendMessage("§aShop " + shopName + " created!");
                    return true;
                } else if (args.length == 2 && args[0].equalsIgnoreCase("remove") && player.hasPermission("somefeature.shop.edit")) {
                    // Remove a shop
                    String shopName = args[1];
                    customItemShop.removeShop(shopName);
                    player.sendMessage("§aShop " + shopName + " removed!");
                    return true;
                } else if (args.length == 1 && args[0].equalsIgnoreCase("list") && player.hasPermission("somefeature.shop.edit")) {
                    // List all shops
                    Set<String> shopNames = customItemShop.getShopNames();
                    if (shopNames.isEmpty()) {
                        player.sendMessage("§cThere are no shops available.");
                    } else {
                        player.sendMessage("§aShops: " + shopNames);
                    }
                    return true;
                } else if (args.length == 5 && args[1].equalsIgnoreCase("additem")) {
                    // Add item to the shop
                    if (player.hasPermission("somefeature.addshopitem")) {
                        String shopName = args[0];
                        String itemType = args[2];
                        int cost;
                        int amount;

                        try {
                            cost = Integer.parseInt(args[3]);
                            amount = Integer.parseInt(args[4]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("§cCost and amount must be valid numbers!");
                            return true;
                        }
                        if (items.contains(itemType)) {
                            customItemShop.addShopItem(shopName, itemType, cost, amount);
                            player.sendMessage("§aItem added to the shop " + shopName + "!");
                        } else {
                            player.sendMessage("§cItem not found!");
                        }
                        return true;
                    } else {
                        player.sendMessage("§cYou do not have permission to use this command.");
                    }
                } else if (args.length == 3 && args[1].equalsIgnoreCase("removeitem")) {
                    // Remove item from the shop
                    if (player.hasPermission("somefeature.removeshopitem")) {
                        String shopName = args[0];
                        String itemType = args[2];

                        customItemShop.removeShopItem(shopName, itemType);
                        player.sendMessage("§aItem removed from the shop " + shopName + "!");
                        return true;
                    } else {
                        player.sendMessage("§cYou do not have permission to use this command.");
                    }
                } else {
                    player.sendMessage("§cUsage: /shop <shopName> or /shop create <shopName> or /shop remove <shopName> or /shop <shopName> additem <namespace:item> <cost> <amount> or /shop <shopName> removeitem <namespace:item>");
                }
            }
        }
        return false;
    }
}
