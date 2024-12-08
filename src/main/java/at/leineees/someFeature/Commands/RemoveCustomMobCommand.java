package at.leineees.someFeature.Commands;

import at.leineees.someFeature.Data.CustomMob.CustomMobManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveCustomMobCommand implements CommandExecutor {
    private final CustomMobManager customMobManager;

    public RemoveCustomMobCommand(CustomMobManager customMobManager) {
        this.customMobManager = customMobManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("removecustommob")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("@m")) {
                        customMobManager.removeAllCustomMobs();
                        player.sendMessage("§aAll custom mobs have been removed and lastId reset to 0.");
                    } else {
                        try {
                            int id = Integer.parseInt(args[0]);
                            customMobManager.removeCustomMob(id);
                            player.sendMessage("§aCustom mob with ID " + id + " has been removed.");
                        } catch (NumberFormatException e) {
                            String nameTag = args[0];
                            customMobManager.removeCustomMobByNameTag(nameTag);
                            player.sendMessage("§aCustom mob(s) with name tag " + nameTag + " have been removed.");
                        }
                    }
                } else {
                    player.sendMessage("§cUsage: /removecustommob [nametag|@m]");
                }
                return true;
            }
        }
        return false;
    }
}