package at.leineees.someFeature.Commands;

import at.leineees.someFeature.Data.Coins.CoinManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TradeCommand implements CommandExecutor {
    private final CoinManager coinManager;

    public TradeCommand(CoinManager coinManager) {
        this.coinManager = coinManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("trade")) {
            if (sender instanceof Player player) {
                if (args.length == 3 && args[0].equalsIgnoreCase("coins")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        int amount;
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("§cInvalid amount.");
                            return true;
                        }
                        if (coinManager.getCoins(player) >= amount && amount > 0) {
                            coinManager.removeCoins(player, amount);
                            coinManager.addCoins(target, amount);
                            player.sendMessage("§aYou have traded " + amount + " coins to " + target.getName() + ".");
                            target.sendMessage("§aYou have received " + amount + " coins from " + player.getName() + ".");
                        } else {
                            player.sendMessage("§cYou do not have enough coins.");
                        }
                    } else {
                        player.sendMessage("§cPlayer not found.");
                    }
                    return true;
                } else {
                    player.sendMessage("§cUsage: /trade coins <player> <amount>");
                    return true;
                }
            } else {
                sender.sendMessage("§cThis command can only be used by a player.");
                return true;
            }
        }
        return false;
    }
}