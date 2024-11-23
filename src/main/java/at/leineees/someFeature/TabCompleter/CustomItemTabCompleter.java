package at.leineees.someFeature.TabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CustomItemTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("givecustomitem")) {
            List<String> suggestions = new ArrayList<>();
            if (args.length == 1) {
                suggestions.add("fly_feather");
                suggestions.add("aspect_of_the_void");
                suggestions.add("grappling_hook");
                suggestions.add("tree_fella");
                suggestions.add("super_pickaxe");
                //more item suggestions
            }
            return suggestions;
        }
        return null;
    }
}