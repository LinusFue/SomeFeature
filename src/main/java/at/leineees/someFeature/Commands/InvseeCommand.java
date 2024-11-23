package at.leineees.someFeature.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InvseeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /invsee <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        Inventory targetInventory = Bukkit.createInventory(player, 45, target.getName() + "'s Inventory");

        // Copy target's inventory contents
        ItemStack[] contents = target.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            targetInventory.setItem(i, contents[i]);
        }

        // Copy target's armor contents
        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            ItemStack armorItem = armorContents[i];
            targetInventory.setItem(36 + i, armorItem);
        }

        // Copy target's offhand item
        ItemStack offHandItem = target.getInventory().getItemInOffHand();
        targetInventory.setItem(40, offHandItem);

        player.openInventory(targetInventory);
        return true;
    }
}