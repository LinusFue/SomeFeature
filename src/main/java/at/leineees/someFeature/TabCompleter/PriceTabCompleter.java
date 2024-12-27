package at.leineees.someFeature.TabCompleter;

import at.leineees.someFeature.Economy.PriceManager;
import at.leineees.someFeature.Tools.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PriceTabCompleter implements TabCompleter {
    private final PriceManager priceManager;

    public PriceTabCompleter(PriceManager priceManager) {
        this.priceManager = priceManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            return TabCompleteHelper.filterSuggestions(args[0], Arrays.asList("list", "set", "remove"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                return TabCompleteHelper.filterSuggestions(args[1], TabCompleteHelper.getAllItems());
            } else if (args[0].equalsIgnoreCase("remove")) {
                return TabCompleteHelper.filterSuggestions(args[1], priceManager.getPrices().keySet());
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                suggestions.add("<price>");
            }
        }
        return suggestions;
    }
}