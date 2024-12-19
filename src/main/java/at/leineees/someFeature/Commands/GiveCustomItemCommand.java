package at.leineees.someFeature.Commands;

import at.leineees.someFeature.CustomItems.CustomItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.security.Key;
import java.util.Map;
import java.util.function.Supplier;

public class GiveCustomItemCommand implements CommandExecutor {
    private final CustomItems customItems;

    public GiveCustomItemCommand(CustomItems customItems) {
        this.customItems = customItems;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("givecustomitem")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("minecraft.give")) {
                    if (args.length == 0) {
                        sender.sendMessage("§cUsage: /givecustomitem <item>");
                        return true;
                    }
                    Map<String, Supplier<ItemStack>> items = CustomItems.getAllCustomItems();
                    if (!items.containsKey(args[0].toLowerCase())) {
                        sender.sendMessage("§cUnknown item: " + args[0]);
                        return true;
                    }
                    ItemStack customItem = CustomItems.getCustomItem(args[0].toLowerCase());
                    if(customItem != null) {
                        player.getInventory().addItem(customItem);
                        sender.sendMessage("§aGiven item: " + args[0]);
                    }
                    return true;
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