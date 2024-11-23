package at.leineees.someFeature.Commands;

import at.leineees.someFeature.Data.CustomMob.CustomMobData;
import at.leineees.someFeature.Data.CustomMob.CustomMobManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SpawnCustomMobCommand implements CommandExecutor {
    private final CustomMobManager customMobManager;

    public SpawnCustomMobCommand(CustomMobManager customMobManager) {
        this.customMobManager = customMobManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("spawncustommob")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length >= 9) {
                    try {
                        EntityType entityType = EntityType.valueOf(args[0].toUpperCase());
                        String nameTag = args[1];
                        double x = Double.parseDouble(args[2]);
                        double y = Double.parseDouble(args[3]);
                        double z = Double.parseDouble(args[4]);
                        boolean ai = Boolean.parseBoolean(args[5]);
                        boolean silent = Boolean.parseBoolean(args[6]);
                        boolean invulnerable = Boolean.parseBoolean(args[7]);
                        String cmd = String.join(" ", Arrays.copyOfRange(args, 8, args.length));
                        Location location = new Location(player.getWorld(), x, y, z);
                        CustomMobData customMobData = new CustomMobData(customMobManager.getLastId(), entityType, nameTag, cmd, location, ai, silent, invulnerable);
                        customMobManager.spawnCustomMob(customMobData);
                        player.sendMessage(ChatColor.GREEN + "Custom mob spawned successfully!");
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(ChatColor.RED + "Invalid entity type, coordinates, or boolean values.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /spawncustommob [minecraft:entity] [nametag] [x] [y] [z] [ai] [silent] [invulnerable] [command]");
                }
                return true;
            }
        }
        return false;
    }
}