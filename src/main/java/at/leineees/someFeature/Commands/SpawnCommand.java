package at.leineees.someFeature.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private Location spawnLocation;

    public SpawnCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        loadSpawnLocation();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern verwendet werden.");
            return true;
        }

        if (args.length == 0) {
            if (spawnLocation != null) {
                player.teleport(spawnLocation);
                player.sendMessage("§aDu wurdest zum Spawn teleportiert!");
            } else {
                player.sendMessage("§cEs wurde noch kein Spawn gesetzt!");
            }
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("setspawn")) {
            if (player.hasPermission("somefeature.setspawn")) {
                spawnLocation = player.getLocation();
                saveSpawnLocation();
                player.sendMessage("§aSpawn wurde erfolgreich gesetzt!");
            } else {
                player.sendMessage("§cDu hast keine Berechtigung den Spawn zu setzen!");
            }
            return true;
        }

        return false;
    }

    private void saveSpawnLocation() {
        FileConfiguration config = plugin.getConfig();
        config.set("spawn.world", spawnLocation.getWorld().getName());
        config.set("spawn.x", spawnLocation.getX());
        config.set("spawn.y", spawnLocation.getY());
        config.set("spawn.z", spawnLocation.getZ());
        config.set("spawn.yaw", spawnLocation.getYaw());
        config.set("spawn.pitch", spawnLocation.getPitch());
        plugin.saveConfig();
    }

    private void loadSpawnLocation() {
        FileConfiguration config = plugin.getConfig();
        if (config.contains("spawn.world")) {
            World world = Bukkit.getWorld(config.getString("spawn.world"));
            double x = config.getDouble("spawn.x");
            double y = config.getDouble("spawn.y");
            double z = config.getDouble("spawn.z");
            float yaw = (float) config.getDouble("spawn.yaw");
            float pitch = (float) config.getDouble("spawn.pitch");
            spawnLocation = new Location(world, x, y, z, yaw, pitch);
        }
    }
}

