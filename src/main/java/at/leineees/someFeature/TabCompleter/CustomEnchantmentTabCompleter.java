package at.leineees.someFeature.TabCompleter;

import at.leineees.someFeature.Enchantments.CustomEnchantment;
import at.leineees.someFeature.Enchantments.EnchantmentManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CustomEnchantmentTabCompleter implements TabCompleter {
    private final EnchantmentManager manager;

    public CustomEnchantmentTabCompleter(EnchantmentManager manager) {
        this.manager = manager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("cenchant")) {
            if (args.length == 1) {
                // Suggest enchantment names
                for (CustomEnchantment enchantment : manager.getAllEnchantments()) {
                    if (enchantment.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(enchantment.getKey().getKey());
                    }
                }
            } else if (args.length == 2) {
                // Suggest levels
                CustomEnchantment enchantment = manager.getEnchantment(args[0]);
                if (enchantment != null) {
                    try {
                        int level = Integer.parseInt(args[1]);
                        for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                            if (String.valueOf(i).startsWith(args[1])) {
                                completions.add(String.valueOf(i));
                            }
                        }
                    } catch (NumberFormatException e) {
                        // If the input is not a number, suggest all levels
                        for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                            completions.add(String.valueOf(i));
                        }
                    }
                }
            }
        }

        return completions;
    }
}