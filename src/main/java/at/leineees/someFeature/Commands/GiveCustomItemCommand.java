package at.leineees.someFeature.Commands;

import at.leineees.someFeature.CustomItems.CustomItems;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public class GiveCustomItemCommand implements CommandExecutor {

    public GiveCustomItemCommand() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("givecustomitem")) {
            if (sender instanceof Player player) {
                if (player.hasPermission("minecraft.give")) {
                    if (args.length == 0) {
                        sender.sendMessage("§cUsage: /givecustomitem <item>");
                        return true;
                    }
                    if (args[0].contains(":") && args[0].split(":").length == 2) {
                        Map<NamespacedKey, Supplier<ItemStack>> items = CustomItems.getAllCustomItems();
                        NamespacedKey key = new NamespacedKey(args[0].toLowerCase().split(":")[0], args[0].toLowerCase().split(":")[1]);
                        if (!items.containsKey(key)) {
                            sender.sendMessage("§cUnknown item: " + args[0]);
                            return true;
                        }
                        ItemStack customItem = CustomItems.getCustomItem(key);
                        if (customItem != null) {
                            player.getInventory().addItem(customItem);
                            sender.sendMessage("§aGiven item: " + args[0]);
                        }
                        return true;
                    } else {
                        sender.sendMessage("§cInvalid item format. Use <namespace>:<key>");
                        return true;
                    }
                } else {
                    sender.sendMessage("§cYou do not have permission to use this command.");
                }
            } else {
                sender.sendMessage("§cThis command can only be used by a player.");
            }
            return true;
        }
        return false;
    }
}