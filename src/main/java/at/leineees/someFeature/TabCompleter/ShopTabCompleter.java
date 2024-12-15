package at.leineees.someFeature.TabCompleter;

import at.leineees.someFeature.Economy.CustomItemShop;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import at.leineees.someFeature.CustomItems.CustomItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopTabCompleter implements TabCompleter {
    private final CustomItemShop customItemShop;

    public ShopTabCompleter(CustomItemShop customItemShop) {
        this.customItemShop = customItemShop;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("shop")) {
            List<String> items = new ArrayList<>();
            for (String customItem : CustomItems.getAllCustomItems().keySet()) {
                items.add(customItem);
            }
            for (Material material : Material.values()) {
                items.add("minecraft:" +  material.name().toLowerCase());
            }
            if (args.length == 1) {
                List<String> suggestions = new ArrayList<>(customItemShop.getShopNames());
                suggestions.add("create");
                suggestions.add("remove");
                suggestions.add("list");
                return suggestions;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
                return new ArrayList<>(Arrays.asList("<ShopName>"));
            }else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                return filterSuggestions(args[1], new ArrayList<>(customItemShop.getShopNames()));
            }else if (args.length == 2 && !args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("list")) {
                return filterSuggestions(args[1], new ArrayList<>(Arrays.asList("additem", "removeitem")));
            } else if (args.length == 3 && args[1].equalsIgnoreCase("additem")) {
                return filterSuggestions(args[2], items);
            } else if (args.length == 4 && args[1].equalsIgnoreCase("additem")) {
                return filterSuggestions(args[3], Arrays.asList("100", "200", "300"));
            } else if (args.length == 5 && args[1].equalsIgnoreCase("additem")) {
                return filterSuggestions(args[4], Arrays.asList("1", "32", "64"));
            } else if (args.length == 3 && args[1].equalsIgnoreCase("removeitem")) {
                if(customItemShop.getShopItems(args[0]) != null) {
                    return filterSuggestions(args[2], customItemShop.getShopItemTypes(args[0]));
                }
            }
        }
        return null;
    }

    private List<String> filterSuggestions(String input, List<String> suggestions) {
        List<String> filtered = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().contains(input.toLowerCase())) {
                filtered.add(suggestion);
            }
        }
        return filtered;
    }
}