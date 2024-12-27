package at.leineees.someFeature.TabCompleter;

import at.leineees.someFeature.Tools.TabCompleteHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoinTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("coins")) {
            if (sender instanceof Player player) {
                if (args.length == 1) {
                    if (player.hasPermission("somefeature.coins.others")) {
                        List<String> playerNames = new ArrayList<>();
                        for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
                            playerNames.add(onlinePlayer.getName());
                        }
                        return playerNames;
                    }
                } else if (args.length == 2) {
                    if (player.hasPermission("somefeature.coins.manage")) {
                        return TabCompleteHelper.filterSuggestions(args[1], Arrays.asList("add", "remove", "set"));
                    }
                } else if (args.length == 3) {
                    if (player.hasPermission("somefeature.coins.manage")) {
                        return TabCompleteHelper.filterSuggestions(args[2], Arrays.asList("1", "10", "100", "1000"));
                    }
                }
            }
        }
        return null;
    }
}