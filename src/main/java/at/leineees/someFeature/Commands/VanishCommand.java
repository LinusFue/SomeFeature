package at.leineees.someFeature.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishCommand implements Listener, CommandExecutor {
    private final JavaPlugin plugin;
    private final Set<UUID> vanishedPlayers = new HashSet<>();

    public VanishCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (player.hasPermission("somefeature.vanish")) {
            if (vanishedPlayers.contains(player.getUniqueId())) {
                showPlayer(player);
                player.sendMessage("§aYou are now visible.");
            } else {
                hidePlayer(player);
                player.sendMessage("§aYou are now invisible.");
            }
            return true;
        } else {
            player.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }
    }

    private void hidePlayer(Player player) {
        vanishedPlayers.add(player.getUniqueId());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(plugin, player);
        }
        player.setPlayerListName(null);
        player.setCustomNameVisible(false);
        player.setInvisible(true);
        player.setCollidable(false);
    }

    private void showPlayer(Player player) {
        vanishedPlayers.remove(player.getUniqueId());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(plugin, player);
        }
        player.setPlayerListName(player.getName());
        player.setCustomNameVisible(true);
        player.setInvisible(false);
        player.setCollidable(true);
    }

    @org.bukkit.event.EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (UUID vanishedUUID : vanishedPlayers) {
            Player vanished = Bukkit.getPlayer(vanishedUUID);
            if (vanished != null) {
                player.hidePlayer(plugin, vanished);
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        vanishedPlayers.remove(player.getUniqueId());
    }
}