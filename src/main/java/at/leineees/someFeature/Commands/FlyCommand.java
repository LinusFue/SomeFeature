package at.leineees.someFeature.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("fly")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("somefeature.fly")) {
                    if (!player.getAllowFlight()) {
                        player.setAllowFlight(true);
                        player.sendMessage("§aYou can now fly!");
                    } else {
                        player.setAllowFlight(false);
                        player.sendMessage("§cYou can no longer fly!");
                    }
                } else {
                    player.sendMessage("§cYou do not have permission to fly.");
                }
            } else {
                sender.sendMessage("§cThis command can only be used by a player.");
            }
            return true;
        }
        return false;
    }
}