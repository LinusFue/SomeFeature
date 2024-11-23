package at.leineees.someFeature.Commands;

import at.leineees.someFeature.Data.Coins.CoinManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoinsCommand implements CommandExecutor {
    private final CoinManager coinManager;

    public CoinsCommand(CoinManager coinManager) {
        this.coinManager = coinManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("coins")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.sendMessage(ChatColor.GREEN + "You have " + coinManager.getCoins(player) + " coins.");
                    return true;
                } else if (args.length == 1 && player.hasPermission("somefeature.coins.others")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        player.sendMessage(ChatColor.GREEN + target.getName() + " has " + coinManager.getCoins(target) + " coins.");
                    } else {
                        player.sendMessage(ChatColor.RED + "Player not found.");
                    }
                    return true;
                } else if (args.length == 3 && player.hasPermission("somefeature.coins.manage")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        String action = args[1];
                        int amount;
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "Invalid amount.");
                            return true;
                        }
                        if (action.equalsIgnoreCase("add")) {
                            coinManager.addCoins(target, amount);
                            player.sendMessage(ChatColor.GREEN + "Added " + amount + " coins to " + target.getName() + ".");
                        } else if (action.equalsIgnoreCase("remove")) {
                            coinManager.removeCoins(target, amount);
                            player.sendMessage(ChatColor.GREEN + "Removed " + amount + " coins from " + target.getName() + ".");
                        } else {
                            player.sendMessage(ChatColor.RED + "Usage: /coins <player> <add|remove> <amount>");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Player not found.");
                    }
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /coins [player] [add|remove] [amount]");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
                return true;
            }
        }
        return false;
    }
}