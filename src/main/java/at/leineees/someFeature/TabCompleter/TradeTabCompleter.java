package at.leineees.someFeature.TabCompleter;

import at.leineees.someFeature.Tools.TabCompleteHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TradeTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("trade")) {
            if (args.length == 1) {
                return TabCompleteHelper.filterSuggestions(args[0], List.of("coins"));
            } else if (args.length == 2 && args[0].equalsIgnoreCase("coins")) {
                List<String> completions = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
                return TabCompleteHelper.filterSuggestions(args[1], completions);
            } else if (args.length == 3 && args[0].equalsIgnoreCase("coins")) {
                return TabCompleteHelper.filterSuggestions(args[2], Arrays.asList("1", "10", "100"));
            }
        }
        return null;
    }
}