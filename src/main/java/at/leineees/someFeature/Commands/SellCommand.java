package at.leineees.someFeature.Commands;

import at.leineees.someFeature.Economy.SellItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SellCommand implements CommandExecutor {
    private final SellItems sellItems;

    public SellCommand(SellItems sellItems) {
        this.sellItems = sellItems;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sellItems.openSellInventory(player);
            return true;
        }
        return false;
    }
}