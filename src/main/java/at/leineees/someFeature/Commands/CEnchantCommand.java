package at.leineees.someFeature.Commands;

import at.leineees.someFeature.Enchantments.CustomEnchantment;
import at.leineees.someFeature.Enchantments.EnchantmentManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CEnchantCommand implements CommandExecutor {
    private final EnchantmentManager manager;

    public CEnchantCommand(EnchantmentManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("cenchant")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("somefeature.enchant")) {
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + "Usage: /cenchant <enchantment> <level>");
                        return true;
                    }

                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.getType() == Material.AIR) {
                        player.sendMessage(ChatColor.RED + "You must be holding an item!");
                        return true;
                    }

                    CustomEnchantment enchant = manager.getEnchantment(args[0]);
                    if (enchant == null) {
                        player.sendMessage(ChatColor.RED + "Unknown enchantment!");
                        return true;
                    }

                    try {
                        int level = Integer.parseInt(args[1]);
                        if (level < 1 || level > enchant.getMaxLevel()) {
                            player.sendMessage(ChatColor.RED + "Invalid level!");
                            return true;
                        }

                        manager.applyEnchantment(item, enchant, level);
                        player.sendMessage(ChatColor.GREEN + "Enchantment applied!");

                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Invalid level!");
                    }
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                }
            }
        }
        return false;
    }
}