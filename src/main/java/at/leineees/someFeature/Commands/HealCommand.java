package at.leineees.someFeature.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("heal")) {
                {
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(25);
                    player.sendMessage("§aYou have been healed");
                }
                return true;
            } else {
                sender.sendMessage("§cOnly a Player can execute this command");
            }
        }
        return false;
    }
}
