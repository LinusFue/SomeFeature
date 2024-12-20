package at.leineees.someFeature.Commands;

import at.leineees.someFeature.Economy.PriceManager;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class PriceCommand implements CommandExecutor {
    private final PriceManager priceManager;

    public PriceCommand(PriceManager priceManager) {
        this.priceManager = priceManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /price <list|set|remove> [arguments]");
            return false;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "list":
                handleListCommand(sender);
                break;
            case "set":
                if (args.length != 3) {
                    sender.sendMessage("§cUsage: /price set <item> <price>");
                    return false;
                }
                handleSetCommand(sender, new NamespacedKey(args[1].split(":")[0], args[1].split(":")[1]), args[2]);
                break;
            case "remove":
                if (args.length != 2) {
                    sender.sendMessage("§cUsage: /price remove <item>");
                    return false;
                }
                handleRemoveCommand(sender, args[1]);
                break;
            default:
                sender.sendMessage("§cUnknown subcommand. Usage: /price <list|set|remove> [arguments]");
                return false;
        }

        return true;
    }

    private void handleListCommand(CommandSender sender) {
        if (priceManager.getPrices().isEmpty()) {
            sender.sendMessage("§cNo prices set.");
            return;
        }

        sender.sendMessage("§aItem Prices:");
        for (Map.Entry<NamespacedKey, Integer> entry : priceManager.getPrices().entrySet()) {
            sender.sendMessage("§a" + entry.getKey() + ": " + entry.getValue() + " coins");
        }
    }

    private void handleSetCommand(CommandSender sender, NamespacedKey itemType, String priceStr) {
        try {
            int price = Integer.parseInt(priceStr);
            priceManager.setPrice(itemType, price);
            sender.sendMessage("§aSet price of " + itemType + " to " + price + " coins.");
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid price.");
        }
    }

    private void handleRemoveCommand(CommandSender sender, String itemType) {
        if (!priceManager.getPrices().containsKey(itemType)) {
            sender.sendMessage("§cItem does not exist.");
            return;
        }
        priceManager.getPrices().remove(itemType);
        priceManager.savePrices();
        sender.sendMessage("§aRemoved " + itemType + " from the price list.");
    }
}