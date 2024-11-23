package at.leineees.someFeature.Commands;

import at.leineees.someFeature.Economy.CustomItemShop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopCommand implements CommandExecutor {
    private final CustomItemShop customItemShop;

    public ShopCommand(CustomItemShop customItemShop) {
        this.customItemShop = customItemShop;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("shop")) {
            if (sender instanceof Player) {
                customItemShop.openShop((Player) sender);
            } else {
                sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
            }
            return true;
        }
        return false;
    }
}
