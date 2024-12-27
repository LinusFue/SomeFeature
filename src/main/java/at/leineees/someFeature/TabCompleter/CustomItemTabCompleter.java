package at.leineees.someFeature.TabCompleter;

import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.Tools.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class CustomItemTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("givecustomitem")) {
            return TabCompleteHelper.filterSuggestions(args[0], CustomItems.getAllCustomItems().keySet());
        }
        return null;
    }
}