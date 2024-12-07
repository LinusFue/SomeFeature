package at.leineees.someFeature.Commands;

import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.SomeFeature;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

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
                        sender.sendMessage(ChatColor.RED + "Usage: /givecustomitem <item>");
                        return true;
                    }
                    //Items
                    switch (args[0].toLowerCase()) {
                        case "fly_feather":
                            player.getInventory().addItem(customItems.createFlyFeather());
                            break;
                        case "aspect_of_the_void":
                            player.getInventory().addItem(customItems.createAOTV());
                            break;
                        case "grappling_hook":
                            player.getInventory().addItem(customItems.createGrapplingHook());
                            break;
                        case "tree_fella":
                            player.getInventory().addItem(customItems.createTreeFella());
                            break;
                        case "super_pickaxe":
                            player.getInventory().addItem(customItems.createSuperPickaxe());
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
                            break;
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
            }
            return true;
        }
        return false;
    }    
}